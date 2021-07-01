package com.trisys.rn.baseapp.learn.fragment

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Environment
import android.text.PrecomputedText
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.google.android.material.snackbar.Snackbar
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.learn.LearnVideoActivity
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
        downloadFolder = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        andExoPlayerView.startPlayer()
        fileName = downloadFolder.path + "/T1 Introduction"
        file = File(fileName)
        if (file.exists()) {
            decryptEncryptedFile()
        } else {
            download()
        }
    }


    private fun showToolbarAndClearFullScreen() {
        (activity as LearnVideoActivity).supportActionBar!!.show()
        (activity as LearnVideoActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

    }

    private fun download() {
        download.visibility = View.VISIBLE
        progressVal.visibility = View.VISIBLE
        content.visibility = View.GONE
        AndroidNetworking.download(
            "https://drive.google.com/u/0/uc?id=1kV20Ymwg_bI_tRO-OaQ7uD5vvu-yLTff&export=download",
            downloadFolder.path,
            "/T1 Introduction"
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
                    andExoPlayerView.setSource(
                        fileName
                    )
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
            val filePath = downloadFolder.path + "/T1 Introduction"
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
        val filePath = downloadFolder.path + "/T1 Introduction"
        val fileData = readFile(filePath)
        val secretKey = getSecretKey(sharedPreferences)
        val encodedData = decrypt(secretKey, fileData)
        saveFile(encodedData, filePath)
        andExoPlayerView.setSource(
            fileName
        )
    }

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
        andExoPlayerView.releasePlayer()
//        encryptDownloadedFile()
    }

}