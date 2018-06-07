package habibkazemi.ir.lingualyrics_android.ui.lyric;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import java.util.List;
import habibkazemi.ir.lingualyrics_android.repository.LyricRepository;
import habibkazemi.ir.lingualyrics_android.util.Constants;
import habibkazemi.ir.lingualyrics_android.vo.Lyric;
import habibkazemi.ir.lingualyrics_android.vo.LyricQuery;
import habibkazemi.ir.lingualyrics_android.vo.Resource;

import static android.content.Context.MODE_PRIVATE;

public class LyricViewModel extends AndroidViewModel {
    private LyricRepository mRepository;

    private final MutableLiveData<LyricQuery> mLyricQueryMutableLiveData = new MutableLiveData<>();
    private final MediatorLiveData <Resource<Lyric>> mLyricLiveData = new MediatorLiveData();
    private final LiveData <Lyric> mLastLyricLiveData;

    private final LiveData<Resource<Lyric>> mLyricLiveDataSource1 = Transformations.switchMap(mLyricQueryMutableLiveData,
            new Function<LyricQuery, LiveData<Resource<Lyric>>>() {
                @Override
                public LiveData<Resource<Lyric>> apply(LyricQuery input) {
                    return mRepository.loadLyric(input.getTitle(), input.getArtist());
                }
    });



    public LyricViewModel(@NonNull Application application) {
        super(application);
        mRepository = new LyricRepository(application);
        mLastLyricLiveData = getLastLyric();
        mLyricLiveData.addSource(mLyricLiveDataSource1, lyricResource -> mLyricLiveData.setValue(lyricResource));

        mLyricLiveData.addSource(mLastLyricLiveData, lyricResource -> {
            // We don't need this anymore
            mLyricLiveData.removeSource(mLastLyricLiveData);

            mLyricLiveData.setValue(Resource.success(lyricResource));
        });
    }

    public LiveData<Resource<Lyric>> getLyric() {
        return mLyricLiveData;
    }

    public LiveData<List<Lyric>> getAllLyrics() {
        return mRepository.getAllLyrics();
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
