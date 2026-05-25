package com.myaplicacion.mapano.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myaplicacion.mapano.database.AppDatabase;
import com.myaplicacion.mapano.model.Evento;
import com.myaplicacion.mapano.model.Farmacia;
import com.myaplicacion.mapano.model.ParadaTaxi;
import com.myaplicacion.mapano.model.Restaurante;

import java.util.List;

/**
 * ViewModel para el mapa.
 * Proporciona los datos de todas las categorías al MapaFragment.
 */
public class MapaViewModel extends AndroidViewModel {

    private final LiveData<List<Restaurante>> restaurantes;
    private final LiveData<List<Evento>> eventos;
    private final LiveData<List<Farmacia>> farmacias;
    private final LiveData<List<ParadaTaxi>> paradasTaxi;

    public MapaViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(application);

        restaurantes = db.restauranteDao().obtenerTodos();
        eventos = db.eventoDao().obtenerTodos();
        farmacias = db.farmaciaDao().obtenerTodas();
        paradasTaxi = db.paradaTaxiDao().obtenerTodas();
    }

    public LiveData<List<Restaurante>> getRestaurantes() {
        return restaurantes;
    }

    public LiveData<List<Evento>> getEventos() {
        return eventos;
    }

    public LiveData<List<Farmacia>> getFarmacias() {
        return farmacias;
    }

    public LiveData<List<ParadaTaxi>> getParadasTaxi() {
        return paradasTaxi;
    }
}
