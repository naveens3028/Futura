package com.trisys.rn.baseapp.activity

import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.MediaController
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.GetQRCode
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.utils.VideoCache
import kotlinx.android.synthetic.main.activity_test_video.*
import kotlinx.android.synthetic.main.activity_video_play.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.net.URL


class TestVideoPlayerActivity : AppCompatActivity(), OnNetworkResponse {

    private var url: String? = null
    private var videoId: String? = null
    private var bucketName: String = "upmyranksvideos"
    private var accessKey: String = "AKIAYMFPBBZPLLRCRRM7"
    private var secretKey: String = "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"
    lateinit var player: SimpleExoPlayer
    private var mediaSource: MediaSource? = null
    lateinit var networkHelper: NetworkHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_video)

        networkHelper = NetworkHelper(this)
        url = intent.getStringExtra("url")
        videoId = intent.getStringExtra("videoId")

        testRecycler.visibility = View.GONE
        toolbarLayout.visibility = View.GONE
        videoView.visibility = View.VISIBLE


        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = "Test Result"
        player = SimpleExoPlayer.Builder(this).build()
        player.setThrowsWhenUsingWrongThread(false)
        videoView.player = player

       // preparExoPlayer(url!!)
        if (!videoId.isNullOrEmpty()){


            getVideoId(videoId!!)

           /* Log.e("videoid", videoId!!)
            val myCredentials: AWSCredentials =
                BasicAWSCredentials(accessKey, secretKey)
            val s3client: AmazonS3 = AmazonS3Client(myCredentials)
            s3client.setRegion(Region.getRegion(Regions.AP_SOUTH_1))
            val request = GeneratePresignedUrlRequest(bucketName, videoId+".mp4")
            val objectURL: URL = s3client.generatePresignedUrl(request)
            Toast.makeText(this,"url" +objectURL.toString(),Toast.LENGTH_LONG).show()
           // preparExoPlayer(baseurl+videoId+".mp4")
            preparExoPlayer(objectURL.toString())
            Toast.makeText(this,videoId.toString(),Toast.LENGTH_LONG).show()
            Toast.makeText(this,baseurl+videoId+".mp4",Toast.LENGTH_LONG).show()*/


        }else{
            url?.let { preparExoPlayer(it) }
        }


    }

    private fun getVideoId(id: String) {
        networkHelper.getCall(
            URLHelper.qrcode + id,
            "qrcode",
            ApiUtils.getHeader(this),
            this
        )
    }

    fun preparExoPlayer(url: String) {
        // Build the media item.
        mediaSource = buildMediaSource(Uri.parse(url))
        // Set the media item to be played.
        player.setMediaSource(mediaSource!!)
        // val mediaItem: MediaItem = MediaItem.fromUri(url)
        // Set the media item to be played.
        //player.setMediaItem(mediaItem)
        // Prepare the player.
        player.prepare()
        // Start the playback.
        player.play()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        try {
            menuInflater.inflate(R.menu.menu_learn, menu)
            val item1 =
                menu.findItem(R.id.action_menu_notification).actionView.findViewById(R.id.layoutNotification) as RelativeLayout
            item1.setOnClickListener {
                startActivity(Intent(this, NotificationsActivity::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        player.stop()
        player.release()
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val cacheDataSourceFactory = CacheDataSourceFactory(
            VideoCache.get(this),
            DefaultHttpDataSourceFactory("exoplayer-demo")
        )
        return ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(uri)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        statefulLayout.showContent()
        if (responseCode == networkHelper.responseSuccess && tag == "qrcode") {
            val qrResponse = Gson().fromJson(response, GetQRCode::class.java)
            Toast.makeText(this,"codeurl "+ qrResponse.data.qrCodeUrl, Toast.LENGTH_LONG).show()
            Toast.makeText(this, qrResponse.data.videoUrl, Toast.LENGTH_LONG).show()
            val baseurl = "https://static.upmyranks.com/"
            preparExoPlayer(baseurl+qrResponse.data.videoUrl+".mp4")
        }else{
            Toast.makeText(this,"Unable to view the video... Try again later...",Toast.LENGTH_LONG).show()
        }
    }

}