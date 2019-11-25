package org.smonard.iutubplayer.process.main

import android.app.Activity
import android.widget.ArrayAdapter
import org.smonard.iutubplayer.IutubApplication
import org.smonard.iutubplayer.R
import org.smonard.iutubplayer.data.LocalStorage
import org.smonard.iutubplayer.process.support.Video
import org.smonard.iutubplayer.process.support.VideoListAdapter

abstract class VideoListProcessor internal constructor(protected val videoPlayer: VideoPlayer, protected val localStorage: LocalStorage) {
    open var adapter: VideoListAdapter? = null

    protected abstract val contextId: Int

    abstract val videoList: List<Video>

    fun performMenuAction(menuAction: Int, videoPosition: Int): Boolean {
        val video = videoList[videoPosition]
        when (menuAction) {
            R.id.play -> playVideo(video)
            R.id.make_default -> localStorage.saveDefaultVideo(video)
            R.id.remove_from_playlist -> removeVideoFromPlaylist(video)
            R.id.add_to_playlist -> addVideoToPlaylist(video)
        }
        return true
    }

    fun getVideoListAdapter(activity: Activity): ArrayAdapter<*>? { // move it to ctr
        if (this.adapter == null) {
            adapter = VideoListAdapter(activity, videoList)
        }
        return this.adapter
    }

    fun playVideo(video: Video) {
        IutubApplication.currentPlayingContext = contextId
        videoPlayer.play(video)
    }

    protected abstract fun addVideoToPlaylist(video: Video)

    protected abstract fun removeVideoFromPlaylist(video: Video)

}
