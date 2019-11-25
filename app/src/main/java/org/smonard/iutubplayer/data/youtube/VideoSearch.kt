package org.smonard.iutubplayer.data.youtube

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoSearch {
    @GET("youtube/v3/search?part=snippet&type=video&maxResults=15")
    fun getVideoMatches(@Query("q") query: String, @Query("key") apiKey: String): Call<VideoResultStruct>

    @GET("youtube/v3/search?part=snippet&type=video&maxResults=30")
    fun getVideoRelates(@Query("relatedToVideoId") videoId: String, @Query("key") apiKey: String): Call<VideoResultStruct>
}
