package com.myaplicacion.mapano.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myaplicacion.mapano.database.AppDatabase;
import com.myaplicacion.mapano.database.dao.DeseoLugarDao;
import com.myaplicacion.mapano.model.DeseoLugar;
import com.myaplicacion.mapano.network.BackendApiService;
import com.myaplicacion.mapano.network.BackendRetrofitClient;
import com.myaplicacion.mapano.network.model.DeseoNotaPeticion;
import com.myaplicacion.mapano.network.model.PuntoInteresPeticion;
import com.myaplicacion.mapano.network.model.PuntoInteresRespuesta;
import com.myaplicacion.mapano.network.model.DeseoPeticion;
import com.myaplicacion.mapano.network.model.DeseoRespuesta;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private final BackendApiService backendApiService;
    private final long usuarioId;

    public ListaDeseosViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(application);
        deseoLugarDao = db.deseoLugarDao();
        executor = Executors.newSingleThreadExecutor();

        backendApiService = BackendRetrofitClient.getApiService();

        SharedPreferences preferencias = application.getSharedPreferences("sesion", Application.MODE_PRIVATE);

        usuarioId = preferencias.getLong("usuarioId", -1);

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

    public void consultarDeseosBackend(OnConsultaDeseosCallback callback)
    {
        if (usuarioId == -1)
        {
            callback.onError("No hay un usuario con sesión iniciada");
            return;
        }

        backendApiService.listarDeseosUsuario(usuarioId)
                .enqueue(new Callback<List<DeseoRespuesta>>()
                {
                    @Override
                    public void onResponse(
                            Call<List<DeseoRespuesta>> call,
                            Response<List<DeseoRespuesta>> response)
                    {
                        if (!response.isSuccessful() || response.body() == null)
                        {
                            callback.onError("No se pudo consultar la lista");
                            return;
                        }

                        callback.onCorrecto(response.body());
                    }

                    @Override
                    public void onFailure(
                            Call<List<DeseoRespuesta>> call,
                            Throwable t)
                    {
                        callback.onError("No se pudo conectar con el servidor");
                    }
                });
    }

    public void sincronizarDeseosBackend(OnOperacionCallback callback)
    {
        consultarDeseosBackend(new OnConsultaDeseosCallback()
        {
            @Override
            public void onCorrecto(List<DeseoRespuesta> deseosBackend)
            {
                executor.execute(() ->
                {
                    for (DeseoRespuesta respuesta : deseosBackend)
                    {
                        DeseoLugar deseoLocal =
                                deseoLugarDao.obtenerPorIdDeseoBackend(
                                        respuesta.getId());

                        if (deseoLocal == null)
                        {
                            deseoLocal = convertirADeseoLugar(respuesta);
                            deseoLugarDao.insertar(deseoLocal);
                        }
                        else
                        {
                            actualizarDesdeBackend(
                                    deseoLocal,
                                    respuesta);

                            deseoLugarDao.actualizar(deseoLocal);
                        }
                    }

                    new Handler(Looper.getMainLooper()).post(
                            callback::onCorrecto);
                });
            }

            @Override
            public void onError(String mensaje)
            {
                callback.onError(mensaje);
            }
        });
    }

    private DeseoLugar convertirADeseoLugar(DeseoRespuesta respuesta)
    {
        String categoria = respuesta.getTipoPunto().toLowerCase();

        DeseoLugar deseo = new DeseoLugar(
                        respuesta.getPuntoInteresId(),
                        categoria,
                        respuesta.getNombrePunto(),
                        respuesta.getLatitud(),
                        respuesta.getLongitud());

        deseo.setIdPuntoBackend(respuesta.getPuntoInteresId());

        deseo.setIdDeseoBackend(respuesta.getId());

        deseo.setNotaPersonal(respuesta.getNota());

        deseo.setVisitado(respuesta.isVisitado());

        return deseo;
    }

    private void actualizarDesdeBackend(DeseoLugar deseoLocal,DeseoRespuesta respuesta)
    {
        deseoLocal.setNombreLugar(
                respuesta.getNombrePunto());

        deseoLocal.setCategoria(
                respuesta.getTipoPunto().toLowerCase());

        deseoLocal.setLatitud(
                respuesta.getLatitud());

        deseoLocal.setLongitud(
                respuesta.getLongitud());

        deseoLocal.setIdPuntoBackend(
                respuesta.getPuntoInteresId());

        deseoLocal.setIdDeseoBackend(
                respuesta.getId());

        deseoLocal.setNotaPersonal(
                respuesta.getNota());

        deseoLocal.setVisitado(
                respuesta.isVisitado());
    }
    // ========================
    // CREATE (Alta)
    // ========================

    public void agregarDeseo(DeseoLugar deseo) {
        executor.execute(() -> deseoLugarDao.insertar(deseo));
    }

    public void agregarDeseoSincronizado(DeseoLugar deseo,String descripcion,OnOperacionCallback callback)
    {
        if (usuarioId == -1)
        {
            callback.onError("No hay un usuario con sesión iniciada");
            return;
        }

        String tipoBackend = deseo.getCategoria().toUpperCase();

        PuntoInteresPeticion puntoPeticion = new PuntoInteresPeticion(
                        deseo.getNombreLugar(),
                        descripcion,
                        deseo.getLatitud(),
                        deseo.getLongitud(),
                        "",
                        tipoBackend);

        backendApiService.guardarPunto(puntoPeticion)
                .enqueue(new Callback<PuntoInteresRespuesta>()
                {
                    @Override
                    public void onResponse(
                            Call<PuntoInteresRespuesta> call,
                            Response<PuntoInteresRespuesta> response)
                    {
                        if (!response.isSuccessful() || response.body() == null)
                        {
                            callback.onError("No se pudo guardar el punto");
                            return;
                        }

                        long idPuntoBackend = response.body().getId();

                        crearDeseoBackend(
                                deseo,
                                idPuntoBackend,
                                callback);
                    }

                    @Override
                    public void onFailure(
                            Call<PuntoInteresRespuesta> call,
                            Throwable t)
                    {
                        callback.onError("No se pudo conectar con el servidor");
                    }
                });
    }

    private void crearDeseoBackend(DeseoLugar deseo,long idPuntoBackend,OnOperacionCallback callback)
    {
        DeseoPeticion peticion = new DeseoPeticion(
                        usuarioId,
                        idPuntoBackend,
                        deseo.getNotaPersonal());

        backendApiService.crearDeseo(peticion)
                .enqueue(new Callback<DeseoRespuesta>()
                {
                    @Override
                    public void onResponse(
                            Call<DeseoRespuesta> call,
                            Response<DeseoRespuesta> response)
                    {
                        if (!response.isSuccessful() || response.body() == null)
                        {
                            callback.onError("No se pudo crear el deseo");
                            return;
                        }

                        deseo.setIdPuntoBackend(idPuntoBackend);
                        deseo.setIdDeseoBackend(response.body().getId());

                        executor.execute(() ->
                        {
                            deseoLugarDao.insertar(deseo);
                            new Handler(Looper.getMainLooper()).post(
                                    callback::onCorrecto);
                        });
                    }

                    @Override
                    public void onFailure(Call<DeseoRespuesta> call,Throwable t)
                    {
                        callback.onError("No se pudo conectar con el servidor");
                    }
                });
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

    public void actualizarDeseoSincronizado(
            DeseoLugar deseo,
            String nuevaNota,
            int nuevaPrioridad,
            OnOperacionCallback callback)
    {
        long idDeseoBackend = deseo.getIdDeseoBackend();

        if (idDeseoBackend <= 0)
        {
            callback.onError("El deseo no está sincronizado con el servidor");
            return;
        }

        DeseoNotaPeticion peticion = new DeseoNotaPeticion(nuevaNota);

        backendApiService.actualizarNotaDeseo(
                        idDeseoBackend,
                        peticion)
                .enqueue(new Callback<DeseoRespuesta>()
                {
                    @Override
                    public void onResponse(Call<DeseoRespuesta> call,Response<DeseoRespuesta> response)
                    {
                        if (!response.isSuccessful() || response.body() == null)
                        {
                            callback.onError(
                                    "No se pudo actualizar la nota");
                            return;
                        }

                        executor.execute(() ->
                        {
                            deseo.setNotaPersonal(response.body().getNota());

                            deseo.setPrioridad(nuevaPrioridad);

                            deseoLugarDao.actualizar(deseo);

                            new Handler(Looper.getMainLooper()).post(callback::onCorrecto);
                        });
                    }

                    @Override
                    public void onFailure(Call<DeseoRespuesta> call,Throwable t)
                    {
                        callback.onError("No se pudo conectar con el servidor");
                    }
                });
    }
    // ========================
    // DELETE (Baja)
    // ========================

    public void eliminarDeseo(DeseoLugar deseo) {
        executor.execute(() -> deseoLugarDao.eliminar(deseo));
    }

    public void eliminarDeseoSincronizado(DeseoLugar deseo,OnOperacionCallback callback)
    {
        long idDeseoBackend = deseo.getIdDeseoBackend();

        if (idDeseoBackend <= 0)
        {
            callback.onError("El deseo no está sincronizado con el servidor");
            return;
        }

        backendApiService.eliminarDeseo(idDeseoBackend)
                .enqueue(new Callback<Void>()
                {
                    @Override
                    public void onResponse(Call<Void> call,Response<Void> response)
                    {
                        if (!response.isSuccessful() && response.code() != 404)
                        {
                            callback.onError("No se pudo eliminar el deseo");
                            return;
                        }

                        executor.execute(() ->
                        {
                            deseoLugarDao.eliminar(deseo);

                            new Handler(Looper.getMainLooper()).post(callback::onCorrecto);
                        });
                    }

                    @Override
                    public void onFailure(Call<Void> call,Throwable t)
                    {
                        callback.onError("No se pudo conectar con el servidor");
                    }
                });
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
    public interface OnOperacionCallback
    {
        void onCorrecto();

        void onError(String mensaje);
    }

    public interface OnConsultaDeseosCallback
    {
        void onCorrecto(List<DeseoRespuesta> deseos);

        void onError(String mensaje);
    }
}

