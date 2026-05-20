package com.myaplicacion.mapano;

import android.app.Application;

import com.myaplicacion.mapano.database.DatabaseSeeder;

import org.osmdroid.config.Configuration;

/**
 * Clase Application de MaPaño.
 * Se ejecuta al iniciar la app, antes de cualquier Activity.
 * Inicializa osmdroid y los datos mock.
 */
public class MaPanoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Configurar osmdroid (obligatorio)
        Configuration.getInstance().setUserAgentValue(getPackageName());

        // Insertar datos mock si la BD está vacía
        DatabaseSeeder seeder = new DatabaseSeeder(this);
        seeder.seedIfEmpty();
    }
}
