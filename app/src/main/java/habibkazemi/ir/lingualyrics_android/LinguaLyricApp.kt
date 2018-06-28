package habibkazemi.ir.lingualyrics_android

import android.app.Application

import habibkazemi.ir.lingualyrics_android.Log.CrashReportingTree
import timber.log.Timber

class LinguaLyricApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }
}
