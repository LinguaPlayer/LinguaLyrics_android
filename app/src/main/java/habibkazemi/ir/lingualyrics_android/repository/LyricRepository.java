package habibkazemi.ir.lingualyrics_android.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import habibkazemi.ir.lingualyrics_android.api.LyricsApi;
import habibkazemi.ir.lingualyrics_android.api.LyricsService;
import habibkazemi.ir.lingualyrics_android.db.LinguaLyricsDb;
import habibkazemi.ir.lingualyrics_android.db.LyricDao;
import habibkazemi.ir.lingualyrics_android.vo.Lyric;
import habibkazemi.ir.lingualyrics_android.vo.LyricLink;
import habibkazemi.ir.lingualyrics_android.vo.Resource;
import retrofit2.Call;

public class LyricRepository {

    LyricDao mLyricDao;
    LiveData<Lyric> mLyric;
    LyricsService mLyricsService;


    public LyricRepository(Application application) {
        LinguaLyricsDb linguaLyricsDb = LinguaLyricsDb.getDatabase(application.getApplicationContext());
        mLyricDao = linguaLyricsDb.lyricDao();
        mLyricsService = LyricsApi.getLyricService(application);
    }

    public LiveData<Resource<Lyric>> loadLyric(String url) {
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
                return mLyricDao.getLyric(url);
            }

            @NonNull
            @Override
            protected Call<Lyric> createCall() {
                return mLyricsService.getLyric(url);
            }
        }.getAsLiveData();
    }

    public LiveData <Resource<List<LyricLink>>> loadLyricUrls(String query) {
        return new NetworkBoundResource < List<LyricLink> , List<LyricLink> > () {
            @Override
            protected void saveCallResult(@NonNull List<LyricLink> items) {
                mLyricDao.insert(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<LyricLink> data) {
                // Fetch data from network anyway
                return true;
            }

            @NonNull
            @Override
            protected LiveData <List<LyricLink>> loadFromDb() {
                return mLyricDao.getLyricList(query);
            }

            @NonNull
            @Override
            protected Call <List<LyricLink>> createCall() {
                return mLyricsService.getLyricList(query);
            }
        }.getAsLiveData();
    }

    public LiveData<Lyric> getLyricById(int id ) {
        return mLyricDao.getLyricById(id);
    }
}
