package org.smonard.iutubplayer.data.youtube

import com.google.gson.Gson

import org.smonard.iutubplayer.util.OutOfQuotaException
import org.smonard.iutubplayer.util.YoutubeApiException

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!response.isSuccessful) {
            val apiError = Gson().fromJson(response.body()!!.string(), ErrorResultStruct::class.java).error
            if (apiError != null && apiError.code == 403)
                throw OutOfQuotaException()
            else {
                var apiMessage: String? = null
                if (apiError != null)
                    apiMessage = apiError.message
                throw YoutubeApiException(String.format("Unknown error at Youtube API. %s", apiMessage))
            }
        }

        return response
    }
}