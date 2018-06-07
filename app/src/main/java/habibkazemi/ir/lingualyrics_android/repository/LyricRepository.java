package habibkazemi.ir.lingualyrics_android.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import habibkazemi.ir.lingualyrics_android.api.LyricsApi;
import habibkazemi.ir.lingualyrics_android.api.LyricsService;
import habibkazemi.ir.lingualyrics_android.db.LinguaLyricsDb;
import habibkazemi.ir.lingualyrics_android.db.LyricDao;
import habibkazemi.ir.lingualyrics_android.vo.Lyric;
import habibkazemi.ir.lingualyrics_android.vo.LyricQuery;
import habibkazemi.ir.lingualyrics_android.vo.Resource;
import retrofit2.Call;
import retrofit2.Response;

public class LyricRepository {

    LyricDao mLyricDao;
    LiveData<Lyric> mLyric;
    LyricsService mLyricsService;


    public LyricRepository(Application application) {
        LinguaLyricsDb linguaLyricsDb = LinguaLyricsDb.getDatabase(application.getApplicationContext());
        mLyricDao = linguaLyricsDb.lyricDao();
        mLyricsService = LyricsApi.getLyricService(application);
    }

    public LiveData<Resource<Lyric>> loadLyric(String title, String artist) {
        return new NetworkBoundResource<Lyric, Lyric>() {
            @Override
            protected void saveCallResult(@NonNull Lyric item) {
                mLyricDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Lyric data) {
                if (data == null)
                    return true;
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Lyric> loadFromDb() {
                return mLyricDao.getLyric(title, artist);
            }

            @NonNull
            @Override
            protected Call<Lyric> createCall() {
                return mLyricsService.getLyric(title, artist);
            }
        }.getAsLiveData();
    }


    public LiveData<List<Lyric>> getAllLyrics() {
        return mLyricDao.getAllLyrics();
    }

    public LiveData<Lyric> getLyricById(int id ) {
        return mLyricDao.getLyricById(id);
    }

    public void insert(Lyric lyric) {
        new InsertAsyncTask(mLyricDao).execute(lyric);
    }

    private static class InsertAsyncTask extends AsyncTask<Lyric, Void, Void> {

        private LyricDao lyricDao;

        private InsertAsyncTask(LyricDao lyricDao) {
            this.lyricDao = lyricDao;
        }

        @Override
        protected Void doInBackground(Lyric... lyrics) {
            lyricDao.insert(lyrics[0]);
            return null;
        }
    }
}
