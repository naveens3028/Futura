package com.upmyranksapp.learn.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upmyranksapp.R
import com.upmyranksapp.adapter.VideoPlayedAdapter
import com.upmyranksapp.database.AppDatabase
import com.upmyranksapp.database.model.VideoPlayedItem
import com.upmyranksapp.helper.exoplayer.ExoUtil.buildMediaItems
import com.upmyranksapp.model.VideoMaterial
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.ImageLoader
import com.upmyranksapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_video.*
import vimeoextractor.OnVimeoExtractionListener
import vimeoextractor.VimeoExtractor
import vimeoextractor.VimeoVideo
import java.io.*
import java.lang.reflect.Type


class VideoFragment : Fragment(),VideoPlayedAdapter.ActionCallback {

    private lateinit var downloadFolder: File
    private lateinit var fileName: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var myPreferences: MyPreferences
    lateinit var file: File
    private var pos: Int ?= null
    var videoData = ArrayList<VideoMaterial>()
    lateinit var db: AppDatabase

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
        db = AppDatabase.getInstance(requireContext())!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        player = SimpleExoPlayer.Builder(requireContext()).build()
//        player.setThrowsWhenUsingWrongThread(false)
//        player_view.setPlayer(player)
        val listType: Type = object : TypeToken<ArrayList<VideoMaterial?>?>() {}.getType()
        videoData = Gson().fromJson(myPreferences.getString(Define.VIDEO_DATA), listType)
        pos = myPreferences.getInt(Define.VIDEO_POS)
//

        val myList = ArrayList<VideoMaterial>()
        var myPos = pos
        val listSize = videoData.size - (pos!! +1)

        for (i in 1..listSize){
            myList.add(VideoMaterial(description = videoData[myPos!!+i].description,null, null, videoData[myPos!!+i].filePath, null, null, videoData[myPos!!+i].title, videoData[myPos!!+i].videoId ))
        }


        if (!myList.isNullOrEmpty()){
            upNext.visibility = View.VISIBLE
            setAdapter(myList)
        }else{
            upNext.visibility = View.GONE
        }

        Log.e("popData1", listSize.toString())
        Log.e("popData", myList.toString())
//        val id = videoData.description.replace("https://vimeo.com/","")

        if(videoData != null) {
            videoData[pos!!].filePath?.let {
                ImageLoader.loadFull(requireContext(),
                    it, videoPlaceholder)
            }
        }
        ImageLoader.loadFull(requireContext(), videoData[pos!!].filePath!!,videoPlaceholder)

        val myClass = VideoPlayedItem(videoUrl =videoData[pos!!].description.toString(), lastPlayed = "4:10", logoImg = videoData[pos!!].filePath.toString() , videoTitle = videoData[pos!!].title.toString())
        db.videoDao.addVideo(myClass)


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
            if(videoData != null) {
                if (videoData[pos!!].description!!.contains("vimeo", true)) {
                    loadVMEOVideos(videoData[pos!!].courseName!!, videoData[pos!!].description!!)
                }else{
                    buildMediaItems(
                        requireActivity(),
                        childFragmentManager,videoData[pos!!].courseName!!,videoData[pos!!].description!!,false)
                }
            }
        }
    }

    fun loadVMEOVideos(title: String,url: String){
        val videoId = url.replace("https://vimeo.com/", "")
        VimeoExtractor.getInstance()
            .fetchVideoWithIdentifier(videoId, null, object : OnVimeoExtractionListener {
                override fun onSuccess(video: VimeoVideo) {
                    val hdStream = video.streams["720p"]
                    println("VIMEO VIDEO STREAM$hdStream")
                    hdStream?.let {
                        requireActivity().runOnUiThread {
                            //code that runs in main
                            buildMediaItems(
                                requireActivity(),
                                childFragmentManager,title,
                                it,false)
                        }
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    Log.d("failure", throwable.message!!)
                }
            })
    }

    fun setAdapter(myList: ArrayList<VideoMaterial>){
        val videoRecyclerView = view?.findViewById(R.id.upNextRecycler) as RecyclerView
        val videoAdapter = VideoPlayedAdapter(requireActivity(), "1",null, myList , this)
        videoRecyclerView.adapter = videoAdapter
    }

    override fun onVideoClickListener(videoPlayedItem: VideoPlayedItem) {
    }

    override fun onVideoClickListener1(videoPlayedItem: VideoMaterial) {
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

 /*   private fun encryptDownloadedFile() {
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
    }*/

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

/*    @Throws(Exception::class)
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
    }*/
}