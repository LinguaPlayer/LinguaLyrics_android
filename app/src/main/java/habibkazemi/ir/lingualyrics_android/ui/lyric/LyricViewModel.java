package habibkazemi.ir.lingualyrics_android.ui.lyric;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.List;

import habibkazemi.ir.lingualyrics_android.repository.LyricRepository;
import habibkazemi.ir.lingualyrics_android.util.Constants;
import habibkazemi.ir.lingualyrics_android.vo.Lyric;
import habibkazemi.ir.lingualyrics_android.vo.LyricQuery;

import static android.content.Context.MODE_PRIVATE;

public class LyricViewModel extends AndroidViewModel {
    private LyricRepository mRepository;

    private final MutableLiveData<LyricQuery> mLyricQueryMutableLiveData = new MutableLiveData<>();

    private final LiveData<Lyric> mLyricLiveData = Transformations.switchMap(mLyricQueryMutableLiveData,
            new Function<LyricQuery, LiveData<Lyric>>() {
                @Override
                public LiveData<Lyric> apply(LyricQuery input) {
                    return mRepository.getLyric(input.getTitle(), input.getArtist());
                }
    });


    public LyricViewModel(@NonNull Application application) {
        super(application);
        mRepository = new LyricRepository(application);
    }

    public LiveData<Lyric> getLyric() {
        return mLyricLiveData;
    }


    public LiveData<List<Lyric>> getAllLyrics() {
        return mRepository.getAllLyrics();
    }

    public void insert(Lyric lyric) {
        mRepository.insert(lyric);
    }

    public void setLyricQuery(LyricQuery lyricQuery) {
        mLyricQueryMutableLiveData.setValue(lyricQuery);
    }

    public LiveData<Lyric> getLastLyric() {
        int id = getApplication().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).getInt(Constants.LAST_ACCESSED_LYRIC_ID, 0);
        return mRepository.getLyricById(id);
    }

    public void setLastLyric(Lyric lastLyric) {
        SharedPreferences.Editor editor = getApplication().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
        editor.putInt(Constants.LAST_ACCESSED_LYRIC_ID, lastLyric.getId());
        editor.apply();
    }

}
