package ir.habibkazemi.lingualyrics.api

import ir.habibkazemi.lingualyrics.vo.Lyric
import ir.habibkazemi.lingualyrics.vo.LyricLink
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by habibkazemi on 5/21/18.
 */

interface LyricsService {
    @GET("/api/v1/url/")
    fun getLyric(@Query("url") url: String): Call<Lyric>


    @GET("/api/v1/")
    fun getLyricList(@Query("query") query: String): Call<List<LyricLink>>
}
