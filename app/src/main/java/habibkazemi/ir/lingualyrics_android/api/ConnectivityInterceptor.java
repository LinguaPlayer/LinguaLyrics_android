package habibkazemi.ir.lingualyrics_android.api;

import android.content.Context;

import java.io.IOException;

import habibkazemi.ir.lingualyrics_android.util.NetworkUtil;
import habibkazemi.ir.lingualyrics_android.util.NoConnectivityException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by habibkazemi on 5/21/18.
 */

public class ConnectivityInterceptor implements Interceptor {

    private Context mContext;

    public ConnectivityInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtil.INSTANCE.isOnline(mContext)) {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

}


