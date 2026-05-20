package com.myaplicacion.mapano.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.myaplicacion.mapano.database.dao.DeseoLugarDao;
import com.myaplicacion.mapano.database.dao.EventoDao;
import com.myaplicacion.mapano.database.dao.FarmaciaDao;
import com.myaplicacion.mapano.database.dao.FotoLugarDao;
import com.myaplicacion.mapano.database.dao.ParadaTaxiDao;
import com.myaplicacion.mapano.database.dao.RestauranteDao;
import com.myaplicacion.mapano.model.DeseoLugar;
import com.myaplicacion.mapano.model.Evento;
import com.myaplicacion.mapano.model.Farmacia;
import com.myaplicacion.mapano.model.FotoLugar;
import com.myaplicacion.mapano.model.ParadaTaxi;
import com.myaplicacion.mapano.model.Restaurante;

/**
 * Base de datos Room de MaPaño.
 * Contiene todas las tablas de la aplicación.
 * Usa el patrón Singleton para una única instancia.
 */
@Database(
        entities = {
                Restaurante.class,
                Evento.class,
                Farmacia.class,
                ParadaTaxi.class,
                DeseoLugar.class,
                FotoLugar.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    // ========================
    // DAOs
    // ========================

    public abstract RestauranteDao restauranteDao();
    public abstract EventoDao eventoDao();
    public abstract FarmaciaDao farmaciaDao();
    public abstract ParadaTaxiDao paradaTaxiDao();
    public abstract DeseoLugarDao deseoLugarDao();
    public abstract FotoLugarDao fotoLugarDao();

    // ========================
    // SINGLETON
    // ========================

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "mapano_database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
