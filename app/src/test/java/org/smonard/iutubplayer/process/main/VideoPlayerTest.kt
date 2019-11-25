package org.smonard.iutubplayer.process.main

import com.google.android.youtube.player.YouTubePlayer

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.smonard.iutubplayer.data.LocalStorage
import org.smonard.iutubplayer.process.support.Video

import java.util.ArrayList

import org.mockito.Mockito.*
import org.smonard.iutubplayer.helpers.RandomStructs

@RunWith(MockitoJUnitRunner::class)
class VideoPlayerTest {

    private val random = RandomStructs()

    @InjectMocks
    private lateinit var videoPlayer: VideoPlayer

    @Mock
    private val youTubePlayer: YouTubePlayer? = null

    @Mock
    private val localStorage: LocalStorage? = null

    @Before
    fun setUp() {
        videoPlayer.setYoutubePlayer(youTubePlayer!!)
    }

    @Test
    fun playVideoUsingVideoPlayer() {
        val testVideo = random.nextObject(Video::class.java)

        videoPlayer.play(testVideo)

        verify<YouTubePlayer>(youTubePlayer).cueVideo(testVideo.id)
        verify<YouTubePlayer>(youTubePlayer).play()
    }

    @Test
    fun notifiesListenersWhenNewVideoPlayed() {
        val testVideo = random.nextObject(Video::class.java)
        val listeners = ArrayList<VideoPlayer.OnStartVideoListener>()
        for (index in 0..9) {
            val listener = mock(VideoPlayer.OnStartVideoListener::class.java)
            listeners.add(listener)
            videoPlayer.addOnStartVideoListener(listener)
        }

        videoPlayer.play(testVideo)

        for (listener in listeners) {
            verify<VideoPlayer.OnStartVideoListener>(listener).onVideoStarted(testVideo)
        }
    }

    @Test
    fun doesNotNotifiesListenersWhenVideoReplayed() {
        val testVideo = random.nextObject(Video::class.java)
        val listener = mock(VideoPlayer.OnStartVideoListener::class.java)
        videoPlayer.addOnStartVideoListener(listener)

        videoPlayer.play(testVideo)
        videoPlayer.play(testVideo)

        verify<VideoPlayer.OnStartVideoListener>(listener, atMost(1)).onVideoStarted(testVideo)
    }

    @Test
    fun doesNotCueVideoWhenVideoReplayed() {
        val testVideo = random.nextObject(Video::class.java)

        videoPlayer.play(testVideo)
        videoPlayer.play(testVideo)

        verify<YouTubePlayer>(youTubePlayer, times(2)).play()
        verify<YouTubePlayer>(youTubePlayer, atMost(1)).cueVideo(testVideo.id)
    }


    @Test
    fun notifiesListenersOnVideoEnded() {
        val testVideo = random.nextObject(Video::class.java)
        val listeners = ArrayList<VideoPlayer.OnFinishVideoListener>()
        for (index in 0..9) {
            val listener = mock(VideoPlayer.OnFinishVideoListener::class.java)
            listeners.add(listener)
            videoPlayer.addOnFinishVideoListener(listener)
        }

        videoPlayer.play(testVideo)
        videoPlayer.onVideoEnded()

        for (listener in listeners) {
            verify<VideoPlayer.OnFinishVideoListener>(listener).onVideoFinished(testVideo)
        }
    }

    @Test
    fun playsDefaultVideoFromLocalStorage() {
        val testVideo = random.nextObject(Video::class.java)
        `when`(localStorage!!.loadDefaultVideo()).thenReturn(testVideo)

        videoPlayer.playDefault()

        verify<YouTubePlayer>(youTubePlayer).cueVideo(testVideo.id)
        verify<YouTubePlayer>(youTubePlayer).play()
    }

}