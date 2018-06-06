package habibkazemi.ir.lingualyrics_android.api;

import android.content.Context;

/**
 * Created by habibkazemi on 5/21/18.
 */

public class LyricsApi {
    public static final String BASE_URL = "http://94.102.59.115/";

    public static LyricsService getLyricService(Context context){
        return RetrofitClient.getClient(context, BASE_URL).create(LyricsService.class);
    }
}
