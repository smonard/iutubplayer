package org.smonard.iutubplayer.data

import android.content.Context

import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.internal.matchers.apachecommons.ReflectionEquals
import org.mockito.junit.MockitoJUnitRunner
import org.smonard.iutubplayer.IutubApplication
import org.smonard.iutubplayer.helpers.RandomStructs
import org.smonard.iutubplayer.process.support.Video

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

import org.junit.Assert.*
import org.mockito.Matchers.anyString
import org.mockito.Matchers.eq
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class LocalStorageTest {

    private lateinit var localStorage: LocalStorage
    private val random = RandomStructs()

    @Mock
    private val context: Context? = null

    @Before
    @Throws(FileNotFoundException::class)
    fun setUp() {
        val iutubApplication = object : IutubApplication() {
            override fun getApplicationContext(): Context? {
                return context
            }
        }
        iutubApplication.onCreate()
        localStorage = LocalStorage()

        val testFile = File("./test.json")
        val stream = FileOutputStream(testFile)
        val streamR = FileInputStream(testFile)

        `when`(context!!.openFileOutput(anyString(), eq(Context.MODE_PRIVATE))).thenReturn(stream)
        `when`(context.openFileInput(anyString())).thenReturn(streamR)
    }

    @Test
    fun savesAndLoadsDefaultVideo() {
        val video = random.nextObject(Video::class.java)

        localStorage.saveDefaultVideo(video)
        val actual = localStorage.loadDefaultVideo()

        assertTrue(ReflectionEquals(video).matches(actual))
    }

    @Test
    fun savesAndLoadsPlaylist() {
        val videos = random.getList(Video::class.java, 5)

        localStorage.savePlaylist(videos)
        val actual = localStorage.loadPlaylist()

        assertTrue(ReflectionEquals(videos).matches(actual))
    }

    @Test
    @Throws(FileNotFoundException::class)
    fun loadsFallbackDefaultVideoWhenError() {
        `when`(context!!.openFileInput(anyString())).thenReturn(null)
        val video = random.nextObject(Video::class.java)

        localStorage.saveDefaultVideo(video)
        val actual = localStorage.loadDefaultVideo()

        assertThat(actual.id, Is.`is`("JqY92qg_yPs"))
    }

    @Test
    @Throws(FileNotFoundException::class)
    fun loadsEmptyPlaylistWhenError() {
        `when`(context!!.openFileInput(anyString())).thenReturn(null)
        val videos = random.getList(Video::class.java, 5)

        localStorage.savePlaylist(videos)
        val actual = localStorage.loadPlaylist()

        assertTrue(actual.isEmpty())
    }
}