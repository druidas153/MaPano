package com.myaplicacion.mapano.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton de Retrofit para conectar con la API de datos abiertos de Zaragoza.
 */
public class RetrofitClient {

    private static final String BASE_URL = "https://www.zaragoza.es/sede/servicio/";

    private static Retrofit retrofit = null;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ZaragozaApiService getApiService() {
        return getInstance().create(ZaragozaApiService.class);
    }
}
