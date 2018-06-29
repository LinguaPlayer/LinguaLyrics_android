package ir.habibkazemi.lingualyrics.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import ir.habibkazemi.lingualyrics.vo.Lyric
import ir.habibkazemi.lingualyrics.vo.LyricLink

@Dao
interface LyricDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lyric: Lyric)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lyricLinks: List<LyricLink>)

    @Query("SELECT * from Lyric where url = :url")
    fun getLyric(url: String): LiveData<Lyric>

    // Description has both artist and title data inside
    @Query("SELECT * FROM LyricLink where userQuery LIKE \'%\' || :query ||  \'%\' OR  description LIKE \'%\' || :query ||  \'%\' ")
    fun getLyricList(query: String): android.arch.paging.DataSource.Factory<Int, LyricLink>

    @Query("SELECT * FROM Lyric where id = :id")
    fun getLyricById(id: Int): LiveData<Lyric>
}
