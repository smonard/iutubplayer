package org.smonard.iutubplayer.view.partial

import android.os.Bundle
import android.view.*
import android.widget.Toolbar
import org.smonard.iutubplayer.R
import org.smonard.iutubplayer.process.main.PlaylistProcessor
import org.smonard.iutubplayer.process.main.VideoListProcessor
import org.smonard.iutubplayer.process.support.InstanceHolder
import org.smonard.iutubplayer.process.support.InstanceHolder.getVideosProcessor

class PlayListFragment : VideoListFragment() {
    private val playlistProcessor: PlaylistProcessor = getVideosProcessor(
        InstanceHolder.Processor.PLAYLIST,
        PlaylistProcessor::class.java
    )!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        playlistProcessor.notifyPlayingOption()
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        listView.onItemClickListener = this
        listAdapter = playlistProcessor.getVideoListAdapter(activity)
        registerForContextMenu(listView)
        val myToolbar =
            activity.findViewById<Toolbar>(R.id.toolbar)
        myToolbar.title = String()
        activity.setActionBar(myToolbar)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        menuInflater: MenuInflater
    ) {
        menuInflater.inflate(R.menu.playback_option_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        playlistProcessor.switchPlayingOption(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    override val contextMenuId: Int
        get() = R.menu.playlist_video_menu

    override val videoListProcessor: VideoListProcessor
        get() = playlistProcessor

}