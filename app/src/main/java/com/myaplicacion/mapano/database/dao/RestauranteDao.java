package com.myaplicacion.mapano.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myaplicacion.mapano.model.Restaurante;

import java.util.List;
/**
 * DAO para restaurantes. Solo lectura para el usuario.
 * Los datos se insertan desde el Seeder (mock) o desde la API (futuro).
 */

@Dao
public interface RestauranteDao {

    @Query("SELECT * FROM restaurantes")
    LiveData<List<Restaurante>> obtenerTodos();

    @Query("SELECT * FROM restaurantes WHERE id = :id")
    LiveData<Restaurante> obtenerPorId(long id);

    @Query("SELECT * FROM restaurantes WHERE esPremium = 1 ORDER BY prioridadMapa DESC")
    LiveData<List<Restaurante>> obtenerPremium();

    @Query("SELECT * FROM restaurantes WHERE tipoCocina = :tipo")
    LiveData<List<Restaurante>> obtenerPorTipoCocina(String tipo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTodos(List<Restaurante> restaurantes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertar(Restaurante restaurante);

    @Query("DELETE FROM restaurantes")
    void eliminarTodos();

    @Query("SELECT COUNT(*) FROM restaurantes")
    int contarRestaurantes();


}
