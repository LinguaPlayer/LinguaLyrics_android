package ir.habibkazemi.lingualyrics.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import ir.habibkazemi.lingualyrics.LinguaLyricApp

import timber.log.Timber
import ir.habibkazemi.lingualyrics.ui.main.MainActivity
import android.R.id.edit
import androidx.core.content.edit
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ir.habibkazemi.lingualyrics.ui.lyric.LyricsFragment
import ir.habibkazemi.lingualyrics.util.Constants
import ir.habibkazemi.lingualyrics.util.Constants.UPDATE_LYRICS_ACTION


class MyBroadcastReceiver : BroadcastReceiver() {
    internal object BroadcastTypes {
        val SPOTIFY_PACKAGE = "com.spotify.music"
        val PLAYBACK_STATE_CHANGED = "$SPOTIFY_PACKAGE.playbackstatechanged"
        val QUEUE_CHANGED = "$SPOTIFY_PACKAGE.queuechanged"
        val METADATA_CHANGED = "$SPOTIFY_PACKAGE.metadatachanged"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This is sent with all broadcasts, regardless of type. The value is taken from
        // System.currentTimeMillis(), which you can compare to in order to determine how
        // old the event is.
        val timeSentInMs = intent.getLongExtra("timeSent", 0L)

        val action = intent.action

        when (action) {
            BroadcastTypes.METADATA_CHANGED -> {
                val trackId = intent.getStringExtra("id")
                val artistName = intent.getStringExtra("artist")
                val album = intent.getStringExtra("album")
                val trackName = intent.getStringExtra("track")
                val trackLengthInSec = intent.getIntExtra("length", 0)

                val i = Intent(context, MainActivity::class.java)

                val currentMusicPref = context.getSharedPreferences("current_music", Context.MODE_PRIVATE)

                currentMusicPref.edit {
                    putString("artist", artistName)
                    putString("title", trackName)
                }

                if (LinguaLyricApp.appIsVisible) {
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    Timber.d("Open the activity")
                    val intent = Intent(Constants.UPDATE_LYRICS_ACTION)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                }

                Timber.d("METADATA_CHANGED $artistName $trackName")
                // Do something with extracted information...
            }

            BroadcastTypes.PLAYBACK_STATE_CHANGED -> {
                val playing = intent.getBooleanExtra("playing", false)
                val positionInMs = intent.getIntExtra("playbackPosition", 0)
                Timber.d("PLAYBACK_STATE_CHANGED$playing ")
                // Do something with extracted information
            }
            BroadcastTypes.QUEUE_CHANGED -> // Sent only as a notification, your app may want to respond accordingly.
                Timber.d("QEUE_CHANGED")
        }
    }
}


