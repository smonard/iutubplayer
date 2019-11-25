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

@RunWith(MockitoJUnitRunner::class)
class VideoSearcherTest {

    private val structs = RandomStructs()

    @InjectMocks
    private lateinit var videoSearcher: VideoSearcher

    @Mock
    private val videoPlayer: VideoPlayer? = null

    @Mock
    private val playlist: PlaylistProcessor? = null

    @Mock
    private val api: VideoProvider? = null

    @Mock
    private val localStorage: LocalStorage? = null

    private var testVideoList: MutableList<Video>? = null

    // ## search

    @Captor
    private val callback: ArgumentCaptor<Function<List<Video>, Void?>>? = null

    @Before
    fun setUp() {
        testVideoList = videoSearcher.videoList
        videoSearcher.getVideoListAdapter(Activity())
    }

    // ## getList :) just trying

    @Test
    fun retrievesVideoListAttribute() {
        val expectedList = ReflectionUtils.getFieldVal(videoSearcher, "coincidences", false) as List<Video>
        val actualList = videoSearcher.videoList

        assertSame(expectedList, actualList)
    }

    // ## playVideo#getContextId

    @Test
    fun setsDefaultContextWhenPlaysVideo() {
        videoSearcher.playVideo(structs.nextObject(Video::class.java))

        assertEquals(R.id.related.toLong(), IutubApplication.currentPlayingContext.toLong())
    }

    // ## performMenuAction#addsVideoToPlayListManager

    @Test
    fun addsVideoToPlayListManager() {
        val testVideo = structs.nextObject(Video::class.java)
        testVideoList!!.add(testVideo)

        videoSearcher.performMenuAction(R.id.add_to_playlist, 0)

        verify<PlaylistProcessor>(playlist).addVideoToPlaylist(testVideo)
    }

    @Test
    @Throws(ExecutionException::class, InterruptedException::class)
    fun searchesUsingApiObject() {
        val newVideos = RandomStructs().getPlaylist(1)
        val query = structs.nextObject(String::class.java)
        val mockAdapter = mock(VideoListAdapter::class.java)
        videoSearcher.adapter = mockAdapter

        videoSearcher.search(query)
        verify<VideoProvider>(api).searchVideos(eq(query), callback!!.capture())
        callback.value.apply(newVideos)

        verify(mockAdapter, times(1)).clear()
        verify(mockAdapter, times(1)).addAll(newVideos)
    }
}