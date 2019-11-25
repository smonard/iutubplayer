package org.smonard.iutubplayer.process.main

import org.smonard.iutubplayer.IutubApplication
import org.smonard.iutubplayer.R
import org.smonard.iutubplayer.data.LocalStorage
import org.smonard.iutubplayer.process.support.Video
import org.smonard.iutubplayer.util.MessageBoxHelper
import java.util.*

class PlaylistProcessor(videoPlayer: VideoPlayer, localStorage: LocalStorage) : VideoListProcessor(videoPlayer, localStorage), VideoPlayer.OnFinishVideoListener {

    private val playlist: MutableList<Video>

    private var playingOption = PlayingOptions.CONTINUOUS

    override val videoList: List<Video>
        get() = playlist

    override val contextId: Int
        get() = R.id.playlist

    init {
        this.playlist = this.localStorage.loadPlaylist()
        this.videoPlayer.addOnFinishVideoListener(this)
    }

    override fun removeVideoFromPlaylist(video: Video) {
        playlist.remove(video)
        this.adapter!!.remove(video) //otherwise list-view does not get notified
        localStorage.savePlaylist(playlist)
    }

    public override fun addVideoToPlaylist(video: Video) {
        playlist.add(video)
        localStorage.savePlaylist(playlist)
    }

    override fun onVideoFinished(video: Video) {
        playNextVideo(video)
    }

    private fun playNextVideo(video: Video) {
        if (IutubApplication.currentPlayingContext == R.id.playlist) {
            playVideo(getNextVideo(video)!!)
        }
    }

    private fun getNextVideo(video: Video): Video? {
        return when (playingOption) {
            PlayingOptions.CONTINUOUS -> {
                val index = if (playlist.indexOf(video) == playlist.size - 1) 0 else playlist.indexOf(video) + 1
                playlist[index]
            }
            PlayingOptions.SHUFFLE -> playlist[Random().nextInt(playlist.size)]
            PlayingOptions.REPEATED -> video
        }
    }

    fun switchPlayingOption(id: Int) {
        when (id) {
            R.id.replay1 -> playingOption = if (playingOption == PlayingOptions.REPEATED) PlayingOptions.CONTINUOUS else PlayingOptions.REPEATED
            R.id.shuffle -> playingOption = if (playingOption == PlayingOptions.SHUFFLE) PlayingOptions.CONTINUOUS else PlayingOptions.SHUFFLE
        }
        notifyPlayingOption()
    }

    fun notifyPlayingOption() {
        when (playingOption) {
            PlayingOptions.REPEATED -> MessageBoxHelper.show("Playing on repeat")
            PlayingOptions.SHUFFLE -> MessageBoxHelper.show("Playing randomly")
            PlayingOptions.CONTINUOUS -> MessageBoxHelper.show("Playing normally")
        }
    }

    enum class PlayingOptions {
        SHUFFLE,
        CONTINUOUS,
        REPEATED
    }
}
