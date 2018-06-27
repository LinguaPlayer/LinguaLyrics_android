package habibkazemi.ir.lingualyrics_android.api

import android.content.Context

/**
 * Created by habibkazemi on 5/21/18.
 */

object LyricsApi {
    val BASE_URL = "http://lingualyrics.habibkazemi.ir/"
    //    public static final String BASE_URL = "http://10.0.2.2:8000/";

    fun getLyricService(context: Context): LyricsService {
        return RetrofitClient.getClient(context, BASE_URL).create(LyricsService::class.java)
    }
}
