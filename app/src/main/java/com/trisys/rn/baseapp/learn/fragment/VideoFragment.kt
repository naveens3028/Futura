package com.trisys.rn.baseapp.learn.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.trisys.rn.baseapp.GlideApp
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.MaterialVideoList
import com.trisys.rn.baseapp.model.VideoMaterial
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import com.trisys.rn.baseapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_video.*
import java.io.*
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class VideoFragment : Fragment() {

    private lateinit var downloadFolder: File
    private lateinit var fileName: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var myPreferences: MyPreferences
    lateinit var videoNext: VideoMaterial

    lateinit var file: File


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

        val videoData =
            Gson().fromJson(myPreferences.getString(Define.VIDEO_DATA), VideoMaterial::class.java)
//        val position = myPreferences.getString(Define.VIDEO_NEXT)?.toInt() ?: -1
        /*val nextVideoString = myPreferences.getString(Define.VIDEO_NEXT).toString()
        Utils.testLog(nextVideoString)
        Utils.testLog(myPreferences.getString(Define.VIDEO_DATA).toString())
        if (!nextVideoString.isNullOrEmpty()){
            videoNext =
                Gson().fromJson(nextVideoString, VideoMaterial::class.java)
        }*/
        title.text = videoData.title
//        andExoPlayerView.startPlayer()
        /*andExoPlayerView.setSource(
            "https://player.vimeo.com/video/409534666"
        )*/
        val url = "https://player.vimeo.com/video/${videoData.description.filter { it.isDigit() }}"
        Utils.testLog(url)
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true

        webView.loadUrl(url)
        /*if (nextVideoString.isNullOrEmpty()){
            nextVideoCard.visibility = View.GONE
            upNext.visibility = View.GONE
        }
        else{
            nextVideoCard.visibility = View.VISIBLE
            upNext.visibility = View.VISIBLE
            GlideApp.with(requireContext()).load(videoNext.filePath).into(nextVideo)
        }*/

        /*fileName = downloadFolder.path + "/Mobile_Medium_T1 Life span & life cycle"
        file = File(fileName)
        if (file.exists()) {
            decryptEncryptedFile()
        } else {
            download()
        }*/
    }

    private fun download() {
        download.visibility = View.VISIBLE
        progressVal.visibility = View.VISIBLE
        content.visibility = View.GONE
        AndroidNetworking.download(
            "https://drive.google.com/u/0/uc?id=1Yg_vdLCVnfzXIoeImz6-nWBM_GWTCUe1&export=download",
            downloadFolder.path,
            "/Mobile_Medium_T1 Life span & life cycle"
        )
            .setTag("downloadTest")
            .setPriority(Priority.MEDIUM)
            .build()
            .setDownloadProgressListener { bytesDownloaded, totalBytes ->
            }
            .startDownload(object : DownloadListener {
                override fun onDownloadComplete() {
                    download.visibility = View.GONE
                    progressVal.visibility = View.GONE
                    content.visibility = View.VISIBLE
                    Snackbar.make(
                        requireActivity().window.decorView.rootView,
                        "Download Completed",
                        Snackbar.LENGTH_LONG
                    ).show()
                    /*andExoPlayerView.setSource(
                        fileName
                    )*/
                }

                override fun onError(error: ANError?) {
                    download.visibility = View.GONE
                    progressVal.visibility = View.GONE
                    content.visibility = View.VISIBLE
                    Snackbar.make(
                        requireActivity().window.decorView.rootView,
                        error?.errorDetail.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })
    }

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

    private fun decryptEncryptedFile() {
        val filePath = downloadFolder.path + "/Mobile_Medium_T1 Life span & life cycle"
        val fileData = readFile(filePath)
        val secretKey = getSecretKey(sharedPreferences)
        val encodedData = decrypt(secretKey, fileData)
        saveFile(encodedData, filePath)
        /*andExoPlayerView.setSource(
            fileName
        )*/
    }

    @Throws(Exception::class)
    fun decrypt(yourKey: SecretKey, fileData: ByteArray): ByteArray {
        val decrypted: ByteArray
        val cipher = Cipher.getInstance("AES", "BC")
        cipher.init(Cipher.DECRYPT_MODE, yourKey, IvParameterSpec(ByteArray(cipher.blockSize)))
        decrypted = cipher.doFinal(fileData)
        return decrypted
    }

}