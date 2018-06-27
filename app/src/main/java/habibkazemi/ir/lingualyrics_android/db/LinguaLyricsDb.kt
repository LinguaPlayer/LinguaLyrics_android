package habibkazemi.ir.lingualyrics_android.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import habibkazemi.ir.lingualyrics_android.vo.Lyric
import habibkazemi.ir.lingualyrics_android.vo.LyricLink

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
