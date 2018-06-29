package ir.habibkazemi.lingualyrics.api

import android.content.Context

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by habibkazemi on 5/21/18.
 */

object RetrofitClient {

    @Volatile private var retrofit: Retrofit? = null


    fun getClient(context: Context, baseUrl: String): Retrofit =
            retrofit ?: synchronized(this){
                retrofit ?: buildClient(context, baseUrl).also { retrofit = it }
            }

    private fun buildClient(context: Context, baseUrl: String) =
        Retrofit.Builder()
                .client(OkHttpClient.Builder()
                        //                                .readTimeout(60, TimeUnit.SECONDS)
                        //                                .connectTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(ConnectivityInterceptor(context))
                        .build())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
}

