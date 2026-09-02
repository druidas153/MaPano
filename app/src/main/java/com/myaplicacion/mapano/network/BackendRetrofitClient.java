package com.myaplicacion.mapano.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.myaplicacion.mapano.BuildConfig;
public class BackendRetrofitClient {

    private static final String BASE_URL = BuildConfig.BACKEND_URL;

    private static Retrofit retrofit = null;

    public static Retrofit getInstance()
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static BackendApiService getApiService()
    {
        return getInstance().create(BackendApiService.class);
    }
}
