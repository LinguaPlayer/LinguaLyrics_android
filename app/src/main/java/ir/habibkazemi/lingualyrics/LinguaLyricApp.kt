package ir.habibkazemi.lingualyrics

import android.app.Application

import ir.habibkazemi.lingualyrics.Log.CrashReportingTree
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
