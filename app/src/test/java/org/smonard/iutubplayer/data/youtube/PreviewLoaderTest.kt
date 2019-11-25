package org.smonard.iutubplayer.data.youtube

import android.graphics.Bitmap

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.smonard.iutubplayer.helpers.RandomStructs

import java.util.function.Function

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner::class)
class PreviewLoaderTest {

    private lateinit var previewLoader: PreviewLoader

    @Mock
    private val callbackMock: Function<Bitmap, Void?>? = null

    @Test
    @Ignore
    fun doInBackground() {
        val validUrl = "https://i.ytimg.com/vi/ZWFhN_nqgCc/hqdefault_live.jpg"
        previewLoader = PreviewLoader(validUrl, callbackMock!!)

        val bitmap = previewLoader.doInBackground()

        assertNotNull(bitmap)
    }

    @Test
    fun onPostExecution() {
        val result = mock(Bitmap::class.java)
        previewLoader = PreviewLoader(RandomStructs().randomString(), callbackMock!!)

        previewLoader.onPostExecute(result)

        verify<Function<Bitmap, Void?>>(callbackMock).apply(result)
    }
}