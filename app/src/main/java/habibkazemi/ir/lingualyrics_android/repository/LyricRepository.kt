package habibkazemi.ir.lingualyrics_android.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList

import habibkazemi.ir.lingualyrics_android.api.LyricsApi
import habibkazemi.ir.lingualyrics_android.api.LyricsService
import habibkazemi.ir.lingualyrics_android.db.LinguaLyricsDb
import habibkazemi.ir.lingualyrics_android.db.LyricDao
import habibkazemi.ir.lingualyrics_android.vo.Lyric
import habibkazemi.ir.lingualyrics_android.vo.LyricLink
import habibkazemi.ir.lingualyrics_android.vo.Resource
import retrofit2.Call

class LyricRepository(application: Application) {

    internal var mLyricDao: LyricDao
    internal var mLyric: LiveData<Lyric>? = null
    internal var mLyricsService: LyricsService


    init {
        val linguaLyricsDb = LinguaLyricsDb.getDatabase(application.applicationContext)
        mLyricDao = linguaLyricsDb.lyricDao()
        mLyricsService = LyricsApi.getLyricService(application)
    }

    fun loadLyric(url: String): LiveData<Resource<Lyric>> {
        return object : NetworkBoundResource<Lyric, Lyric>() {

            override fun saveCallResult(item: Lyric?) {
                item?.let{
                    mLyricDao.insert(it)
                }
            }

            override fun shouldFetch(data: Lyric?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<Lyric> {
                return mLyricDao.getLyric(url)
            }

            override fun createCall(): Call<Lyric> {
                return mLyricsService.getLyric(url)
            }
        }.asLiveData
    }

    fun loadLyricUrls(query: String): LiveData<Resource<PagedList<LyricLink>>> {
        return object : NetworkBoundResource<PagedList<LyricLink>, List<LyricLink>>() {
            override fun saveCallResult(items: List<LyricLink>?) {
                items?.let {
                    mLyricDao.insert(it)
                }
            }

            override fun shouldFetch(data: PagedList<LyricLink>?): Boolean {
                // Fetch data from network anyway
                return true
            }

            override fun loadFromDb(): LiveData<PagedList<LyricLink>> {
                val dataSourceFactory = mLyricDao.getLyricList(query)
                return LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).build()
            }

            override fun createCall(): Call<List<LyricLink>> {
                return mLyricsService.getLyricList(query)
            }
        }.asLiveData
    }

    fun getLyricById(id: Int): LiveData<Lyric> {
        return mLyricDao.getLyricById(id)
    }

    companion object {

        private const val DATABASE_PAGE_SIZE = 20
    }
}
