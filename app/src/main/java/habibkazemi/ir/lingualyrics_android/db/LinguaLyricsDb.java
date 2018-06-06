package habibkazemi.ir.lingualyrics_android.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import habibkazemi.ir.lingualyrics_android.api.ConnectivityInterceptor;
import habibkazemi.ir.lingualyrics_android.vo.Lyric;

/**
 * Created by habibkazemi on 5/29/18
 */

@Database(
        entities = {Lyric.class},
        version = 1,
        exportSchema = false
)
public abstract class LinguaLyricsDb extends RoomDatabase{
    public abstract LyricDao lyricDao();

    private static LinguaLyricsDb INSTANCE;
    public static LinguaLyricsDb getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (LinguaLyricsDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LinguaLyricsDb.class, "lyric_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
