package com.myaplicacion.mapano.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myaplicacion.mapano.database.AppDatabase;
import com.myaplicacion.mapano.database.dao.DeseoLugarDao;
import com.myaplicacion.mapano.model.DeseoLugar;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para la Lista de Deseos.
 * Gestiona el CRUD completo de DeseoLugar.
 */
public class ListaDeseosViewModel extends AndroidViewModel {

    private final DeseoLugarDao deseoLugarDao;
    private final ExecutorService executor;

    private final LiveData<List<DeseoLugar>> todosLosDeseos;
    private final LiveData<List<DeseoLugar>> deseosPendientes;
    private final LiveData<List<DeseoLugar>> deseosVisitados;

    public ListaDeseosViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(application);
        deseoLugarDao = db.deseoLugarDao();
        executor = Executors.newSingleThreadExecutor();

        todosLosDeseos = deseoLugarDao.obtenerTodos();
        deseosPendientes = deseoLugarDao.obtenerPendientes();
        deseosVisitados = deseoLugarDao.obtenerVisitados();
    }

    // ========================
    // READ (Consultas)
    // ========================

    public LiveData<List<DeseoLugar>> getTodosLosDeseos() {
        return todosLosDeseos;
    }

    public LiveData<List<DeseoLugar>> getDeseosPendientes() {
        return deseosPendientes;
    }

    public LiveData<List<DeseoLugar>> getDeseosVisitados() {
        return deseosVisitados;
    }

    public LiveData<List<DeseoLugar>> getDeseosPorCategoria(String categoria) {
        return deseoLugarDao.obtenerPorCategoria(categoria);
    }

    // ========================
    // CREATE (Alta)
    // ========================

    public void agregarDeseo(DeseoLugar deseo) {
        executor.execute(() -> deseoLugarDao.insertar(deseo));
    }

    // ========================
    // UPDATE (Modificación)
    // ========================

    public void actualizarDeseo(DeseoLugar deseo) {
        executor.execute(() -> deseoLugarDao.actualizar(deseo));
    }

    public void actualizarNota(long id, String nota) {
        executor.execute(() -> deseoLugarDao.actualizarNota(id, nota));
    }

    public void actualizarPrioridad(long id, int prioridad) {
        executor.execute(() -> deseoLugarDao.actualizarPrioridad(id, prioridad));
    }

    public void marcarComoVisitado(long id) {
        executor.execute(() -> deseoLugarDao.marcarComoVisitado(id, System.currentTimeMillis()));
    }

    // ========================
    // DELETE (Baja)
    // ========================

    public void eliminarDeseo(DeseoLugar deseo) {
        executor.execute(() -> deseoLugarDao.eliminar(deseo));
    }

    public void eliminarPorId(long id) {
        executor.execute(() -> deseoLugarDao.eliminarPorId(id));
    }

    // ========================
    // UTILIDADES
    // ========================

    public void verificarSiExiste(long idPunto, String categoria, OnExisteCallback callback) {
        executor.execute(() -> {
            int count = deseoLugarDao.existeEnLista(idPunto, categoria);
            callback.onResult(count > 0);
        });
    }

    public interface OnExisteCallback {
        void onResult(boolean existe);
    }
}

