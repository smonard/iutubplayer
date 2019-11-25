package org.smonard.iutubplayer.data

import android.content.Context
import android.util.Log

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

import org.smonard.iutubplayer.IutubApplication
import org.smonard.iutubplayer.process.support.Video

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class LocalStorage {

    private val context: Context = IutubApplication.globalContext!!
    private val gson: Gson = GsonBuilder().create()

    fun savePlaylist(videos: List<Video>) {
        writeToFile(PLAYLIST_FILE, gson.toJson(videos))
    }

    fun saveDefaultVideo(video: Video) {
        writeToFile(DEFAULT_VIDEO_FILE, gson.toJson(video))
    }

    fun loadDefaultVideo(): Video {
        return try {
            val inputStream = context.openFileInput(DEFAULT_VIDEO_FILE)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            gson.fromJson(bufferedReader, Video::class.java)
        } catch (e: Exception) {
            Log.e("ErrorFileService", e.message, e)
            Video("JqY92qg_yPs");
        }
    }

    fun loadPlaylist(): ArrayList<Video> {
        return try {
            val inputStream = context.openFileInput(PLAYLIST_FILE)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val listType = object : TypeToken<List<Video>>() {}.type
            gson.fromJson(bufferedReader, listType)
        } catch (e: Exception) {
            Log.e("ErrorFileService", e.message, e)
            ArrayList()
        }

    }

    private fun writeToFile(filename: String, data: String) {
        try {
            val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            outputStream.write(data.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            Log.e("ErrorFileService", e.message, e)
        }

    }

    companion object {
        private const val PLAYLIST_FILE = "iutub_pl.json"
        private const val DEFAULT_VIDEO_FILE = "iutub_def.json"
    }
}
