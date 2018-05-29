package habibkazemi.ir.lingualyrics_android.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;

import java.io.IOException;

import habibkazemi.ir.lingualyrics_android.model.Lyric;
import habibkazemi.ir.lingualyrics_android.remote.LyricsApi;
import habibkazemi.ir.lingualyrics_android.remote.LyricsService;
import habibkazemi.ir.lingualyrics_android.util.Constants;
import retrofit2.Response;

/**
 * Created by habibkazemi on 5/26/18.
 */

public class LyricAsyncTaskLoader extends AsyncTaskLoader<Lyric> {

    private Lyric mLyric;
    private Bundle mArgs;

    // TODO: remove this and use real database
    private String dummyDatabase = "{\"artist\":\"\",\"result\":{\"album\":\"Greg Kurstin\",\"artist\":\"Adele\",\"cover_art_image_url\":\"https://t2.genius.com/unsafe/220x220/https%3A%2F%2Fimages.genius.com%2F56fe4a76d3a480e425824b2e18a2e983.1000x1000x1.jpg\",\"lyric_text\":\"[THIS IS A DUMMY DATABASE DATA AND SHOULD BE REPLACED WITH REAL DATABASE DATA]\\n\\n\\nHello, it\\u0027s me\\nI was wondering if after all these years you\\u0027d like to meet\\nTo go over everything\\nThey say that time\\u0027s supposed to heal ya, but I ain\\u0027t done much healing\\nHello, can you hear me?\\nI\\u0027m in California dreaming about who we used to be\\nWhen we were younger and free\\nI\\u0027ve forgotten how it felt before the world fell at our feet\\nThere\\u0027s such a difference between us\\nAnd a million miles[Chorus]\\nHello from the other side\\nI must\\u0027ve called a thousand times\\nTo tell you I\\u0027m sorry\\nFor everything that I\\u0027ve done\\nBut when I call, you never seem to be home\\nHello from the outside\\nAt least I can say that I\\u0027ve tried\\nTo tell you I\\u0027m sorry, for breaking your heart\\nBut it don\\u0027t matter\\nIt clearly doesn\\u0027t tear you apart anymore[Verse 2]\\nHello, how are you?\\nIt\\u0027s so typical of me to talk about myself, I\\u0027m sorry\\nI hope that you\\u0027re well\\nDid you ever make it out of that town where nothing ever happened?\\nIt\\u0027s no secret that the both of us\\nAre running out of time[Chorus]\\nSo hello from the other side\\nI must\\u0027ve called a thousand times\\nTo tell you I\\u0027m sorry\\nFor everything that I\\u0027ve done\\nBut when I call, you never seem to be home\\nHello from the outside\\nAt least I can say that I\\u0027ve tried\\nTo tell you I\\u0027m sorry, for breaking your heart\\nBut it don\\u0027t matter\\nIt clearly doesn\\u0027t tear you apart anymore[Bridge]\\n(Highs, highs, highs, highs, lows, lows, lows, lows)\\nOoh, anymore\\n(Highs, highs, highs, highs, lows, lows, lows, lows)\\nOoh, anymore\\n(Highs, highs, highs, highs, lows, lows, lows, lows)\\nOoh, anymore\\n(Highs, highs, highs, highs, lows, lows, lows, lows)\\nAnymore[Chorus]\\nHello from the other side\\nI must\\u0027ve called a thousand times\\nTo tell you I\\u0027m sorry\\nFor everything that I\\u0027ve done\\nBut when I call, you never seem to be home\\nHello from the outside\\nAt least I can say that I\\u0027ve tried\\nTo tell you I\\u0027m sorry, for breaking your heart\\nBut it don\\u0027t matter\\nIt clearly doesn\\u0027t tear you apart anymore[Produced by Greg Kurstin]\\n[Music Video]\",\"title\":\"Hello\"},\"title\":\"hello adele\"}";

    public LyricAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        mArgs = args;
    }

    @Override
    protected void onStartLoading() {
        if (mLyric != null) {
            deliverResult(mLyric);
        } else {
            forceLoad();
        }
    }

    @Override
    public Lyric loadInBackground() {
        //1- Activity just created (app starts or closed by backButton) and there is no query yet
        // and I should load the previous data

        //2- And as I'm using this in fragment that's in the navigation menu and i'm recreating it
        // in each configuration change and ... so I need to load the previous data from database

        // In above situation mArgs is null because I call initLoaderManager with null bundle
        // in onCreate

        if (mArgs == null) {
            // Load previous data from database
            mLyric = new Gson().fromJson(dummyDatabase, Lyric.class);
            return mLyric;
        }

        String query = mArgs.getString(Constants.SEARCH_QUERY_URL_EXTRA);
        LyricsService lyricsService = LyricsApi.getLyricService(getContext());
        Lyric lyric = null;
        // TODO: Handle errors in proper way and return reasons of error and make a diff between not found and Error
        try {
            Response<Lyric> response = lyricsService.getLyric(query, "").execute();
            lyric = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lyric;
    }

    @Override
    public void deliverResult(Lyric lyric) {
        // If data not changes after activity pause LoaderManager not calls the onLoadFinished
        // so I made a clone of it
        // https://medium.com/@ramona.cristea89/hello-ian-lake-9729bf91a9e6

        mLyric = lyric.clone();
        super.deliverResult(mLyric);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }
}
