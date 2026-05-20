package com.myaplicacion.mapano.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.myaplicacion.mapano.model.FotoLugar;

import java.util.List;

/**
 * DAO para las fotos tomadas por el usuario.
 * Operaciones: Crear, Leer y Eliminar (sin edición).
 */
@Dao
public interface FotoLugarDao {

    // ========================
    // CREATE
    // ========================

    @Insert
    long insertar(FotoLugar foto);

    // ========================
    // READ
    // ========================

    @Query("SELECT * FROM fotos_lugar ORDER BY fechaCaptura DESC")
    LiveData<List<FotoLugar>> obtenerTodas();

    @Query("SELECT * FROM fotos_lugar WHERE idPuntoInteres = :idPunto AND categoria = :categoria ORDER BY fechaCaptura DESC")
    LiveData<List<FotoLugar>> obtenerPorLugar(String idPunto, String categoria);

    @Query("SELECT * FROM fotos_lugar WHERE categoria = :categoria ORDER BY fechaCaptura DESC")
    LiveData<List<FotoLugar>> obtenerPorCategoria(String categoria);

    @Query("SELECT COUNT(*) FROM fotos_lugar WHERE idPuntoInteres = :idPunto AND categoria = :categoria")
    int contarFotosPorLugar(String idPunto, String categoria);

    // ========================
    // DELETE
    // ========================

    @Delete
    void eliminar(FotoLugar foto);

    @Query("DELETE FROM fotos_lugar WHERE id = :id")
    void eliminarPorId(long id);

    @Query("DELETE FROM fotos_lugar WHERE idPuntoInteres = :idPunto AND categoria = :categoria")
    void eliminarPorLugar(String idPunto, String categoria);

    @Query("DELETE FROM fotos_lugar")
    void eliminarTodas();
}

