package habibkazemi.ir.lingualyrics_android;

import android.app.Application;
import android.os.Build;

import habibkazemi.ir.lingualyrics_android.Log.CrashReportingTree;
import timber.log.Timber;

public class LinguaLyricApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }
}
