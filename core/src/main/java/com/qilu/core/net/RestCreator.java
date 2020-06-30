package com.qilu.core.net;

import com.qilu.core.app.ConfigKeys;
import com.qilu.core.app.Qilu;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestCreator {

    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }

    private static final class OKHttpHolder{
        private static final int TiME_OUT=600;
        private static final OkHttpClient.Builder BUILDER=new OkHttpClient.Builder();
        private static final OkHttpClient OK_HTTP_CLIENT= BUILDER
                .connectTimeout(TiME_OUT,TimeUnit.SECONDS)
                .readTimeout(TiME_OUT,TimeUnit.SECONDS)
                .writeTimeout(TiME_OUT,TimeUnit.SECONDS)
                .build();
    }
    private static final class RetrofitHolder {
        private static final String BASE_URL = Qilu.getConfiguration(ConfigKeys.API_HOST);
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
    private static final class RestServiceHolder{
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }
}
