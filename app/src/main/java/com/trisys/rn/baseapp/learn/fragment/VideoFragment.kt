package com.trisys.rn.baseapp.learn.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.VideoPlayActivity
import com.trisys.rn.baseapp.helper.exoplayer.IntentUtil
import com.trisys.rn.baseapp.model.VideoMaterial
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.ImageLoader
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_video.*
import java.io.*
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList


class VideoFragment : Fragment() {

    private lateinit var downloadFolder: File
    private lateinit var fileName: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var myPreferences: MyPreferences
    lateinit var file: File
    lateinit var player: SimpleExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("MySharedPref", 0)
        myPreferences = MyPreferences(requireContext())
        downloadFolder = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        player = SimpleExoPlayer.Builder(requireContext()).build()
//        player.setThrowsWhenUsingWrongThread(false)
//        player_view.setPlayer(player)

        val videoData = Gson().fromJson(myPreferences.getString(Define.VIDEO_DATA), VideoMaterial::class.java)
//
//        val id = videoData.description.replace("https://vimeo.com/","")

        ImageLoader.loadFull(requireContext(),videoData.filePath,videoPlaceholder)



//        VimeoExtractor.getInstance()
//            .fetchVideoWithIdentifier(id, null, object : OnVimeoExtractionListener {
//                override fun onSuccess(video: VimeoVideo) {
//                    val hdStream = video.streams["720p"]
//                    println("VIMEO VIDEO STREAM$hdStream")
//                    hdStream?.let {
//                        requireActivity().runOnUiThread {
//                            //code that runs in main
//                            preparExoPlayer(it)
//                        }
//                    }
//                }
//
//                override fun onFailure(throwable: Throwable) {
//                    Log.d("failure",throwable.message!!)
//                }
//            })

    }

//    fun preparExoPlayer(url: String){
//        // Build the media item.
//        val mediaItem: MediaItem = MediaItem.fromUri(url)
//        // Set the media item to be played.
//        player.setMediaItem(mediaItem)
//        // Prepare the player.
//        player.prepare()
//        // Start the playback.
//        player.play()
//    }

    override fun onStart() {
        super.onStart()

        videoPlaceholder.setOnClickListener {
            buildMediaItems("https://eduinstitute-videos.s3.ap-south-1.amazonaws.com/VID-20190430-WA0010.mp4")
        }
    }

    fun buildMediaItems(url: String) {
        val extension = null
        val subtitleUri = null
        val title = url
        val uri = Uri.parse(url)
        val mediaItem = MediaItem.Builder()
        val adaptiveMimeType =
            Util.getAdaptiveMimeTypeForContentType(Util.inferContentType(uri, extension))
        mediaItem
            .setUri(uri)
            .setMediaMetadata(MediaMetadata.Builder().setTitle(title).build())
            .setMimeType(adaptiveMimeType)

        val playlist= PlaylistHolder(title, listOf(mediaItem.build()))
        val intent = Intent(requireContext(), VideoPlayActivity::class.java)
        intent.putExtra(
            IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA,
            true
            )

        IntentUtil.addToIntent(playlist.mediaItems, intent)
        startActivity(intent)

    }
    class PlaylistHolder constructor(title: String, mediaItems: List<MediaItem>) {
        val title: String
        val mediaItems: List<MediaItem>

        init {
            Assertions.checkArgument(!mediaItems.isEmpty())
            this.title = title
            this.mediaItems = Collections.unmodifiableList(ArrayList(mediaItems))
        }
    }

//    private fun download() {
//        download.visibility = View.VISIBLE
//        progressVal.visibility = View.VISIBLE
//        content.visibility = View.GONE
//        AndroidNetworking.download(
//            "https://drive.google.com/u/0/uc?id=1Yg_vdLCVnfzXIoeImz6-nWBM_GWTCUe1&export=download",
//            downloadFolder.path,
//            "/Mobile_Medium_T1 Life span & life cycle"
//        )
//            .setTag("downloadTest")
//            .setPriority(Priority.MEDIUM)
//            .build()
//            .setDownloadProgressListener { bytesDownloaded, totalBytes ->
//            }
//            .startDownload(object : DownloadListener {
//                override fun onDownloadComplete() {
//                    download.visibility = View.GONE
//                    progressVal.visibility = View.GONE
//                    content.visibility = View.VISIBLE
//                    Snackbar.make(
//                        requireActivity().window.decorView.rootView,
//                        "Download Completed",
//                        Snackbar.LENGTH_LONG
//                    ).show()
//                    andExoPlayerView.setSource(
//                        fileName
//                    )
//                }
//
//                override fun onError(error: ANError?) {
//                    download.visibility = View.GONE
//                    progressVal.visibility = View.GONE
//                    content.visibility = View.VISIBLE
//                    Snackbar.make(
//                        requireActivity().window.decorView.rootView,
//                        error?.errorDetail.toString(),
//                        Snackbar.LENGTH_LONG
//                    ).show()
//                }
//            })
//    }

    private fun encryptDownloadedFile() {
        try {
            val filePath = downloadFolder.path + "/Mobile_Medium_T1 Life span & life cycle"
            val fileData = readFile(filePath)
            val secretKey =
                getSecretKey(sharedPreferences) //create SecretKey & stored it in shared preferences
            val encodedData = encrypt(secretKey, fileData) //encrypt file
            saveFile(encodedData, filePath) // Save the encrypt file
        } catch (e: Exception) {
            Log.d("TAG", e.message.toString())
        }
    }

    @Throws(Exception::class)
    fun readFile(filePath: String): ByteArray {
        val file = File(filePath)
        val fileContents = file.readBytes()
        val inputBuffer = BufferedInputStream(FileInputStream(file))
        inputBuffer.read(fileContents)
        inputBuffer.close()
        return fileContents
    }

    private fun getSecretKey(sharedPref: SharedPreferences): SecretKey {
        val key =
            sharedPref.getString("secretKeyPref", null) // check secretKey whether stored or not
        if (key == null) {
            val secretKey = generateSecretKey() //generate secure random
            saveSecretKey(sharedPref, secretKey!!) //Save the key
            return secretKey
        }
        val decodedKey = Base64.decode(key, Base64.NO_WRAP) //Decode the key
        return SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
    }

    @Throws(Exception::class)
    fun generateSecretKey(): SecretKey? {
        val secureRandom = SecureRandom()
        val keyGenerator = KeyGenerator.getInstance("AES")
        //generate a key with secure random
        keyGenerator?.init(128, secureRandom)
        return keyGenerator?.generateKey()
    }

    fun saveSecretKey(sharedPref: SharedPreferences, secretKey: SecretKey): String {
        val encodedKey = Base64.encodeToString(secretKey.encoded, Base64.NO_WRAP)
        sharedPref.edit().putString("secretKeyPref", encodedKey).apply()
        return encodedKey
    }

    @Throws(Exception::class)
    fun encrypt(yourKey: SecretKey, fileData: ByteArray): ByteArray {
        val data = yourKey.encoded
        val skeySpec = SecretKeySpec(data, 0, data.size, "AES")
        val cipher = Cipher.getInstance("AES", "BC")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(fileData)
    }

    @Throws(Exception::class)
    fun saveFile(fileData: ByteArray, path: String) {
        val file = File(path)
        val bos = BufferedOutputStream(FileOutputStream(file, false))
        bos.write(fileData)
        bos.flush()
        bos.close()
    }

//    private fun decryptEncryptedFile() {
//        val filePath = downloadFolder.path + "/Mobile_Medium_T1 Life span & life cycle"
//        val fileData = readFile(filePath)
//        val secretKey = getSecretKey(sharedPreferences)
//        val encodedData = decrypt(secretKey, fileData)
//        saveFile(encodedData, filePath)
//        andExoPlayerView.setSource(
//            fileName
//        )
//    }

    @Throws(Exception::class)
    fun decrypt(yourKey: SecretKey, fileData: ByteArray): ByteArray {
        val decrypted: ByteArray
        val cipher = Cipher.getInstance("AES", "BC")
        cipher.init(Cipher.DECRYPT_MODE, yourKey, IvParameterSpec(ByteArray(cipher.blockSize)))
        decrypted = cipher.doFinal(fileData)
        return decrypted
    }

    override fun onStop() {
        super.onStop()
//        player.stop()
//        player.release()
//        encryptDownloadedFile()
    }
}