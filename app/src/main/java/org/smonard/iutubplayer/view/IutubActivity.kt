package org.smonard.iutubplayer.view

import android.app.Activity
import android.os.Bundle
import org.smonard.iutubplayer.R


import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import org.smonard.iutubplayer.BuildConfig
import org.smonard.iutubplayer.process.main.VideoPlayer
import org.smonard.iutubplayer.process.support.InstanceHolder
import org.smonard.iutubplayer.view.partial.PlayListFragment
import org.smonard.iutubplayer.view.partial.RelatedListFragment
import org.smonard.iutubplayer.view.partial.VideoSearchFragment

class IutubActivity : Activity(), YouTubePlayer.OnInitializedListener {
    private var videoPlayer: VideoPlayer? = null
    private val videoSearchFragment = VideoSearchFragment()
    private val relatedListFragment = RelatedListFragment()
    private val playListFragment = PlayListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoPlayer = InstanceHolder.getVideosProcessor(InstanceHolder.Processor.PLAYER, VideoPlayer::class.java)
        setContentView(R.layout.activity_iutub)
        val youtubeFragment = fragmentManager.findFragmentById(R.id.youtubeFragment) as YouTubePlayerFragment
        youtubeFragment.initialize(BuildConfig.YAPI_KEY, this)
        val navigationView: BottomNavigationView = findViewById(R.id.menu)
        fragmentManager.beginTransaction().add(R.id.wrapper, playListFragment).commit()
        fragmentManager.beginTransaction().replace(R.id.wrapper, relatedListFragment).commit()
        navigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> fragmentManager.beginTransaction().replace(R.id.wrapper, videoSearchFragment).commit()
                R.id.related -> fragmentManager.beginTransaction().replace(R.id.wrapper, relatedListFragment).commit()
                else -> fragmentManager.beginTransaction().replace(R.id.wrapper, playListFragment).commit()
            }
            true
        }
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, restored: Boolean) {
        (findViewById<View>(R.id.menu) as BottomNavigationView).selectedItemId = R.id.search
        videoPlayer!!.setYoutubePlayer(youTubePlayer)
        if (!restored) {
            videoPlayer!!.playDefault()
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError) {
            youTubeInitializationResult.getErrorDialog(this, 1).show()
        } else {
            val message = String.format("Error on Youtube Init: %s", youTubeInitializationResult.toString())
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}