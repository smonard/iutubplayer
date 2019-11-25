package org.smonard.iutubplayer.view.partial

import android.app.ListFragment
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.view.View.OnCreateContextMenuListener
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.AdapterView.OnItemClickListener
import org.smonard.iutubplayer.process.main.VideoListProcessor

abstract class VideoListFragment : ListFragment(), OnCreateContextMenuListener, OnItemClickListener {

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        val selectedVideo =
            videoListProcessor.videoList[position]
        videoListProcessor.playVideo(selectedVideo)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = activity.menuInflater
        inflater.inflate(contextMenuId, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterContextMenuInfo
        return videoListProcessor.performMenuAction(item.itemId, info.position)
    }

    protected abstract val videoListProcessor: VideoListProcessor
    protected abstract val contextMenuId: Int
}