package org.smonard.iutubplayer.data.youtube

import android.os.AsyncTask
import android.util.Log
import org.smonard.iutubplayer.BuildConfig
import org.smonard.iutubplayer.util.InternalException
import org.smonard.iutubplayer.util.MessageBoxHelper
import java.io.IOException
import java.util.function.Function

class VideoProviderAsyncTask(private val service: VideoSearch, private val callback: Function<VideoResultStruct, Void?>) : AsyncTask<VideoProviderAsyncTask.SearchParams, Void, VideoResultStruct>() {

    private var throwable: Exception? = null

    public override fun doInBackground(vararg searchParamsArgs: SearchParams): VideoResultStruct? {
        return try {
            if (searchParamsArgs.size == 1) {
                val searchParams = searchParamsArgs[0]
                if (searchParams.type == SearchType.QUERY) getVideoMatches(searchParams.criteria) else getVideoRelates(searchParams.criteria)
            } else {
                throw InternalException()
            }
        } catch (e: Exception) {
            Log.e("ErrorApiClient", e.message, e)
            throwable = e
            VideoResultStruct()
        }
    }

    public override fun onPostExecute(rsult: VideoResultStruct) {
        super.onPostExecute(rsult)
        if (throwable != null) {
            MessageBoxHelper.showLongMessage(throwable!!.message!!)
        } else {
            callback.apply(rsult)
        }
    }

    @Throws(IOException::class)
    private fun getVideoMatches(query: String?): VideoResultStruct? {
        return service.getVideoMatches(query!!, BuildConfig.YAPI_KEY).execute().body()
    }

    @Throws(IOException::class)
    private fun getVideoRelates(videoId: String?): VideoResultStruct? {
        return service.getVideoRelates(videoId!!, BuildConfig.YAPI_KEY).execute().body()
    }

    open class SearchParams(val type: SearchType, val criteria: String?)

    enum class SearchType {
        QUERY,
        RELATED
    }
}
