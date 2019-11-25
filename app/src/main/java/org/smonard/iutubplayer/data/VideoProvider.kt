package org.smonard.iutubplayer.data

import org.smonard.iutubplayer.data.youtube.ResponseInterceptor
import org.smonard.iutubplayer.data.youtube.VideoProviderAsyncTask
import org.smonard.iutubplayer.data.youtube.VideoResultStruct
import org.smonard.iutubplayer.data.youtube.VideoSearch
import org.smonard.iutubplayer.process.support.Video
import java.util.function.Function
import java.util.stream.Collectors

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoProvider {
    private val service: VideoSearch

    init {
        val httpClient = OkHttpClient.Builder()
                .addInterceptor(ResponseInterceptor())
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()

        service = retrofit.create(VideoSearch::class.java)
    }

    fun searchRelatedVideos(videoId: String?, callback: Function<List<Video>, Void?>?) {
        val searchParams = VideoProviderAsyncTask.SearchParams(VideoProviderAsyncTask.SearchType.RELATED, videoId)
        VideoProviderAsyncTask(service, parseAndCallback(callback!!)).execute(searchParams)
    }

    fun searchVideos(query: String?, callback: Function<List<Video>, Void?>?) {
        val searchParams = VideoProviderAsyncTask.SearchParams(VideoProviderAsyncTask.SearchType.QUERY, query)
        VideoProviderAsyncTask(service, parseAndCallback(callback!!)).execute(searchParams)
    }

    private fun parseAndCallback(callback: Function<List<Video>, Void?>): Function<VideoResultStruct, Void?> {
        return Function { videoResult ->
            val videos = videoResult.items.stream().map { item -> Video(item.id.videoId, item.snippet.channelTitle, item.snippet.publishedAt, item.snippet.title, item.snippet.thumbnails.medium.url) }
                    .collect(Collectors.toList())
            callback.apply(videos)
            null
        }
    }
}
