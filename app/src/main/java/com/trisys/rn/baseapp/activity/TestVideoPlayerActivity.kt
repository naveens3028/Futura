package com.trisys.rn.baseapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.utils.VideoCache
import kotlinx.android.synthetic.main.activity_test_video.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class TestVideoPlayerActivity : AppCompatActivity() {

    private var url: String? = null
    private var videoId: String? = null
    lateinit var player: SimpleExoPlayer
    private var mediaSource: MediaSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_video)

        url = intent.getStringExtra("url")
        videoId = intent.getStringExtra("videoId")

        if (!videoId.isNullOrEmpty()){

        }
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

        preparExoPlayer(url!!)

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

}