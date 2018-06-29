package ir.habibkazemi.lingualyrics.ui.lyric

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import ir.habibkazemi.lingualyrics.repository.LyricRepository
import ir.habibkazemi.lingualyrics.util.Constants
import ir.habibkazemi.lingualyrics.vo.Lyric
import ir.habibkazemi.lingualyrics.vo.Resource

import android.content.Context.MODE_PRIVATE

class LyricViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: LyricRepository = LyricRepository(application)

    private val mLyricQueryByUrlMutableLiveData = MutableLiveData<String>()
    private val mLyricQueryByKeywordMutableLiveData = MutableLiveData<String>()

    internal var lastQuery: String = ""
        private set

    private val mLyricMediatorLiveData: MediatorLiveData<Resource<Lyric>> = MediatorLiveData()
    var appBarCollapsed: Boolean = true

    val lyricList = Transformations.switchMap(mLyricQueryByKeywordMutableLiveData) {
        query -> mRepository.loadLyricUrls(query)
    }

    private val mLyricLiveDataSource = Transformations.switchMap(mLyricQueryByUrlMutableLiveData) {
        url -> mRepository.loadLyric(url)
    }

    val lyric: LiveData<Resource<Lyric>>
        get() = mLyricMediatorLiveData

    private val mLastLyricLiveData: LiveData<Lyric>
        get() {
            val id = getApplication<Application>().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                    MODE_PRIVATE).getInt(Constants.LAST_ACCESSED_LYRIC_ID, 0)
            return mRepository.getLyricById(id)
        }

    init {
        mLyricMediatorLiveData.addSource(mLyricLiveDataSource) {
            lyricResource -> mLyricMediatorLiveData.setValue(lyricResource)
        }

        mLyricMediatorLiveData.addSource(mLastLyricLiveData) { lyricResource ->
            // We don't need this anymore
            mLyricMediatorLiveData.removeSource(mLastLyricLiveData)
            mLyricMediatorLiveData.setValue(Resource.success(lyricResource))
        }
    }

    fun setLyricQuery(query: String) {
        mLyricQueryByKeywordMutableLiveData.value = query
        lastQuery = query
    }

    fun queryLyricByUrl(url: String?) {
        mLyricQueryByUrlMutableLiveData.value = url
    }

    fun setLastLyric(lastLyric: Lyric?) {
        val editor = getApplication<Application>().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit()
        lastLyric?.id?.let {
            id -> editor.putInt(Constants.LAST_ACCESSED_LYRIC_ID, id)
            editor.apply()
        }
    }
}
