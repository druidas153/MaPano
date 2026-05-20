package com.myaplicacion.mapano.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myaplicacion.mapano.model.Farmacia;

import java.util.List;

/**
 * DAO para farmacias. Solo lectura para el usuario.
 */
@Dao
public interface FarmaciaDao {

    @Query("SELECT * FROM farmacias")
    LiveData<List<Farmacia>> obtenerTodas();

    @Query("SELECT * FROM farmacias WHERE id = :id")
    LiveData<Farmacia> obtenerPorId(long id);

    @Query("SELECT * FROM farmacias WHERE esDeGuardia = 1")
    LiveData<List<Farmacia>> obtenerDeGuardia();

    @Query("SELECT * FROM farmacias WHERE estaAbierta = 1")
    LiveData<List<Farmacia>> obtenerAbiertas();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTodas(List<Farmacia> farmacias);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(Farmacia farmacia);

    @Query("DELETE FROM farmacias")
    void eliminarTodas();
}
