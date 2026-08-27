package com.myaplicacion.mapano.repository;

import android.content.Context;
import android.util.Log;

import com.myaplicacion.mapano.database.AppDatabase;
import com.myaplicacion.mapano.database.dao.EventoDao;
import com.myaplicacion.mapano.database.dao.FarmaciaDao;
import com.myaplicacion.mapano.database.dao.ParadaTaxiDao;
import com.myaplicacion.mapano.database.dao.RestauranteDao;
import com.myaplicacion.mapano.model.DatosComunes;
import com.myaplicacion.mapano.model.Evento;
import com.myaplicacion.mapano.model.Farmacia;
import com.myaplicacion.mapano.model.ParadaTaxi;
import com.myaplicacion.mapano.model.Restaurante;
import com.myaplicacion.mapano.network.RetrofitClient;
import com.myaplicacion.mapano.network.ZaragozaApiService;
import com.myaplicacion.mapano.network.model.EventoApiResponse;
import com.myaplicacion.mapano.network.model.FarmaciaApiResponse;
import com.myaplicacion.mapano.network.model.RestauranteApiResponse;
import com.myaplicacion.mapano.network.model.TaxiApiResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repositorio que conecta la API de Zaragoza con la base de datos local (Room).
 * Descarga datos de la API y los guarda en Room.
 * Si no hay conexión, se usan los datos locales (mock o cacheados).
 */
public class DataRepository {

    private static final String TAG = "DataRepository";
    private static final int MAX_RESULTADOS = 50;

    private final ZaragozaApiService apiService;
    private final RestauranteDao restauranteDao;
    private final EventoDao eventoDao;
    private final FarmaciaDao farmaciaDao;
    private final ParadaTaxiDao paradaTaxiDao;
    private final ExecutorService executor;

    public DataRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        apiService = RetrofitClient.getApiService();
        restauranteDao = db.restauranteDao();
        eventoDao = db.eventoDao();
        farmaciaDao = db.farmaciaDao();
        paradaTaxiDao = db.paradaTaxiDao();
        executor = Executors.newSingleThreadExecutor();
    }

    // ========================
    // SINCRONIZAR RESTAURANTES
    // ========================

    public void sincronizarRestaurantes() {
        apiService.obtenerRestaurantes(MAX_RESULTADOS,0, "wgs84").enqueue(new Callback<RestauranteApiResponse>() {
            @Override
            public void onResponse(Call<RestauranteApiResponse> call, Response<RestauranteApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Restaurantes recibidos: " + response.body().getResult().size());
                    executor.execute(() -> {
                        // Evitar duplicados
                        restauranteDao.eliminarPorOrigen("API_ZARAGOZA");
                        for (RestauranteApiResponse.RestauranteApi api : response.body().getResult()) {
                            if (api.getGeometry() == null) //continue;
                            {
                                Log.w(TAG, "Restaurante sin coordenadas: " + api.getTitle());
                                continue;
                            }

                            //Log.d(TAG, "Guardando: " + api.getTitle() +
                            //        " lat=" + api.getGeometry().getLatitud() +
                            //        " lon=" + api.getGeometry().getLongitud());

/*
                            Log.d(TAG, "Restaurante: " + api.getTitle() +
                                    " Lat/Y=" + api.getGeometry().getLatitud() + " Lon/X=" + api.getGeometry().getLongitud());
*/
                            Restaurante restaurante = new Restaurante();
                            DatosComunes datos = new DatosComunes(
                                    api.getTitle(),
                                    api.getDescription() != null ? api.getDescription() : "",
                                    api.getGeometry().getLatitud(),
                                    api.getGeometry().getLongitud(),
                                    api.getAddress() != null ? api.getAddress() : ""
                            );
                            datos.setOrigenDatos("API_ZARAGOZA");
                            restaurante.setDatosComunes(datos);
                            restaurante.setTelefono(api.getPhone() != null ? api.getPhone() : "");
                            restaurante.setTipoCocina("Variada");
                            restaurante.setTenedores(api.getTenedores() != null ? Integer.parseInt(api.getTenedores()) : 3);

                            restauranteDao.insertar(restaurante);
                        }
                        Log.d(TAG, "Restaurantes guardados en Room");
                    });
                }
            }

            @Override
            public void onFailure(Call<RestauranteApiResponse> call, Throwable t) {
                Log.e(TAG, "Error al obtener restaurantes: " + t.getMessage());
            }
        });
    }

    // ========================
    // SINCRONIZAR EVENTOS
    // ========================

    public void sincronizarEventos() {
        apiService.obtenerEventos(MAX_RESULTADOS, 0,"wgs84").enqueue(new Callback<EventoApiResponse>() {
            @Override
            public void onResponse(Call<EventoApiResponse> call, Response<EventoApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("sincronizarEventos", "Eventos recibidos: " + response.body().getResult().size());
                    executor.execute(() -> {
                        eventoDao.eliminarPorOrigen("API_ZARAGOZA");
                        for (EventoApiResponse.EventoApi api : response.body().getResult()) {
                            if (api.getGeometry() == null) continue;

                            Evento evento = new Evento();
                            DatosComunes datos = new DatosComunes(
                                    api.getTitle(),
                                    api.getDescription() != null ? api.getDescription() : "",
                                    api.getGeometry().getLatitud(),
                                    api.getGeometry().getLongitud(),
                                    api.getLugar() != null ? api.getLugar() : ""
                            );
                            datos.setOrigenDatos("API_ZARAGOZA");

                            Log.d(TAG, "Evento: " + api.getTitle() +
                                    " Lat/Y=" + api.getGeometry().getLatitud() + " Lon/X=" + api.getGeometry().getLongitud());

                            evento.setDatosComunes(datos);
                            evento.setTipoEvento(api.getTematica() != null ? api.getTematica() : "Cultural");
                            evento.setFechaInicio(api.getStartDate() != null ? api.getStartDate() : "");
                            evento.setFechaFin(api.getEndDate() != null ? api.getEndDate() : "");
                            evento.setHoraInicio("");
                            evento.setHoraFin("");
                            evento.setOrganizador("Ayuntamiento de Zaragoza");
                            evento.setPrecio(0);
                            Log.d("Evento",evento.toString());
                            eventoDao.insertar(evento);
                        }
                        Log.d(TAG, "Eventos guardados en Room");
                    });
                }
            }

            @Override
            public void onFailure(Call<EventoApiResponse> call, Throwable t) {
                Log.e(TAG, "Error al obtener eventos: " + t.getMessage());
            }
        });
    }

    // ========================
    // SINCRONIZAR FARMACIAS
    // ========================

    public void sincronizarFarmacias() {
        apiService.obtenerFarmacias(MAX_RESULTADOS, 0,"wgs84").enqueue(new Callback<FarmaciaApiResponse>() {
            @Override
            public void onResponse(Call<FarmaciaApiResponse> call, Response<FarmaciaApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("sincronizarFarmacias", "Farmacias recibidas: " + response.body().getResult().size());
                    executor.execute(() -> {
                        farmaciaDao.eliminarPorOrigen("API_ZARAGOZA");
                        for (FarmaciaApiResponse.FarmaciaApi api : response.body().getResult()) {
                            if (api.getGeometry() == null) continue;

                            Farmacia farmacia = new Farmacia();
                            DatosComunes datos = new DatosComunes(
                                    api.getTitle(),
                                    "",
                                    api.getGeometry().getLatitud(),
                                    api.getGeometry().getLongitud(),
                                    api.getStreetAddress() != null ? api.getStreetAddress() : ""
                            );
                            datos.setOrigenDatos("API_ZARAGOZA");
                            Log.d(TAG, "Farmacia: " + api.getTitle() +
                                    " Lat/Y=" + api.getGeometry().getLatitud() + " Lon/X=" + api.getGeometry().getLongitud());

                            farmacia.setDatosComunes(datos);
                            farmacia.setTelefono(api.getPhone() != null ? api.getPhone() : "");
                            farmacia.setHorarioApertura(api.getHorario() != null ? api.getHorario() : "");
                            farmacia.setHorarioCierre("");
                            farmacia.setEsDeGuardia(true);

                            farmaciaDao.insertar(farmacia);
                        }
                        Log.d(TAG, "Farmacias guardadas en Room");
                    });
                }
            }

            @Override
            public void onFailure(Call<FarmaciaApiResponse> call, Throwable t) {
                Log.e(TAG, "Error al obtener farmacias: " + t.getMessage());
            }
        });
    }

    // ========================
    // SINCRONIZAR TAXIS
    // ========================

    public void sincronizarParadasTaxi() {
        apiService.obtenerParadasTaxi(MAX_RESULTADOS, 0,"wgs84").enqueue(new Callback<TaxiApiResponse>() {
            @Override
            public void onResponse(Call<TaxiApiResponse> call, Response<TaxiApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Paradas taxi recibidas: " + response.body().getResult().size());
                    executor.execute(() -> {
                        paradaTaxiDao.eliminarPorOrigen("API_ZARAGOZA");
                        for (TaxiApiResponse.TaxiApi api : response.body().getResult()) {
                            if (api.getGeometry() == null) continue;
                            Log.d("sincronizarParadasTaxi", "Guardando parada taxi: " + api.getTitle() +
                                    " Lat=" + api.getGeometry().getLatitud() + " Lon=" + api.getGeometry().getLongitud());
                            ParadaTaxi parada = new ParadaTaxi();
                            DatosComunes datos = new DatosComunes(
                                    "Parada "+api.getTitle(),
                                    "",
                                    api.getGeometry().getLatitud(),
                                    api.getGeometry().getLongitud(),
                                    api.getStreetAddress() != null ? api.getStreetAddress() : ""
                            );
                            datos.setOrigenDatos("API_ZARAGOZA");
                            Log.d(TAG, "ParadaTaxi: " + api.getTitle() +
                                    " Lat/Y=" + api.getGeometry().getLatitud() + " Lon/X=" + api.getGeometry().getLongitud());

                            parada.setDatosComunes(datos);
                            parada.setCapacidadTotal(api.getNumPlazas());
                            parada.setTaxisDisponibles(6);
                            parada.setTieneAdaptados(false);
                            parada.setEstado("disponible");


                            paradaTaxiDao.insertar(parada);
                        }
                        Log.d(TAG, "Paradas taxi guardadas en Room");
                    });
                }
            }

            @Override
            public void onFailure(Call<TaxiApiResponse> call, Throwable t) {
                Log.e(TAG, "Error al obtener paradas taxi: " + t.getMessage());
            }
        });
    }

    // ========================
    // SINCRONIZAR TODO
    // ========================

    public void sincronizarTodo() {
        sincronizarRestaurantes();
        sincronizarEventos();
        sincronizarFarmacias();
        sincronizarParadasTaxi();
    }
}
