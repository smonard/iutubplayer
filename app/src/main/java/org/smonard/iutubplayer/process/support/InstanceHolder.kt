package org.smonard.iutubplayer.process.support

import org.smonard.iutubplayer.data.LocalStorage
import org.smonard.iutubplayer.data.VideoProvider
import org.smonard.iutubplayer.process.main.PlaylistProcessor
import org.smonard.iutubplayer.process.main.RelatedVideosManager
import org.smonard.iutubplayer.process.main.VideoPlayer
import org.smonard.iutubplayer.process.main.VideoSearcher

object InstanceHolder {
    private val playlistProcessor: PlaylistProcessor
    private val videoPlayer: VideoPlayer = VideoPlayer(LocalStorage())
    private val relatedVideosManager: RelatedVideosManager
    private val videoSearcher: VideoSearcher

    init {
        playlistProcessor = PlaylistProcessor(videoPlayer, LocalStorage())
        relatedVideosManager = RelatedVideosManager(videoPlayer, playlistProcessor, VideoProvider(), LocalStorage())
        videoSearcher = VideoSearcher(videoPlayer, playlistProcessor, VideoProvider(), LocalStorage())
    }

    fun <E> getVideosProcessor(processorType: Processor?, klass: Class<E>): E? {
        return when (processorType) {
            Processor.PLAYLIST -> klass.cast(playlistProcessor)
            Processor.RELATED -> klass.cast(relatedVideosManager)
            Processor.SEARCH -> klass.cast(videoSearcher)
            Processor.PLAYER -> klass.cast(videoPlayer)
            else -> null
        }
    }

    enum class Processor {
        PLAYLIST, RELATED, SEARCH, PLAYER
    }
}