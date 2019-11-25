package org.smonard.iutubplayer.process.main

import android.app.Activity
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.smonard.iutubplayer.IutubApplication
import org.smonard.iutubplayer.R
import org.smonard.iutubplayer.data.LocalStorage
import org.smonard.iutubplayer.helpers.RandomStructs
import org.smonard.iutubplayer.process.support.Video
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class PlaylistProcessorTest {

    private val random = RandomStructs()

    @InjectMocks
    private lateinit var playlistProcessor: PlaylistProcessor

    @Mock
    private val videoPlayer: VideoPlayer? = null

    @Mock
    private val localStorage: LocalStorage? = null

    private val testPlaylist = ArrayList<Video>()

    @Before
    fun setUp() {
        `when`(localStorage!!.loadPlaylist()).thenReturn(testPlaylist)
        playlistProcessor = PlaylistProcessor(videoPlayer!!, localStorage)
        playlistProcessor.getVideoListAdapter(Activity())
    }

    // ### performMenuAction#removeVideoFromPlaylist

    @Test
    fun removesFromPlaylistOnMenuAction() {
        val testVideo = random.nextObject(Video::class.java)
        testPlaylist.add(testVideo)

        playlistProcessor.performMenuAction(R.id.remove_from_playlist, 0)

        assertTrue(testPlaylist.isEmpty())
        verify<LocalStorage>(localStorage).savePlaylist(testPlaylist)
    }

    // ## playVideo#getcontextId

    @Test
    fun setPlaylistAsPlayingContext() {
        playlistProcessor.playVideo(random.nextObject(Video::class.java))


        assertThat(IutubApplication.currentPlayingContext, `is`(R.id.playlist))
    }

    // ## addVideoToPlaylist

    @Test
    fun addsVideoToPlaylist() {
        val testVideo = random.nextObject(Video::class.java)

        playlistProcessor.addVideoToPlaylist(testVideo)

        assertThat(playlistProcessor.videoList.size, `is`(1))
        assertThat(playlistProcessor.videoList[0], `is`(testVideo))
        verify<LocalStorage>(localStorage).savePlaylist(testPlaylist)
    }

    @Test
    fun savesPlaylist() {
        playlistProcessor.addVideoToPlaylist(random.nextObject(Video::class.java))

        verify<LocalStorage>(localStorage).savePlaylist(testPlaylist)
    }

    // ## onVideoFinished

    @Test
    fun playsNextVideoOnTheList() {
        testPlaylist.addAll(random.getPlaylist(5))
        IutubApplication.currentPlayingContext = R.id.playlist

        playlistProcessor.onVideoFinished(testPlaylist[2])

        verify<VideoPlayer>(videoPlayer).play(testPlaylist[3])
    }

    @Test
    fun playsNextRandomVideoOnTheList() {
        testPlaylist.addAll(random.getPlaylist(4))
        IutubApplication.currentPlayingContext = R.id.playlist
        playlistProcessor.switchPlayingOption(R.id.shuffle)

        for (i in 0..15)
            playlistProcessor.onVideoFinished(testPlaylist[1])

        for (i in 0..3)
            verify<VideoPlayer>(videoPlayer, atLeastOnce()).play(testPlaylist[i])
    }

    @Test
    fun playsSameVideoOnTheList() {
        testPlaylist.addAll(random.getPlaylist(5))
        IutubApplication.currentPlayingContext = R.id.playlist
        playlistProcessor.switchPlayingOption(R.id.replay1)

        playlistProcessor.onVideoFinished(testPlaylist[1])

        verify<VideoPlayer>(videoPlayer).play(testPlaylist[1])
    }

    @Test
    fun doesNotPlayAnyVideoOnTheListWhenDifferentContext() {
        testPlaylist.addAll(random.getPlaylist(5))
        IutubApplication.currentPlayingContext = R.id.related

        playlistProcessor.onVideoFinished(testPlaylist[1])

        verify<VideoPlayer>(videoPlayer, never()).play(ArgumentMatchers.any<Video>())
    }
}
