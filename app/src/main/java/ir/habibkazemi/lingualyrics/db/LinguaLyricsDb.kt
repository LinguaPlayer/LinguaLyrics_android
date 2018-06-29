package ir.habibkazemi.lingualyrics.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import ir.habibkazemi.lingualyrics.vo.Lyric
import ir.habibkazemi.lingualyrics.vo.LyricLink

/**
 * Created by habibkazemi on 5/29/18
 */

@Database(entities = [(Lyric::class), (LyricLink::class)], version = 1, exportSchema = false)
abstract class LinguaLyricsDb : RoomDatabase() {
    abstract fun lyricDao(): LyricDao

    companion object {

        @Volatile private var INSTANCE: LinguaLyricsDb? = null
        fun getDatabase(context: Context): LinguaLyricsDb =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        LinguaLyricsDb::class.java, "lyric_database")
                        .build()
    }

}
