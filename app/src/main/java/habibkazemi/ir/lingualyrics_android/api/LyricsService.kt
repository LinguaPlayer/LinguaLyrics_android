package habibkazemi.ir.lingualyrics_android.api

import android.arch.paging.PagedList

import habibkazemi.ir.lingualyrics_android.vo.Lyric
import habibkazemi.ir.lingualyrics_android.vo.LyricLink
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
