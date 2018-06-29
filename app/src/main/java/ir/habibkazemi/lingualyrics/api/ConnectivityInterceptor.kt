package ir.habibkazemi.lingualyrics.api

import android.content.Context

import java.io.IOException

import ir.habibkazemi.lingualyrics.util.NetworkUtil
import ir.habibkazemi.lingualyrics.util.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by habibkazemi on 5/21/18.
 */

class ConnectivityInterceptor(private val mContext: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtil.isOnline(mContext)) {
            throw NoConnectivityException()
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

}


