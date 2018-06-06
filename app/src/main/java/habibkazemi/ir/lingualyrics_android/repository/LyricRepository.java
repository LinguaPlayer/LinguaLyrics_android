package habibkazemi.ir.lingualyrics_android.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import habibkazemi.ir.lingualyrics_android.api.LyricsApi;
import habibkazemi.ir.lingualyrics_android.api.LyricsService;
import habibkazemi.ir.lingualyrics_android.db.LinguaLyricsDb;
import habibkazemi.ir.lingualyrics_android.db.LyricDao;
import habibkazemi.ir.lingualyrics_android.vo.Lyric;
import habibkazemi.ir.lingualyrics_android.vo.LyricQuery;
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

    public LiveData<Lyric> getLyric(String title, String artist) {
        refreshLyric(new LyricQuery(title, artist));
        return mLyricDao.getLyric(title, artist);

    }

    public void refreshLyric (final LyricQuery lyricQuery) {
        new RefreshAsyncTask(mLyricDao, mLyricsService).execute(lyricQuery);
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

    // TODO: Replace this with WorkManager
    // TODO: Use different Executer for IO and Network
    private static class RefreshAsyncTask extends AsyncTask<LyricQuery, Void, Void> {

        private LyricDao mLyricDao;
        private LyricsService mLyricService;

        RefreshAsyncTask (LyricDao lyricDao, LyricsService lyricsService) {
            this.mLyricService = lyricsService;
            this.mLyricDao = lyricDao;
        }

        @Override
        protected Void doInBackground(LyricQuery... lyricQueries) {
            LyricQuery lyricQuery = lyricQueries[0];

            boolean lyricExist = mLyricDao.hasLyric(lyricQuery.getTitle(), lyricQuery.getArtist());

            Lyric lyric = null;
            Log.d("Lyric", "lyricExist" + lyricExist);
            if (!lyricExist) {
                // TODO: Handle errors in proper way and return reasons of error and make a diff between not found and Error
                try {
                    Response<Lyric> response = this.mLyricService.getLyric(lyricQuery.getTitle()+ lyricQuery.getArtist(), "").execute();
                    lyric = response.body();
                    this.mLyricDao.insert(lyric);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
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
