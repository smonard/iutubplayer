package org.smonard.iutubplayer.data.youtube

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.internal.matchers.apachecommons.ReflectionEquals
import org.mockito.junit.MockitoJUnitRunner
import org.smonard.iutubplayer.helpers.RandomStructs
import org.smonard.iutubplayer.helpers.ReflectionTestUtils

import java.io.IOException
import java.util.function.Function

import retrofit2.Response

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.mockito.Matchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.smonard.iutubplayer.helpers.TestUtils

@RunWith(MockitoJUnitRunner::class)
class VideoProviderAsyncTaskTest {

    @InjectMocks
    private val videoProviderAsyncTask: VideoProviderAsyncTask? = null

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private val search: VideoSearch? = null

    @Mock
    private val callback: Function<VideoResultStruct, Void>? = null

    private val structs = RandomStructs()

    // # DoInBackground

    @Test
    @Throws(IOException::class)
    fun returnsAVideoResultStructureForVideoQuerying() {
        val criteria = structs.randomString()
        val expected = mock(VideoResultStruct::class.java)
        val params = VideoProviderAsyncTask.SearchParams(VideoProviderAsyncTask.SearchType.QUERY, criteria)
        `when`(search!!.getVideoMatches(TestUtils.eq(criteria), anyString()).execute()).thenReturn(Response.success(expected))

        val videoResult = videoProviderAsyncTask!!.doInBackground(params)

        assertEquals(expected, videoResult)
    }

    @Test
    @Throws(IOException::class)
    fun returnsAVideoResultStructureForRelatedVideosSearch() {
        val videoId = structs.randomString()
        val expected = mock(VideoResultStruct::class.java)
        val params = VideoProviderAsyncTask.SearchParams(VideoProviderAsyncTask.SearchType.RELATED, videoId)
        `when`(search!!.getVideoRelates(TestUtils.eq(videoId), anyString()).execute()).thenReturn(Response.success(expected))

        val videoResult = videoProviderAsyncTask!!.doInBackground(params)

        assertEquals(expected, videoResult)
    }

    @Test
    @Throws(IOException::class)
    fun returnsEmptyVideoResultWhenError() {
        val params = structs.nextObject(VideoProviderAsyncTask.SearchParams::class.java)
        `when`(search!!.getVideoMatches(TestUtils.eq(params.criteria!!), anyString()).execute()).thenThrow(RuntimeException("I'm an error"))

        val videoResult = videoProviderAsyncTask!!.doInBackground(params)

        assertTrue(ReflectionEquals(VideoResultStruct()).matches(videoResult))
    }

    @Test
    @Throws(IOException::class)
    fun returnsEmptyVideoResultWhenWrongNumberOfParameters() {
        val param1 = structs.nextObject(VideoProviderAsyncTask.SearchParams::class.java)
        val param2 = structs.nextObject(VideoProviderAsyncTask.SearchParams::class.java)
        val params = arrayOf(param1, param2)

        val videoResult = videoProviderAsyncTask!!.doInBackground(*params)

        assertTrue(ReflectionEquals(VideoResultStruct()).matches(videoResult))
    }

    // ## OnPostExecute

    @Test
    fun appliesCallback() {
        val videoResult = VideoResultStruct()

        videoProviderAsyncTask!!.onPostExecute(videoResult)

        verify<Function<VideoResultStruct, Void>>(callback).apply(videoResult)
    }

    @Test
    fun doesNotAppliesCallbackWhenError() {
        val videoResult = VideoResultStruct()
        ReflectionTestUtils.setFieldValue(videoProviderAsyncTask!!, "throwable", RuntimeException("I'm an error"))

        videoProviderAsyncTask.onPostExecute(videoResult)

        verify<Function<VideoResultStruct, Void>>(callback, never()).apply(videoResult)
    }
}