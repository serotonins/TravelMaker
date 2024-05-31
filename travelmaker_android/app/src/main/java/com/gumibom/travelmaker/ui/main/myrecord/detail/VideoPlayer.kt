package com.gumibom.travelmaker.ui.main.myrecord.detail

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.DefaultLivePlaybackSpeedControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

private const val TAG = "VideoPlayer_싸피"
class VideoPlayer(private val context : Context) {

    private var player : SimpleExoPlayer? = null

    fun initPlayer(playerView: PlayerView) {
        player = SimpleExoPlayer.Builder(context, DefaultRenderersFactory(context))
            .setTrackSelector(DefaultTrackSelector(context))
            .build()

        playerView.player = player
    }

    fun prepareAndPlay(videoUri : Uri) {
        val mediaItem = buildMediaItem(videoUri)
        val userAgent = Util.getUserAgent(context, context.applicationInfo.name)
        val factory = DefaultDataSourceFactory(context, userAgent)
        val progressiveMediaSource = ProgressiveMediaSource.Factory(factory).createMediaSource(mediaItem)
        Log.d(TAG, "prepareAndPlay: $progressiveMediaSource")

        player?.setMediaSource(progressiveMediaSource)
        player?.prepare()
        player?.playWhenReady = true
        player?.play()
    }

    fun releasePlayer() {
        player?.release()
        player = null
    }

    private fun buildMediaItem(uri: Uri): MediaItem {
        return MediaItem.fromUri(uri)
    }
}