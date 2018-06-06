package habibkazemi.ir.lingualyrics_android.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import habibkazemi.ir.lingualyrics_android.vo.Lyric;

@Dao
public interface LyricDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Lyric lyric);

    // TODO: FIX THIS API From server side
    @Query("SELECT * from Lyric where title = :title and artist = :artist")
    LiveData<Lyric> getLyric(String title, String artist);

    @Query("SELECT * from Lyric")
    LiveData<List<Lyric>> getAllLyrics();

    @Query("SELECT * FROM Lyric where id = :id")
    LiveData<Lyric> getLyricById(int id);

    // TODO: FIX THIS API From server side
    @Query("Select * from lyric where title = :title and artist = :artist")
    boolean hasLyric(String title, String artist);
}
