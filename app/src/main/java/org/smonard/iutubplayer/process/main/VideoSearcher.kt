package org.smonard.iutubplayer.process.main

import org.smonard.iutubplayer.R
import org.smonard.iutubplayer.data.LocalStorage
import org.smonard.iutubplayer.data.VideoProvider
import org.smonard.iutubplayer.process.support.Video
import java.util.*
import java.util.function.Function

class VideoSearcher(videoPlayer: VideoPlayer, private val playlistProcessor: PlaylistProcessor, private val provider: VideoProvider, localStorage: LocalStorage) : VideoListProcessor(videoPlayer, localStorage) {
    private val coincidences = ArrayList<Video>()

    override val contextId: Int
        get() = R.id.related

    override val videoList: ArrayList<Video>
        get() = coincidences

    fun search(query: String) {
        provider.searchVideos(query, callback())
    }

    private fun callback(): Function<List<Video>, Void?> {
        return Function { videos ->
            adapter!!.clear()
            adapter!!.addAll(videos)
            null
        }
    }

    override fun addVideoToPlaylist(video: Video) {
        playlistProcessor.addVideoToPlaylist(video)
    }

    override fun removeVideoFromPlaylist(video: Video) {
        // Nothing really
    }
}
