package com.myaplicacion.mapano.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myaplicacion.mapano.model.ParadaTaxi;

import java.util.List;

/**
 * DAO para paradas de taxi. Solo lectura para el usuario.
 */
@Dao
public interface ParadaTaxiDao {

    @Query("SELECT * FROM paradas_taxi")
    LiveData<List<ParadaTaxi>> obtenerTodas();

    @Query("SELECT * FROM paradas_taxi WHERE id = :id")
    LiveData<ParadaTaxi> obtenerPorId(long id);

    @Query("SELECT * FROM paradas_taxi WHERE estado = 'disponible'")
    LiveData<List<ParadaTaxi>> obtenerDisponibles();

    @Query("SELECT * FROM paradas_taxi WHERE tieneAdaptados = 1")
    LiveData<List<ParadaTaxi>> obtenerConAdaptados();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTodas(List<ParadaTaxi> paradas);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(ParadaTaxi parada);

    @Query("DELETE FROM paradas_taxi")
    void eliminarTodas();
    
    @Query("DELETE FROM paradas_taxi WHERE origenDatos = :origen")
    void eliminarPorOrigen(String origen);
}

