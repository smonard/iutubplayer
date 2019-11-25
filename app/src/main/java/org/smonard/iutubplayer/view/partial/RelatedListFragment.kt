package org.smonard.iutubplayer.view.partial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.smonard.iutubplayer.R
import org.smonard.iutubplayer.process.main.RelatedVideosManager
import org.smonard.iutubplayer.process.main.VideoListProcessor
import org.smonard.iutubplayer.process.support.InstanceHolder
import org.smonard.iutubplayer.process.support.InstanceHolder.getVideosProcessor

class RelatedListFragment : VideoListFragment() {
    private val relatedVideosManager: RelatedVideosManager? = getVideosProcessor(
        InstanceHolder.Processor.RELATED,
        RelatedVideosManager::class.java
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_related_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        listView.onItemClickListener = this
        listAdapter = relatedVideosManager!!.getVideoListAdapter(activity)
        registerForContextMenu(listView)
        super.onActivityCreated(savedInstanceState)
    }

    override val videoListProcessor: VideoListProcessor
        get() = relatedVideosManager!!

    override val contextMenuId: Int
        get() = R.menu.video_menu

}