package com.myaplicacion.mapano.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.myaplicacion.mapano.model.DeseoLugar;

import java.util.List;

/**
 * DAO para la Lista de Deseos del usuario.
 * CRUD COMPLETO: Crear, Leer, Actualizar, Eliminar.
 * Este es el CRUD obligatorio del proyecto.
 */
@Dao
public interface DeseoLugarDao {

    // ========================
    // CREATE (Alta)
    // ========================

    @Insert
    long insertar(DeseoLugar deseo);

    // ========================
    // READ (Consultas)
    // ========================

    @Query("SELECT * FROM deseos_lugar ORDER BY fechaAnadido DESC")
    LiveData<List<DeseoLugar>> obtenerTodos();

    @Query("SELECT * FROM deseos_lugar WHERE id = :id")
    LiveData<DeseoLugar> obtenerPorId(long id);

    @Query("SELECT * FROM deseos_lugar WHERE categoria = :categoria ORDER BY prioridad DESC")
    LiveData<List<DeseoLugar>> obtenerPorCategoria(String categoria);

    @Query("SELECT * FROM deseos_lugar WHERE visitado = 0 ORDER BY prioridad DESC")
    LiveData<List<DeseoLugar>> obtenerPendientes();

    @Query("SELECT * FROM deseos_lugar WHERE visitado = 1 ORDER BY fechaVisita DESC")
    LiveData<List<DeseoLugar>> obtenerVisitados();

    @Query("SELECT * FROM deseos_lugar WHERE engagementConfirmado = 1")
    LiveData<List<DeseoLugar>> obtenerConEngagement();

    @Query("SELECT COUNT(*) FROM deseos_lugar WHERE idPuntoInteres = :idPunto AND categoria = :categoria")
    int existeEnLista(long idPunto, String categoria);
    @Query("SELECT * FROM deseos_lugar WHERE idDeseoBackend = :idDeseoBackend LIMIT 1")
    DeseoLugar obtenerPorIdDeseoBackend(long idDeseoBackend);
    // ========================
    // UPDATE (Modificación)
    // ========================

    @Update
    void actualizar(DeseoLugar deseo);

    @Query("UPDATE deseos_lugar SET notaPersonal = :nota WHERE id = :id")
    void actualizarNota(long id, String nota);

    @Query("UPDATE deseos_lugar SET prioridad = :prioridad WHERE id = :id")
    void actualizarPrioridad(long id, int prioridad);

    @Query("UPDATE deseos_lugar SET visitado = 1, fechaVisita = :fechaVisita WHERE id = :id")
    void marcarComoVisitado(long id, long fechaVisita);

    @Query("UPDATE deseos_lugar SET engagementConfirmado = 1, distanciaAlLlegar = :distancia, fechaVisita = :fecha WHERE id = :id")
    void confirmarEngagement(long id, double distancia, long fecha);

    @Query("UPDATE deseos_lugar SET rutaFotoVisita = :rutaFoto WHERE id = :id")
    void guardarFotoVisita(long id, String rutaFoto);

    // ========================
    // DELETE (Baja)
    // ========================

    @Delete
    void eliminar(DeseoLugar deseo);

    @Query("DELETE FROM deseos_lugar WHERE id = :id")
    void eliminarPorId(long id);

    @Query("DELETE FROM deseos_lugar WHERE visitado = 1")
    void eliminarVisitados();

    @Query("DELETE FROM deseos_lugar")
    void eliminarTodos();
}

