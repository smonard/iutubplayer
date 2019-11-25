package org.smonard.iutubplayer.process.main

import com.google.android.youtube.player.YouTubePlayer
import org.smonard.iutubplayer.data.LocalStorage
import org.smonard.iutubplayer.process.support.Video

import java.util.ArrayList
import java.util.function.Consumer

class VideoPlayer(val localStorage: LocalStorage) : YouTubePlayer.PlayerStateChangeListener {
    private var player: YouTubePlayer? = null
    private var currentVideo: Video = Video()
    private val onFinishVideoListeners = ArrayList<OnFinishVideoListener>()
    private val onStartVideoListeners = ArrayList<OnStartVideoListener>()

    fun setYoutubePlayer(player: YouTubePlayer) {
        this.player = player
        this.player!!.setPlayerStateChangeListener(this)
    }

    fun play(video: Video?) {
        if (video !== currentVideo) {
            player!!.cueVideo(video!!.id)
            onStartVideoListeners.forEach(Consumer { listener -> listener.onVideoStarted(video) })
        }
        player!!.play()
        this.currentVideo = video
    }

    override fun onLoading() {

    }

    override fun onLoaded(s: String) {

    }

    override fun onAdStarted() {

    }

    override fun onVideoStarted() {

    }

    override fun onVideoEnded() {
        onFinishVideoListeners.forEach(Consumer { listener -> listener.onVideoFinished(currentVideo) })
    }

    override fun onError(errorReason: YouTubePlayer.ErrorReason) {

    }

    fun addOnStartVideoListener(listener: OnStartVideoListener) {
        if (!this.onStartVideoListeners.contains(listener))
            this.onStartVideoListeners.add(listener)
    }

    fun addOnFinishVideoListener(listener: OnFinishVideoListener) {
        if (!this.onFinishVideoListeners.contains(listener))
            this.onFinishVideoListeners.add(listener)
    }

    fun playDefault() {
        val defaultVideo = localStorage.loadDefaultVideo()
        play(defaultVideo)
    }

    interface OnFinishVideoListener {
        fun onVideoFinished(video: Video)
    }

    interface OnStartVideoListener {
        fun onVideoStarted(video: Video)
    }
}
