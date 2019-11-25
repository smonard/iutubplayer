package org.smonard.iutubplayer.process.main

import android.app.Activity

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.smonard.iutubplayer.IutubApplication
import org.smonard.iutubplayer.R
import org.smonard.iutubplayer.data.LocalStorage
import org.smonard.iutubplayer.data.VideoProvider
import org.smonard.iutubplayer.helpers.RandomStructs
import org.smonard.iutubplayer.process.support.Video
import org.smonard.iutubplayer.process.support.VideoListAdapter
import java.util.concurrent.ExecutionException
import java.util.function.Function

import nonapi.io.github.classgraph.utils.ReflectionUtils

import org.junit.Assert.*
import org.mockito.Mockito.*
import org.smonard.iutubplayer.helpers.ReflectionTestUtils

@RunWith(MockitoJUnitRunner::class)
class RelatedVideosManagerTest {

    private val easyRandom = RandomStructs()

    @InjectMocks
    private lateinit var relatedVideosManager: RelatedVideosManager

    @Mock
    private val videoPlayer: VideoPlayer? = null

    @Mock
    private val localStorage: LocalStorage? = null

    @Mock
    private val playlist: PlaylistProcessor? = null

    @Mock
    private val api: VideoProvider? = null

    private var testVideoList: MutableList<Video> = mutableListOf()

    // ## onVideoStarted

    @Captor
    private val callback: ArgumentCaptor<Function<List<Video>, Void?>>? = null

    @Before
    fun setUp() {
        relatedVideosManager.getVideoListAdapter(Activity())
        testVideoList = relatedVideosManager.videoList
    }

    // ## getList :) just trying

    @Test
    fun retrievesVideoListAttribute() {
        val expectedList = ReflectionUtils.getFieldVal(relatedVideosManager, "relatedVideos", false) as List<Video>
        val actualList = relatedVideosManager.videoList

        assertSame(expectedList, actualList)
    }

    // ## playVideo#getContextId

    @Test
    fun setRightContextWhenPlayingNewVideo() {
        relatedVideosManager.playVideo(easyRandom.nextObject(Video::class.java))

        assertEquals(R.id.related.toLong(), IutubApplication.currentPlayingContext.toLong())
    }

    // ## performMenuAction#addVideoToPlaylistOnMenuAction

    @Test
    fun addsVideoToPlaylistOnMenuAction() {
        val testVideo = easyRandom.nextObject(Video::class.java)
        testVideoList.add(testVideo)

        relatedVideosManager.performMenuAction(R.id.add_to_playlist, 0)

        verify<PlaylistProcessor>(playlist).addVideoToPlaylist(testVideo)
    }

    @Test
    @Throws(ExecutionException::class, InterruptedException::class)
    fun loadsNewRelatedVideoListWhenNewVideoIsPlayed() {
        val newVideos = RandomStructs().getPlaylist(1)
        val testVideo = easyRandom.nextObject(Video::class.java)
        val mockAdapter = mock(VideoListAdapter::class.java)
        relatedVideosManager.adapter = mockAdapter

        relatedVideosManager.onVideoStarted(testVideo)
        verify<VideoProvider>(api).searchRelatedVideos(eq(testVideo.id), callback!!.capture())
        callback.value.apply(newVideos)

        verify(mockAdapter, times(1)).clear()
        verify(mockAdapter, times(1)).addAll(newVideos)
    }

    // ## onVideoFinished

    @Test
    fun playsNextVideoOnVideoFinished() {
        val testVideo = easyRandom.nextObject(Video::class.java)
        testVideoList.add(testVideo)

        relatedVideosManager.onVideoFinished(easyRandom.nextObject(Video::class.java))

        verify<VideoPlayer>(videoPlayer).play(testVideo)
    }

    @Test
    fun doesNotPlayNextVideoOnVideoFinishedWhenContextDiffers() {
        IutubApplication.currentPlayingContext = R.id.playlist

        relatedVideosManager.onVideoFinished(easyRandom.nextObject(Video::class.java))

        verify<VideoPlayer>(videoPlayer, never()).play(any(Video::class.java))
    }
}
