package com.myaplicacion.mapano.network;

//import com.myaplicacion.mapano.network.model.EventoApiResponse;
//import com.myaplicacion.mapano.network.model.FarmaciaApiResponse;
import com.myaplicacion.mapano.network.model.EventoApiResponse;
import com.myaplicacion.mapano.network.model.FarmaciaApiResponse;
import com.myaplicacion.mapano.network.model.RestauranteApiResponse;
import com.myaplicacion.mapano.network.model.TaxiApiResponse;
//import com.myaplicacion.mapano.network.model.TaxiApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface de Retrofit que define las llamadas a la API de Zaragoza.
 */
public interface ZaragozaApiService {

    @GET("restaurante.json")
    Call<RestauranteApiResponse> obtenerRestaurantes(
            @Query("rows") int filas,
            @Query("start") int inicio,
            @Query("srsname") String sistemaReferencia
    );

    @GET("cultura/evento/list.json")
    Call<EventoApiResponse> obtenerEventos(
            @Query("rows") int filas,
            @Query("start") int inicio,
            @Query("srsname") String sistemaReferencia
    );

    @GET("farmacia.json")
    Call<FarmaciaApiResponse> obtenerFarmacias(
            @Query("rows") int filas,
            @Query("start") int inicio,
            @Query("srsname") String sistemaReferencia
    );
    //@GET("urbanismo-infraestructuras/equipamiento/parada-taxi/itinerantes.json")
    @GET("urbanismo-infraestructuras/equipamiento/parada-taxi.json")
    Call<TaxiApiResponse> obtenerParadasTaxi(
            @Query("rows") int filas,
            @Query("start") int inicio,
            @Query("srsname") String sistemaReferencia
            //@Query("estado") String estado
    );


}

