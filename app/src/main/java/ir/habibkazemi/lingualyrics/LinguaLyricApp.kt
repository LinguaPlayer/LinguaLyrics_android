package ir.habibkazemi.lingualyrics

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

import ir.habibkazemi.lingualyrics.Log.CrashReportingTree
import timber.log.Timber

class LinguaLyricApp : Application(), LifecycleObserver {

    companion object {
        var appIsVisible: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun someActivityStarted() {
        appIsVisible = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun someActivityPaused() {
        appIsVisible = false
    }
}
