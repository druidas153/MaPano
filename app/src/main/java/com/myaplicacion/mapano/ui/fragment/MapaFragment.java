package com.myaplicacion.mapano.ui.fragment;
import com.myaplicacion.mapano.ui.fragment.CustomInfoWindow;
import androidx.appcompat.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.myaplicacion.mapano.R;
import com.myaplicacion.mapano.model.DeseoLugar;
import com.myaplicacion.mapano.model.Evento;
import com.myaplicacion.mapano.model.Farmacia;
import com.myaplicacion.mapano.model.ParadaTaxi;
import com.myaplicacion.mapano.model.Restaurante;
import com.myaplicacion.mapano.viewmodel.MapaViewModel;
import com.myaplicacion.mapano.viewmodel.ListaDeseosViewModel;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

/**
 * Fragment principal que muestra el mapa de Zaragoza con los marcadores
 * según la categoría seleccionada.
 */
public class MapaFragment extends Fragment {

    private MapView mapView;
    private MapaViewModel viewModel;
    private FloatingActionButton fabMiUbicacion;
    private Marker marcadorActivo = null;
    private ListaDeseosViewModel deseosViewModel;
    private org.osmdroid.views.overlay.Overlay overlayToqueMapa;

    // Variable de clase
    private MyLocationNewOverlay locationOverlay;
    private ActivityResultLauncher<String[]> permisoUbicacionLauncher;
    // Coordenadas de Zaragoza (centro)
    private static final double ZARAGOZA_LAT = 41.6488;
    private static final double ZARAGOZA_LON = -0.8891;
    private static final double ZOOM_INICIAL = 15.0;

    // Categoría actualmente seleccionada
    private String categoriaActual = "restaurante";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        // Inicializar vistas
        mapView = view.findViewById(R.id.mapView);
        fabMiUbicacion = view.findViewById(R.id.fabMiUbicacion);

        // Configurar launcher para pedir permisos de ubicación
        permisoUbicacionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocation = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocation = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if (fineLocation != null && fineLocation) {
                        activarUbicacion();
                    } else if (coarseLocation != null && coarseLocation) {
                        activarUbicacion();
                    }
                }
        );

        // Configurar el mapa
        configurarMapa();

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MapaViewModel.class);
        deseosViewModel = new ViewModelProvider(requireActivity()).get(ListaDeseosViewModel.class);
        // Observar datos según categoría inicial
        observarDatos();

        // Botón de ubicación
        fabMiUbicacion.setOnClickListener(v -> centrarEnMiUbicacion());

        // Activar geolocalización
        verificarPermisosUbicacion();
        return view;
    }

    /**
     * Configura el mapa de osmdroid.
     */
    private void configurarMapa() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(ZOOM_INICIAL);
        mapView.getController().setCenter(new GeoPoint(ZARAGOZA_LAT, ZARAGOZA_LON));

        // Configurar zoom mínimo y máximo
        mapView.setMinZoomLevel(10.0);
        mapView.setMaxZoomLevel(20.0);// Permitir zoom más alto con sobreescalado de tiles

        // Habilitar el escalado de tiles para ver mejor al hacer zoom máximo
        mapView.setTilesScaledToDpi(true);
        mapView.setTilesScaleFactor(1.4f);

        // Overlay para detectar toques fuera de los marcadores
        overlayToqueMapa = new org.osmdroid.views.overlay.Overlay() {
            @Override
            public boolean onSingleTapConfirmed(android.view.MotionEvent e, MapView mapView) {
                cerrarBubbleActivo();
                return false;
            }
        };
        mapView.getOverlays().add(overlayToqueMapa);
    }

    /**
     * Centra el mapa en Zaragoza.
     */
    private void centrarEnZaragoza() {
        mapView.getController().animateTo(new GeoPoint(ZARAGOZA_LAT, ZARAGOZA_LON));
        mapView.getController().setZoom(ZOOM_INICIAL);
    }

    /**
     * Observa los datos del ViewModel y actualiza los marcadores.
     */
    private void observarDatos() {
        // Observar restaurantes
        viewModel.getRestaurantes().observe(getViewLifecycleOwner(), restaurantes -> {
            if ("restaurante".equals(categoriaActual)) {
                limpiarMarcadores();
                mostrarRestaurantes(restaurantes);
            }
        });

        // Observar eventos
        viewModel.getEventos().observe(getViewLifecycleOwner(), eventos -> {
            if ("evento".equals(categoriaActual)) {
                limpiarMarcadores();
                mostrarEventos(eventos);
            }
        });

        // Observar farmacias
        viewModel.getFarmacias().observe(getViewLifecycleOwner(), farmacias -> {
            if ("farmacia".equals(categoriaActual)) {
                limpiarMarcadores();
                mostrarFarmacias(farmacias);
            }
        });

        // Observar paradas de taxi
        viewModel.getParadasTaxi().observe(getViewLifecycleOwner(), paradas -> {
            if ("taxi".equals(categoriaActual)) {
                limpiarMarcadores();
                mostrarParadasTaxi(paradas);
            }
        });
    }

    /**
     * Cambia la categoría mostrada en el mapa.
     * Se llama desde MainActivity cuando el usuario pulsa en la barra inferior.
     */
    public void cambiarCategoria(String nuevaCategoria) {
        this.categoriaActual = nuevaCategoria;

        // Forzar actualización de marcadores
        limpiarMarcadores();

        switch (nuevaCategoria) {
            case "restaurante":
                List<Restaurante> restaurantes = viewModel.getRestaurantes().getValue();
                if (restaurantes != null) mostrarRestaurantes(restaurantes);
                break;
            case "evento":
                List<Evento> eventos = viewModel.getEventos().getValue();
                if (eventos != null) mostrarEventos(eventos);
                break;
            case "farmacia":
                List<Farmacia> farmacias = viewModel.getFarmacias().getValue();
                if (farmacias != null) mostrarFarmacias(farmacias);
                break;
            case "taxi":
                List<ParadaTaxi> paradas = viewModel.getParadasTaxi().getValue();
                if (paradas != null) mostrarParadasTaxi(paradas);
                break;
        }
    }

    // ========================
    // MOSTRAR MARCADORES
    // ========================

    private void mostrarRestaurantes(List<Restaurante> restaurantes) {
        for (Restaurante r : restaurantes) {
            Marker marker = crearMarcador(
                    r.getDatosComunes().getLatitud(),
                    r.getDatosComunes().getLongitud(),
                    r.getDatosComunes().getNombre(),
                    r.getTenedoresVisual() + " - " + r.getTipoCocina(),
                    r.getId(),
                    "restaurante"
            );
            // Color del marcador según si es premium o no
            if (r.getDatosComunes().isEsPremium()) {
                marker.setSubDescription("⭐ PREMIUM: " + r.getMensajePromo());
            }
            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }

    private void mostrarEventos(List<Evento> eventos) {
        for (Evento e : eventos) {
            Marker marker = crearMarcador(
                    e.getDatosComunes().getLatitud(),
                    e.getDatosComunes().getLongitud(),
                    e.getDatosComunes().getNombre(),
                    e.getTipoEvento() + " - " + e.getFechaInicio() + " " + e.getHoraInicio()
            );
            if (e.isEsPatrocinado()) {
                marker.setSubDescription("⭐ PATROCINADO: " + e.getMensajePromo());
            }
            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }

    private void mostrarFarmacias(List<Farmacia> farmacias) {
        for (Farmacia f : farmacias) {
            Marker marker = crearMarcador(
                    f.getDatosComunes().getLatitud(),
                    f.getDatosComunes().getLongitud(),
                    f.getDatosComunes().getNombre(),
                    f.getEstadoTexto() + " - " + f.getTelefono()
            );
            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }

    private void mostrarParadasTaxi(List<ParadaTaxi> paradas) {
        for (ParadaTaxi t : paradas) {
            Marker marker = crearMarcador(
                    t.getDatosComunes().getLatitud(),
                    t.getDatosComunes().getLongitud(),
                    t.getDatosComunes().getNombre(),
                    t.getEstadoVisual()
            );
            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }

    // ========================
    // UTILIDADES
    // ========================

    /**
     * Crea un marcador en el mapa.
     */
    private Marker crearMarcador(double lat, double lon, String titulo, String descripcion) {
        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(lat, lon));
        marker.setTitle(titulo);
        marker.setSnippet(descripcion);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        return marker;
    }


    /**
     * Crea un marcador en el mapa con opción de añadir a lista de deseos.
     */
    private Marker crearMarcador(double lat, double lon, String titulo,
                                 String descripcion, long idPunto, String categoria) {
        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(lat, lon));
        marker.setTitle(titulo);
        marker.setSnippet(descripcion);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        // InfoWindow personalizada con botón "Añadir a mi lista"
        CustomInfoWindow infoWindow = new CustomInfoWindow(
                mapView, idPunto, categoria, titulo, descripcion, lat, lon,
                (id, cat, nom, desc, la, lo) -> {
                    DeseoLugar deseo = new DeseoLugar(id, cat, nom, la, lo);
                    deseosViewModel.agregarDeseo(deseo);
                    Snackbar.make(mapView, "Añadido a tu lista ❤️", Snackbar.LENGTH_SHORT).show();
                }
        );
        marker.setInfoWindow(infoWindow);

        // Al pulsar el marcador → cerrar el anterior y abrir este
        marker.setOnMarkerClickListener((m, map) -> {
            // Cerrar el bubble anterior si hay uno abierto
            if (marcadorActivo != null && marcadorActivo.isInfoWindowShown()) {
                marcadorActivo.closeInfoWindow();
            }

            // Abrir el nuevo bubble
            m.showInfoWindow();
            marcadorActivo = m;

            return true;
        });

        return marker;
    }

    /**
     * Elimina todos los marcadores del mapa.
     */
    private void limpiarMarcadores() {
        cerrarBubbleActivo();
        InfoWindow.closeAllInfoWindowsOn(mapView);
        mapView.getOverlays().clear();
        // Volver a añadir el overlay de toque (porque clear() lo eliminó)
        mapView.getOverlays().add(overlayToqueMapa);
        if (locationOverlay != null) {
            mapView.getOverlays().add(locationOverlay);
        }
        mapView.invalidate();
    }
    private void cerrarBubbleActivo() {
        if (marcadorActivo != null && marcadorActivo.isInfoWindowShown()) {
            marcadorActivo.closeInfoWindow();
            marcadorActivo = null;
        }
    }

    // ========================
    // CICLO DE VIDA
    // ========================

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (locationOverlay != null) {
            locationOverlay.enableMyLocation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (locationOverlay != null) {
            locationOverlay.disableMyLocation();
        }
    }

    /**
     * Muestra un diálogo para añadir el lugar a la lista de deseos.
     */
    private void mostrarDialogoAñadir(long idPunto, String categoria,
                                      String nombre, double lat, double lon) {
        new AlertDialog.Builder(requireContext())
                .setTitle(nombre)
                .setMessage("¿Quieres añadir este lugar a tu lista de deseos?")
                .setPositiveButton("❤️ Añadir", (dialog, which) -> {
                    DeseoLugar deseo = new DeseoLugar(idPunto, categoria, nombre, lat, lon);
                    deseosViewModel.agregarDeseo(deseo);

                    Snackbar.make(mapView, "Añadido a tu lista ❤️", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Verifica permisos y activa la ubicación del usuario.
     */
    private void verificarPermisosUbicacion() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            activarUbicacion();
        } else {
            // Pedir permisos
            permisoUbicacionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    /**
     * Activa el overlay de ubicación del usuario en el mapa.
     * Muestra un punto azul y permite centrar el mapa en la posición real.
     */
    private void activarUbicacion() {
        locationOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(requireContext()), mapView);
        locationOverlay.enableMyLocation();

        // Cuando obtiene la primera ubicación, centrar el mapa ahí
        locationOverlay.runOnFirstFix(() -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (locationOverlay.getMyLocation() != null) {
                        mapView.getController().animateTo(locationOverlay.getMyLocation());
                        mapView.getController().setZoom(16.0);
                    }
                });
            }
        });

        mapView.getOverlays().add(locationOverlay);
        mapView.invalidate();
    }

    /**
     * Centra el mapa en la ubicación actual del usuario.
     */
    private void centrarEnMiUbicacion() {
        if (locationOverlay != null && locationOverlay.getMyLocation() != null) {
            mapView.getController().animateTo(locationOverlay.getMyLocation());
            mapView.getController().setZoom(16.0);
        } else {
            // Si no hay ubicación, centrar en Zaragoza por defecto
            mapView.getController().animateTo(new GeoPoint(ZARAGOZA_LAT, ZARAGOZA_LON));
            mapView.getController().setZoom(ZOOM_INICIAL);
            Snackbar.make(mapView, "Activa la ubicación para centrar en tu posición", Snackbar.LENGTH_SHORT).show();
        }
    }

}
