package com.trisys.rn.baseapp.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.GetQRCode
import com.trisys.rn.baseapp.model.VideoMaterial
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper.qrcode
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import com.trisys.rn.baseapp.utils.VideoCache
import kotlinx.android.synthetic.main.activity_video_play.*
import kotlinx.android.synthetic.main.fragment_video.player_view
import vimeoextractor.OnVimeoExtractionListener
import vimeoextractor.VimeoExtractor
import vimeoextractor.VimeoVideo


class VideoPlayActivity : AppCompatActivity(), OnNetworkResponse {
    lateinit var player: SimpleExoPlayer
    lateinit var myPreferences: MyPreferences
    lateinit var networkHelper: NetworkHelper
    private var mediaSource: MediaSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        myPreferences = MyPreferences(this)
        networkHelper = NetworkHelper(this)
    }

    override fun onStart() {
        super.onStart()

        player = SimpleExoPlayer.Builder(this).build()
        player.setThrowsWhenUsingWrongThread(false)
        player_view.player = player


        var id = intent.getStringExtra("videoId")

        if (id.isNullOrEmpty()) {
            val videoData = Gson().fromJson(
                myPreferences.getString(Define.VIDEO_DATA),
                VideoMaterial::class.java
            )
            Log.e("popurl", videoData.description.toString())
            if (videoData.description?.contains("vimeo", true)!!){
                playVideo(videoData.description!!)
            }else {
                preparExoPlayer(videoData.description!!)
            }
        } else {
            statefulLayout.showProgress()
            statefulLayout.setProgressText("Loading..")
            getVideoId(id)
        }
    }

    private fun playVideo(id: String) {
        val videoId = id.replace("https://vimeo.com/", "")
        VimeoExtractor.getInstance()
            .fetchVideoWithIdentifier(videoId, null, object : OnVimeoExtractionListener {
                override fun onSuccess(video: VimeoVideo) {
                    val hdStream = video.streams["720p"]
                    println("VIMEO VIDEO STREAM$hdStream")
                    hdStream?.let {
                        runOnUiThread {
                            //code that runs in main
                            preparExoPlayer(it)
                        }
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    Log.d("failure", throwable.message!!)
                }
            })
    }


    private fun onPlayerPlaying() {
        // Set the media item to be played.
        player.setMediaSource(mediaSource!!)
        // Prepare the player.
        player.prepare()
        // Start the playback.
        player.play()
    }

    private fun getVideoId(id: String) {
        networkHelper.getCall(
            qrcode + id,
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


    private fun buildMediaSource(uri: Uri): MediaSource {
        val cacheDataSourceFactory = CacheDataSourceFactory(
            VideoCache.get(this),
            DefaultHttpDataSourceFactory("exoplayer-demo")
        )
        return ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(uri)
    }

    override fun onStop() {
        super.onStop()
        player.stop()
        player.release()
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        statefulLayout.showContent()
        if (responseCode == networkHelper.responseSuccess && tag == "qrcode") {
            val qrResponse = Gson().fromJson(response, GetQRCode::class.java)
            if (qrResponse.data.videoUrl.contains("vimeo", true)){
                playVideo(qrResponse.data.videoUrl)
            }else {
                preparExoPlayer(qrResponse.data.videoUrl)
            }
        } else {
            Toast.makeText(
                this,
                "Unable to view the video... Try again later...",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}