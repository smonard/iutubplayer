package org.smonard.iutubplayer.data

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.smonard.iutubplayer.data.youtube.VideoResultStruct
import org.smonard.iutubplayer.data.youtube.VideoSearch
import org.smonard.iutubplayer.helpers.RandomStructs
import org.smonard.iutubplayer.helpers.ReflectionTestUtils
import org.smonard.iutubplayer.process.support.Video

import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import java.util.function.Function

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.smonard.iutubplayer.helpers.TestUtils
import retrofit2.Response
import retrofit2.Response.success

@RunWith(MockitoJUnitRunner::class)
class VideoProviderTest {

    @InjectMocks
    private lateinit var videoProvider: VideoProvider

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private val videoSearch: VideoSearch? = null

    private val randomStructs = RandomStructs()

    @Before
    fun setUp() {
        ReflectionTestUtils.setFieldValue(videoProvider, "service", videoSearch)
    }

    @Test
    @Throws(InterruptedException::class, IOException::class)
    fun searchesForRawRelatedVideosAndParsesThemToVideoStructure() {
        val signal = CountDownLatch(1)
        val query = randomStructs.randomString()
        val videoStruct = randomStructs.nextObject(VideoResultStruct::class.java)
        `when`<Response<VideoResultStruct>>(videoSearch!!.getVideoRelates(TestUtils.eq(query), anyString()).execute()).thenReturn(success(videoStruct))
        videoProvider.searchRelatedVideos(query, Function<List<Video>, Void?> { videos ->
            testIfItParsesVideoListResponse(videoStruct, videos)
            signal.countDown()
            null
        })

        assertTrue("Callback never called", signal.await(2, TimeUnit.SECONDS))
    }

    @Test
    @Throws(InterruptedException::class, IOException::class)
    fun searchesForRawVideosAndParsesThemToVideoStructure() {
        val signal = CountDownLatch(1)
        val query = randomStructs.randomString()
        val videoStruct = randomStructs.nextObject(VideoResultStruct::class.java)
        `when`<Response<VideoResultStruct>>(videoSearch!!.getVideoMatches(TestUtils.eq(query), anyString()).execute()).thenReturn(success(videoStruct))
        videoProvider.searchVideos(query, Function<List<Video>, Void?> { videos ->
            testIfItParsesVideoListResponse(videoStruct, videos)
            signal.countDown()
            null
        })

        assertTrue("Callback never called", signal.await(1, TimeUnit.SECONDS))
    }

    private fun testIfItParsesVideoListResponse(videoStruct: VideoResultStruct, videos: List<Video>) {
        val counter = AtomicInteger()

        assertEquals(videoStruct.items.size.toLong(), videos.size.toLong())

        videoStruct.items.forEach(Consumer { rawVideo ->
            val actual = videos[counter.getAndIncrement()]
            assertThat(actual.id, `is`(rawVideo.id.videoId))
            assertThat(actual.channelId, `is`(rawVideo.snippet.channelTitle))
            assertThat(actual.publishedAt, `is`(rawVideo.snippet.publishedAt))
            assertThat(actual.title, `is`(rawVideo.snippet.title))
            assertThat(actual.previewUrl, `is`(rawVideo.snippet.thumbnails.medium.url))
        })
    }
}

