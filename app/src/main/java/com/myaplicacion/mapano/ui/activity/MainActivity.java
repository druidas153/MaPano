package com.myaplicacion.mapano.ui.activity;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.myaplicacion.mapano.R;
import com.myaplicacion.mapano.ui.fragment.MapaFragment;

/**
 * Activity principal de MaPaño.
 * Contiene el mapa y la barra inferior de categorías.
 */
public class MainActivity extends AppCompatActivity {

    private MapaFragment mapaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Crear e insertar el MapaFragment
        mapaFragment = new MapaFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, mapaFragment)
                .commit();

        // Configurar la barra inferior de categorías
        configurarBarraCategorias();
    }

    /**
     * Configura los listeners de la barra inferior.
     */
    private void configurarBarraCategorias() {
        BottomNavigationView barraCategoria = findViewById(R.id.barraCategoria);

        barraCategoria.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.cat_restaurantes) {
                mapaFragment.cambiarCategoria("restaurante");
                return true;
            } else if (id == R.id.cat_eventos) {
                mapaFragment.cambiarCategoria("evento");
                return true;
            } else if (id == R.id.cat_farmacias) {
                mapaFragment.cambiarCategoria("farmacia");
                return true;
            } else if (id == R.id.cat_taxis) {
                mapaFragment.cambiarCategoria("taxi");
                return true;
            } else if (id == R.id.cat_deseos) {
                // TODO: Abrir la lista de deseos
                startActivity(new Intent(MainActivity.this, ListaDeseosActivity.class));
                return true;
            }

            return false;
        });
    }
}
