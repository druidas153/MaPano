package com.myaplicacion.mapano.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myaplicacion.mapano.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

/**
 * InfoWindow personalizada para los marcadores del mapa.
 * Muestra nombre, descripción y botón para añadir a lista de deseos.
 */
public class CustomInfoWindow extends InfoWindow {

    private final OnAñadirClickListener listener;
    private final long idPunto;
    private final String categoria;
    private final String nombre;
    private final String descripcion;
    private final double lat;
    private final double lon;

    public interface OnAñadirClickListener {
        void onAñadirClick(long idPunto, String categoria, String nombre,
                           String descripcion, double lat, double lon);
    }

    public CustomInfoWindow(MapView mapView, long idPunto, String categoria,
                            String nombre, String descripcion,
                            double lat, double lon,
                            OnAñadirClickListener listener) {
        super(R.layout.bubble_marcador, mapView);
        this.idPunto = idPunto;
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.lat = lat;
        this.lon = lon;
        this.listener = listener;
    }

    @Override
    public void onOpen(Object item) {
        View view = getView();

        TextView tvTitulo = view.findViewById(R.id.tvBubbleTitulo);
        TextView tvDescripcion = view.findViewById(R.id.tvBubbleDescripcion);
        Button btnAñadir = view.findViewById(R.id.btnAñadirLista);

        tvTitulo.setText(nombre);
        tvDescripcion.setText(descripcion);

        btnAñadir.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAñadirClick(idPunto, categoria, nombre, descripcion, lat, lon);
            }
            close();
        });

        // Cerrar la bubble al pulsar fuera del botón
        view.setOnClickListener(v -> close());
    }

    @Override
    public void onClose() {
        // Nada especial al cerrar
    }
}

