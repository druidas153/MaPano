package com.myaplicacion.mapano.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myaplicacion.mapano.database.AppDatabase;
import com.myaplicacion.mapano.database.dao.FotoLugarDao;
import com.myaplicacion.mapano.model.FotoLugar;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para la galería de fotos de un lugar.
 */
public class GaleriaFotosViewModel extends AndroidViewModel {

    private final FotoLugarDao fotoLugarDao;
    private final ExecutorService executor;

    public GaleriaFotosViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(application);
        fotoLugarDao = db.fotoLugarDao();
        executor = Executors.newSingleThreadExecutor();
    }

    // ========================
    // READ
    // ========================

    public LiveData<List<FotoLugar>> getFotosPorLugar(String idPunto, String categoria) {
        return fotoLugarDao.obtenerPorLugar(idPunto, categoria);
    }

    public LiveData<List<FotoLugar>> getTodasLasFotos() {
        return fotoLugarDao.obtenerTodas();
    }

    // ========================
    // CREATE
    // ========================

    public void guardarFoto(FotoLugar foto) {
        executor.execute(() -> fotoLugarDao.insertar(foto));
    }

    // ========================
    // DELETE
    // ========================

    public void eliminarFoto(FotoLugar foto) {
        executor.execute(() -> fotoLugarDao.eliminar(foto));
    }

    public void eliminarFotoPorId(long id) {
        executor.execute(() -> fotoLugarDao.eliminarPorId(id));
    }
}
