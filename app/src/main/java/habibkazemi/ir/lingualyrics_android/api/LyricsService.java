package habibkazemi.ir.lingualyrics_android.api;

import java.util.List;

import habibkazemi.ir.lingualyrics_android.vo.Lyric;
import habibkazemi.ir.lingualyrics_android.vo.LyricLink;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by habibkazemi on 5/21/18.
 */

public interface LyricsService {
    @GET("/api/v1/url/")
    Call<Lyric> getLyric(@Query("url") String url);


    @GET("/api/v1/")
    Call <List<LyricLink>> getLyricList(@Query("query") String query);
}
