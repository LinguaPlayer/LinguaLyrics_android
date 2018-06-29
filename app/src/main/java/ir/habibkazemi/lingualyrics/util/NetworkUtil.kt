package ir.habibkazemi.lingualyrics.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by habibkazemi on 5/21/18.
 */

object NetworkUtil {
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}
