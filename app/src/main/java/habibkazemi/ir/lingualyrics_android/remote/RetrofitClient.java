package habibkazemi.ir.lingualyrics_android.remote;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by habibkazemi on 5/21/18.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context, String baseUrl){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(new ConnectivityInterceptor(context))
                            .build())
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;

    }
}

