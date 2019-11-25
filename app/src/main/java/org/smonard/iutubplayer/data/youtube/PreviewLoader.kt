package org.smonard.iutubplayer.data.youtube

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log

import java.net.HttpURLConnection
import java.net.URL
import java.util.function.Function

class PreviewLoader(private val url: String, private val callback: Function<Bitmap, Void?>) : AsyncTask<Void, Void, Bitmap>() {

    public override fun doInBackground(vararg params: Void): Bitmap? {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            return BitmapFactory.decodeStream(connection.inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ErrorImgLoader", e.message, e)
        }

        return null
    }

    public override fun onPostExecute(result: Bitmap) {
        callback.apply(result)
    }

}