package org.smonard.iutubplayer.process.main

import org.smonard.iutubplayer.IutubApplication
import org.smonard.iutubplayer.R
import org.smonard.iutubplayer.data.LocalStorage
import org.smonard.iutubplayer.data.VideoProvider
import org.smonard.iutubplayer.process.support.Video
import java.util.*
import java.util.function.Function

class RelatedVideosManager(videoPlayer: VideoPlayer, private val playlistProcessor: PlaylistProcessor, private val provider: VideoProvider, localStorage: LocalStorage) : VideoListProcessor(videoPlayer, localStorage),
    VideoPlayer.OnStartVideoListener, VideoPlayer.OnFinishVideoListener {

    private val relatedVideos = ArrayList<Video>()

    override val contextId: Int
        get() = R.id.related

    override val videoList: ArrayList<Video>
        get() = relatedVideos

    init {
        this.videoPlayer.addOnStartVideoListener(this)
        this.videoPlayer.addOnFinishVideoListener(this)
    }

    override fun addVideoToPlaylist(video: Video) {
        playlistProcessor.addVideoToPlaylist(video)
    }

    override fun removeVideoFromPlaylist(video: Video) {
        // Nothing really
    }

    override fun onVideoStarted(video: Video) {
        provider.searchRelatedVideos(video.id, callback())
    }

    private fun callback(): Function<List<Video>, Void?> {
        return Function { videos ->
            adapter!!.clear()
            adapter!!.addAll(videos)
            null
        }
    }

    override fun onVideoFinished(video: Video) {
        playNextVideo()
    }

    private fun playNextVideo() {
        if (IutubApplication.currentPlayingContext == R.id.related && !relatedVideos.isEmpty()) {
            playVideo(relatedVideos[0])
        }
    }
}
