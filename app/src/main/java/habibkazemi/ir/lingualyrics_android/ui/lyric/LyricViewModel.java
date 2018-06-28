package habibkazemi.ir.lingualyrics_android.ui.lyric;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.PagedList;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import habibkazemi.ir.lingualyrics_android.repository.LyricRepository;
import habibkazemi.ir.lingualyrics_android.util.Constants;
import habibkazemi.ir.lingualyrics_android.vo.Lyric;
import habibkazemi.ir.lingualyrics_android.vo.LyricLink;
import habibkazemi.ir.lingualyrics_android.vo.Resource;

import static android.content.Context.MODE_PRIVATE;

public class LyricViewModel extends AndroidViewModel {
    private LyricRepository mRepository;

    private final MutableLiveData<String> mLyricQueryByUrlMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> mLyricQueryMutableLiveData = new MutableLiveData<>();
    private final MediatorLiveData <Resource<Lyric>> mLyricMediatorLiveData = new MediatorLiveData();
    private final LiveData <Lyric> mLastLyricLiveData;
    private boolean mAppBarWasCollapsed = true;

    private final LiveData<Resource<Lyric>> mLyricLiveDataSource = Transformations.switchMap(mLyricQueryByUrlMutableLiveData,
            new Function<String, LiveData<Resource<Lyric>>>() {
                @Override
                public LiveData<Resource<Lyric>> apply(String url) {
                    return mRepository.loadLyric(url);
                }
            });

    private final LiveData<Resource<PagedList<LyricLink>>> mLyricQueryLiveData = Transformations.switchMap(mLyricQueryMutableLiveData,
            new Function<String, LiveData<Resource<PagedList<LyricLink>>> >() {
                @Override
                public LiveData <Resource<PagedList<LyricLink>>> apply(String query) {
                    return mRepository.loadLyricUrls(query);
                }
            });



    public LyricViewModel(@NonNull Application application) {
        super(application);
        mRepository = new LyricRepository(application);
        mLastLyricLiveData = getLastLyric();
        mLyricMediatorLiveData.addSource(mLyricLiveDataSource, lyricResource -> mLyricMediatorLiveData.setValue(lyricResource));

        mLyricMediatorLiveData.addSource(mLastLyricLiveData, lyricResource -> {
            // We don't need this anymore
            mLyricMediatorLiveData.removeSource(mLastLyricLiveData);

            mLyricMediatorLiveData.setValue(Resource.Companion.success(lyricResource));
        });
    }

    public LiveData<Resource<Lyric>> getLyric() {
        return mLyricMediatorLiveData;
    }

    public void setLyricQuery(String query) {
        mLyricQueryMutableLiveData.setValue(query);
    }

    public void queryLyricByUrl(String url) {
        mLyricQueryByUrlMutableLiveData.setValue(url);
    }

    public LiveData<Lyric> getLastLyric() {
        int id = getApplication().getSharedPreferences(Constants.INSTANCE.getSHARED_PREFERENCES_NAME(), MODE_PRIVATE).getInt(Constants.INSTANCE.getLAST_ACCESSED_LYRIC_ID(), 0);
        return mRepository.getLyricById(id);
    }

    public void setLastLyric(Lyric lastLyric) {
        if (lastLyric != null) {
            SharedPreferences.Editor editor = getApplication().getSharedPreferences(Constants.INSTANCE.getSHARED_PREFERENCES_NAME(), MODE_PRIVATE).edit();
            editor.putInt(Constants.INSTANCE.getLAST_ACCESSED_LYRIC_ID(), lastLyric.getId());
            editor.apply();
        }
    }

    public LiveData<Resource<PagedList<LyricLink>>> getLyricQueryInDatabaseLiveData() {
        return mLyricQueryLiveData;
    }

    public void setIsAppBarCollapsed(boolean collapsed) {
        this.mAppBarWasCollapsed = collapsed;
    }

    public boolean getIsAppBarCollapsed(){
        return this.mAppBarWasCollapsed;
    }
}
