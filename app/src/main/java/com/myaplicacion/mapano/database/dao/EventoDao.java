package com.myaplicacion.mapano.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myaplicacion.mapano.model.Evento;

import java.util.List;

/**
 * DAO para eventos. Solo lectura para el usuario.
 * Los datos se insertan desde el Seeder (mock) o desde la API (futuro).
 */
@Dao
public interface EventoDao {

    @Query("SELECT * FROM eventos")
    LiveData<List<Evento>> obtenerTodos();

    @Query("SELECT * FROM eventos WHERE id = :id")
    LiveData<Evento> obtenerPorId(long id);

    @Query("SELECT * FROM eventos WHERE tipoEvento = :tipo")
    LiveData<List<Evento>> obtenerPorTipo(String tipo);

    @Query("SELECT * FROM eventos WHERE esGratuito = 1")
    LiveData<List<Evento>> obtenerGratuitos();

    @Query("SELECT * FROM eventos WHERE esPatrocinado = 1 ORDER BY prioridadMapa DESC")
    LiveData<List<Evento>> obtenerPatrocinados();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTodos(List<Evento> eventos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(Evento evento);

    @Query("DELETE FROM eventos")
    void eliminarTodos();

    @Query("DELETE FROM eventos WHERE origenDatos = :origen")
    void eliminarPorOrigen(String origen);
}
