package com.trisys.rn.baseapp.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.VideoMaterial
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_video.*
import vimeoextractor.OnVimeoExtractionListener
import vimeoextractor.VimeoExtractor
import vimeoextractor.VimeoVideo

class VideoPlayActivity : AppCompatActivity() {
    lateinit var player: SimpleExoPlayer
    lateinit var myPreferences: MyPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        myPreferences = MyPreferences(this)
    }

    override fun onStart() {
        super.onStart()

        player = SimpleExoPlayer.Builder(this).build()
        player.setThrowsWhenUsingWrongThread(false)
        player_view.setPlayer(player)

        val videoData = Gson().fromJson(myPreferences.getString(Define.VIDEO_DATA), VideoMaterial::class.java)

        val id = videoData.description.replace("https://vimeo.com/","")

        VimeoExtractor.getInstance()
            .fetchVideoWithIdentifier(id, null, object : OnVimeoExtractionListener {
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
                    Log.d("failure",throwable.message!!)
                }
            })
    }
    fun preparExoPlayer(url: String){
        // Build the media item.
        val mediaItem: MediaItem = MediaItem.fromUri(url)
        // Set the media item to be played.
        player.setMediaItem(mediaItem)
        // Prepare the player.
        player.prepare()
        // Start the playback.
        player.play()
    }
    override fun onStop() {
        super.onStop()
        player.stop()
        player.release()
    }
}