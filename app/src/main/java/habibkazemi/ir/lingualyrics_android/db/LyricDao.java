package habibkazemi.ir.lingualyrics_android.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import habibkazemi.ir.lingualyrics_android.vo.Lyric;
import habibkazemi.ir.lingualyrics_android.vo.LyricLink;

@Dao
public interface LyricDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Lyric lyric);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<LyricLink> lyricLinks);

    @Query("SELECT * from Lyric where url = :url")
    LiveData<Lyric> getLyric(String url);

    // Description has both artist and title data inside
    @Query("SELECT * FROM LyricLink where userQuery LIKE \'%\' || :query ||  \'%\' OR  description LIKE \'%\' || :query ||  \'%\' ")
    android.arch.paging.DataSource.Factory<Integer, LyricLink> getLyricList(String query);

    @Query("SELECT * FROM Lyric where id = :id")
    LiveData<Lyric> getLyricById(int id);
}
