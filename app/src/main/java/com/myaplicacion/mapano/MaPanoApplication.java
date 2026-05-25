package com.myaplicacion.mapano;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.myaplicacion.mapano.database.DatabaseSeeder;
import com.myaplicacion.mapano.repository.DataRepository;

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
        // Si hay conexión a Internet, sincronizar con la API de Zaragoza
        if (hayConexion()) {
            DataRepository repository = new DataRepository(this);
            repository.sincronizarTodo();
        }
    }

    /**
     * Verifica si hay conexión a Internet.
     */
    private boolean hayConexion() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }
}
