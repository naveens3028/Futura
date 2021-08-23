package com.trisys.rn.baseapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Pair
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getString
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.MediaItem.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.drm.FrameworkMediaDrm
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory.AdsLoaderProvider
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.*
import com.google.gson.Gson
import com.trisys.rn.baseapp.MyApplication
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.helper.exoplayer.DemoUtil
import com.trisys.rn.baseapp.helper.exoplayer.DownloadTracker
import com.trisys.rn.baseapp.helper.exoplayer.IntentUtil
import com.trisys.rn.baseapp.learn.fragment.VideoFragment
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
import java.util.*


class VideoPlayActivity : AppCompatActivity(), StyledPlayerControlView.VisibilityListener {
    // Saved instance state keys.
    private val KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters"
    private val KEY_WINDOW = "window"
    private val KEY_POSITION = "position"
    private val KEY_AUTO_PLAY = "auto_play"

    protected var player: SimpleExoPlayer? = null

    private val isShowingTrackSelectionDialog = false
    private val selectTracksButton: Button? = null
    private lateinit var httpDataSourceFactory: HttpDataSource.Factory
    private var dataSourceFactory: DataSource.Factory? = null
    private var mediaItems: List<MediaItem>? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var trackSelectorParameters: DefaultTrackSelector.Parameters? = null
//    private var debugViewHelper: DebugTextViewHelper? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private var startAutoPlay = false
    private var startWindow = 0
    private var startPosition: Long = 0
    private var downloadTracker: DownloadTracker? = null
    private var preferExtensionDecodersMenuItem: MenuItem? = null
    private var useExtensionRenderers = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
        dataSourceFactory = DemoUtil.getDataSourceFactory( /* context= */this)
        setContentView(R.layout.activity_video_play)

        downloadTracker = DemoUtil.getDownloadTracker( /* context= */this)
        playerView.setControllerVisibilityListener(this)
        //playerView.setErrorMessageProvider(PlayerErrorMessageProvider())
        playerView.requestFocus()

        if (savedInstanceState != null) {
            trackSelectorParameters =
                savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS)
            startAutoPlay =
                savedInstanceState.getBoolean(KEY_AUTO_PLAY)
            startWindow =
                savedInstanceState.getInt(KEY_WINDOW)
            startPosition =
                savedInstanceState.getLong(KEY_POSITION)
        } else {
            val builder = DefaultTrackSelector.ParametersBuilder( /* context= */this)
            trackSelectorParameters = builder.build()
            clearStartPosition()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        releasePlayer()
        clearStartPosition()
        setIntent(intent)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {

                initializePlayer()
                if (playerView != null) {
                    playerView.onResume()
                }

            mediaItems = createMediaItems(intent)
            if(!downloadTracker!!.isDownloaded(mediaItems!![0])) {
                val playlist = PlaylistHolder(mediaItems!![0].mediaId, mediaItems!!)
                onSampleDownloadButtonClicked(playlist)
            }else{
                Toast.makeText(applicationContext,"already downloaded", Toast.LENGTH_LONG).show()

            }
        }

        btnDownload.setOnClickListener {
            if(!downloadTracker!!.isDownloaded(mediaItems!![0])) {
                val playlist = PlaylistHolder(mediaItems!![0].mediaId, mediaItems!!)
                onSampleDownloadButtonClicked(playlist)
            }else{
                Toast.makeText(applicationContext,"already downloaded", Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
            if (playerView != null) {
                playerView.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause()
            }
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause()
            }
            releasePlayer()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }
    private fun updateStartPosition() {
        if (player != null) {
            startAutoPlay = player!!.playWhenReady
            startWindow = player!!.currentWindowIndex
            startPosition = Math.max(0, player!!.contentPosition)
        }
    }
    protected fun clearStartPosition() {
        startAutoPlay = true
        startWindow = C.INDEX_UNSET
        startPosition = C.TIME_UNSET
    }
    protected fun releasePlayer() {
        if (player != null) {
//            updateTrackSelectorParameters()
            updateStartPosition()
//            debugViewHelper!!.stop()
//            debugViewHelper = null
            player!!.release()
            player = null
            mediaItems = emptyList()
            trackSelector = null
        }
    }

    /** @return Whether initialization was successful.
     */
    protected fun initializePlayer(): Boolean {

        if (player == null) {
            val intent = intent
            mediaItems = createMediaItems(intent)
            if (mediaItems!!.isEmpty()) {
                return false
            }
        }
        Handler(mainLooper).postDelayed({
        if (player == null) {
            val intent = intent
            mediaItems = createMediaItems(intent)

            val preferExtensionDecoders =
                intent.getBooleanExtra(IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA, false)
            val renderersFactory =
                DemoUtil.buildRenderersFactory( /* context= */this, preferExtensionDecoders)
            val mediaSourceFactory: MediaSourceFactory =
                DefaultMediaSourceFactory(dataSourceFactory!!)
                    .setAdViewProvider(playerView)
            trackSelector = DefaultTrackSelector( /* context= */this)
            trackSelector!!.parameters = trackSelectorParameters!!
            lastSeenTrackGroupArray = null
            player = SimpleExoPlayer.Builder( /* context= */this, renderersFactory)
                .setMediaSourceFactory(mediaSourceFactory)
                .setTrackSelector(trackSelector!!)
                .build()
            player!!.setAudioAttributes(AudioAttributes.DEFAULT,  /* handleAudioFocus= */true)
            player!!.playWhenReady = startAutoPlay
            playerView.player = player
           // debugViewHelper = DebugTextViewHelper(player!!, debugTextView!!)
//            debugViewHelper!!.start()
        }

            val haveStartPosition = startWindow != C.INDEX_UNSET
            if (haveStartPosition) {
                player!!.seekTo(startWindow, startPosition)
            }
            player!!.setMediaItems(mediaItems!!,  /* resetPosition= */!haveStartPosition)
            player!!.prepare()
        }, 10000)
        return true
    }

    private fun createMediaItems(intent: Intent): List<MediaItem>? {
        val action = intent.action
        val actionIsListView = IntentUtil.ACTION_VIEW_LIST.equals(action)
        if (!actionIsListView && !IntentUtil.ACTION_VIEW.equals(action)) {
            showToast(getString(R.string.unexpected_intent_action, action))
            finish()
            return emptyList()
        }
        val mediaItems: List<MediaItem> =
            createMediaItems(
                intent,
                DemoUtil.getDownloadTracker( /* context= */this)
            )!!
        for (i in mediaItems.indices) {
            val mediaItem = mediaItems[i]
            if (!Util.checkCleartextTrafficPermitted(mediaItem)) {
                showToast(resources.getString(R.string.error_cleartext_not_permitted))
                finish()
                return emptyList()
            }
            if (Util.maybeRequestReadExternalStoragePermission( /* activity= */this, mediaItem)) {
                // The player will be reinitialized if the permission is granted.
                return emptyList()
            }
            val drmConfiguration =
                Assertions.checkNotNull(mediaItem.playbackProperties).drmConfiguration
            if (drmConfiguration != null) {
                if (Util.SDK_INT < 18) {
                    showToast(resources.getString(R.string.error_drm_unsupported_before_api_18))
                    finish()
                    return emptyList()
                } else if (!FrameworkMediaDrm.isCryptoSchemeSupported(drmConfiguration.uuid)) {
                    showToast(resources.getString(R.string.error_drm_unsupported_scheme))
                    finish()
                    return emptyList()
                }
            }
        }

        return mediaItems
    }
    private fun createMediaItems(
        intent: Intent,
        downloadTracker: DownloadTracker
    ): List<MediaItem>? {
        val mediaItems: MutableList<MediaItem> = ArrayList()
        for (item in IntentUtil.createMediaItemsFromIntent(intent)) {
            val downloadRequest =
                downloadTracker.getDownloadRequest(Assertions.checkNotNull(item.playbackProperties).uri)
            if (downloadRequest != null) {
                val builder = item.buildUpon()
                builder
                    .setMediaId(downloadRequest.id)
                    .setUri(downloadRequest.uri)
                    .setCustomCacheKey(downloadRequest.customCacheKey)
                    .setMimeType(downloadRequest.mimeType)
                    .setStreamKeys(downloadRequest.streamKeys)
                    .setDrmKeySetId(downloadRequest.keySetId)
                    .setDrmLicenseRequestHeaders(getDrmRequestHeaders(
                            item
                        )
                    )
                mediaItems.add(builder.build())
            } else {
                mediaItems.add(item)
            }
        }
        return mediaItems
    }
    private fun getDrmRequestHeaders(item: MediaItem): MutableMap<String, String>? {
        val drmConfiguration = item.playbackProperties!!.drmConfiguration
        return drmConfiguration?.requestHeaders
    }
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
    override fun onVisibilityChange(visibility: Int) {
        if(visibility == 0){
            btnDownload.visibility = View.VISIBLE
        }else{
            btnDownload.visibility = View.GONE
        }
    }

    private fun onSampleDownloadButtonClicked(playlistHolder: PlaylistHolder) {
        val downloadUnsupportedStringId: Int = getDownloadUnsupportedStringId(playlistHolder)
        if (downloadUnsupportedStringId != 0) {
            Toast.makeText(applicationContext, downloadUnsupportedStringId, Toast.LENGTH_LONG)
                .show()
        } else {
            val renderersFactory: RenderersFactory = DemoUtil.buildRenderersFactory( /* context= */
                this, isNonNullAndChecked(
                    preferExtensionDecodersMenuItem
                )
            )
            downloadTracker!!.toggleDownload(
                supportFragmentManager, playlistHolder.mediaItems.get(0), renderersFactory
            )
        }
    }
    private fun getDownloadUnsupportedStringId(playlistHolder: PlaylistHolder): Int {
        if (playlistHolder.mediaItems.size > 1) {
            return R.string.download_playlist_unsupported
        }
        val playbackProperties =
            Assertions.checkNotNull(playlistHolder.mediaItems[0].playbackProperties)
        if (playbackProperties.adsConfiguration != null) {
            return R.string.download_ads_unsupported
        }
        val scheme = playbackProperties.uri.scheme
        return if (!("http" == scheme || "https" == scheme)) {
            R.string.download_scheme_unsupported
        } else 0
    }
    private fun isNonNullAndChecked(menuItem: MenuItem?): Boolean {
        // Temporary workaround for layouts that do not inflate the options menu.
        return menuItem != null && menuItem.isChecked
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

    private class PlaylistGroup(val title: String) {
        val playlists: List<PlaylistHolder>

        init {
            playlists = ArrayList()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_video_downloader, menu)
        preferExtensionDecodersMenuItem = menu.findItem(R.id.prefer_extension_decoders)
        preferExtensionDecodersMenuItem!!.isVisible = useExtensionRenderers
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = !item.isChecked
        return true
    }
}