package com.trisys.rn.baseapp.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.gson.Gson
import com.trisys.rn.baseapp.MyApplication
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
import vimeoextractor.OnVimeoExtractionListener
import vimeoextractor.VimeoExtractor
import vimeoextractor.VimeoVideo


class VideoPlayActivity : AppCompatActivity(), OnNetworkResponse {
    lateinit var player: SimpleExoPlayer
    lateinit var myPreferences: MyPreferences
    lateinit var networkHelper: NetworkHelper
     private var mediaSource :MediaSource? =null


    private lateinit var httpDataSourceFactory: HttpDataSource.Factory
    private lateinit var defaultDataSourceFactory: DefaultDataSourceFactory
    private lateinit var cacheDataSourceFactory: DataSource.Factory
    private val simpleCache: SimpleCache = MyApplication.simpleCache

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        myPreferences = MyPreferences(this)
        networkHelper = NetworkHelper(this)
    }

    override fun onStart() {
        super.onStart()

//        player = SimpleExoPlayer.Builder(this).build()
//        player.setThrowsWhenUsingWrongThread(false)
//        player_view.player = player

        httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)

        defaultDataSourceFactory = DefaultDataSourceFactory(
            this, httpDataSourceFactory
        )

        cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        player = SimpleExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory)).build()


        var id = intent.getStringExtra("videoId")
        if (id.isNullOrEmpty()) {
            val videoData = Gson().fromJson(
                myPreferences.getString(Define.VIDEO_DATA),
                VideoMaterial::class.java
            )
            if(videoData.description.contains("vimeo.com")) {
                playVideo(videoData.description)
            }else{
                preparExoPlayer(id!!)
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

    /*   override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
           when (playbackState) {
               Player.STATE_READY -> {
                   Log.d("ZAQ", "STATE_READY")
                   if (player?.playWhenReady == true) onPlayerPlaying()
               }
               //Player.STATE_BUFFERING -> Log.d("ZAQ", "STATE_BUFFERING")
               //Player.STATE_ENDED -> Log.d("ZAQ", "STATE_ENDED")
               //Player.STATE_IDLE -> Log.d("ZAQ", "STATE_IDLE")
           }
       }*/

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
//        mediaSource = buildMediaSource(url)
//        // Set the media item to be played.
//        player.setMediaSource(mediaSource!!)
//        val mediaItem: MediaItem = MediaItem.fromUri(url)
//        // Set the media item to be played.
//        player.setMediaItem(mediaItem)
//        // Prepare the player.
//        player.prepare()
//
//
//        // Start the playback.
//        player.play()




//        val url1 = "https://eduinstitute-videos.s3.ap-south-1.amazonaws.com/VID-20190430-WA0010.mp4"

        val videoUri = Uri.parse(url)
        val mediaItem = MediaItem.fromUri(videoUri)
        val mediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)

        playerView.player = player
        player.playWhenReady = true
        player.seekTo(0, 0)
        player.repeatMode = Player.REPEAT_MODE_OFF
        player.setMediaSource(mediaSource, true)
        player.prepare()
    }


    private fun buildMediaSource(url: String): MediaSource {
        val cacheDataSourceFactory = CacheDataSourceFactory(
            VideoCache.get(this),
            DefaultHttpDataSourceFactory("exoplayer-demo")
        )
        return ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(MediaItem.fromUri(url))
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
            playVideo(qrResponse.data.videoUrl)
        } else {
            Toast.makeText(
                this,
                "Unable to view the video... Try again later...",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}