package org.smonard.iutubplayer.process.main

import android.app.Activity

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.smonard.iutubplayer.IutubApplication
import org.smonard.iutubplayer.R
import org.smonard.iutubplayer.data.LocalStorage
import org.smonard.iutubplayer.process.support.Video

import java.util.ArrayList

import org.junit.Assert.*
import org.mockito.Mockito.verify
import org.smonard.iutubplayer.helpers.RandomStructs

@RunWith(MockitoJUnitRunner::class)
class VideoListProcessorTest {

    private val random = RandomStructs()

    private lateinit var videoListProcessor: VideoListProcessor

    @Mock
    private val videoPlayer: VideoPlayer? = null

    @Mock
    private val localStorage: LocalStorage? = null

    @Mock
    private val mockProcessor: MockProcessor? = null

    private val videos = ArrayList<Video>()

    @Before
    fun setUp() {
        videoListProcessor = object : VideoListProcessor(videoPlayer!!, localStorage!!) {

            override val contextId: Int
                get() = 5

            override val videoList: List<Video>
                get() = videos

            override fun addVideoToPlaylist(video: Video) {
                mockProcessor!!.addVideoToPlaylist(video)
            }

            override fun removeVideoFromPlaylist(video: Video) {
                mockProcessor!!.removeVideoFromPlaylist(video)
            }
        }
    }

    // ## performMenuAction

    @Test
    fun playsVideoOnMenuAction() {
        val testVideo = random.nextObject(Video::class.java)
        videos.add(testVideo)

        videoListProcessor.performMenuAction(R.id.play, 0)

        verify<VideoPlayer>(videoPlayer).play(testVideo)
        assertEquals(5, IutubApplication.currentPlayingContext.toLong())
    }

    @Test
    fun addsVideoToPlaylistOnMenuAction() {
        val testVideo = random.nextObject(Video::class.java)
        videos.add(testVideo)

        videoListProcessor.performMenuAction(R.id.add_to_playlist, 0)

        verify<MockProcessor>(mockProcessor).addVideoToPlaylist(testVideo)
    }

    @Test
    fun addsVideoFromPlaylistOnMenuAction() {
        val testVideo = random.nextObject(Video::class.java)
        videos.add(testVideo)

        videoListProcessor.performMenuAction(R.id.remove_from_playlist, 0)

        verify<MockProcessor>(mockProcessor).removeVideoFromPlaylist(testVideo)
    }

    @Test
    fun makesDefaultVideoOnMenuAction() {
        val testVideo = random.nextObject(Video::class.java)
        videos.add(testVideo)

        videoListProcessor.performMenuAction(R.id.make_default, 0)

        verify<LocalStorage>(localStorage).saveDefaultVideo(testVideo)
    }

    // ## getVideoListAdapter

    @Test
    fun createsVideoListAdapter() {
        assertNotNull(videoListProcessor.getVideoListAdapter(Activity()))
    }

    // ## playVideo

    @Test
    fun playVideoUsingVideoPlayer() {
        val testVideo = random.nextObject(Video::class.java)

        videoListProcessor.playVideo(testVideo)

        verify<VideoPlayer>(videoPlayer).play(testVideo)
    }

    class MockProcessor {

        fun addVideoToPlaylist(video: Video) {

        }

        fun removeVideoFromPlaylist(video: Video) {

        }
    }
}
