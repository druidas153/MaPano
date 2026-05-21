package com.myaplicacion.mapano.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.myaplicacion.mapano.R;
import com.myaplicacion.mapano.model.FotoLugar;
import com.myaplicacion.mapano.ui.adapter.FotoAdapter;
import com.myaplicacion.mapano.viewmodel.GaleriaFotosViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Activity para la galería de fotos de un lugar.
 * Permite tomar fotos con la cámara y verlas en un grid.
 */
public class GaleriaFotosActivity extends AppCompatActivity
        implements FotoAdapter.OnFotoClickListener {

    // Extras para recibir datos del lugar
    public static final String EXTRA_ID_PUNTO = "extra_id_punto";
    public static final String EXTRA_CATEGORIA = "extra_categoria";
    public static final String EXTRA_NOMBRE_LUGAR = "extra_nombre_lugar";

    private GaleriaFotosViewModel viewModel;
    private FotoAdapter adapter;
    private RecyclerView recyclerFotos;
    private LinearLayout layoutSinFotos;
    private TextView tvNombreLugar;

    private String idPunto;
    private String categoria;
    private String nombreLugar;

    // Para la cámara
    private String rutaFotoActual;
    private ActivityResultLauncher<Intent> camaraLauncher;
    private ActivityResultLauncher<String> permisoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_fotos);

        // Obtener datos del intent
        idPunto = getIntent().getStringExtra(EXTRA_ID_PUNTO);
        categoria = getIntent().getStringExtra(EXTRA_CATEGORIA);
        nombreLugar = getIntent().getStringExtra(EXTRA_NOMBRE_LUGAR);

        // Inicializar vistas
        recyclerFotos = findViewById(R.id.recyclerFotos);
        layoutSinFotos = findViewById(R.id.layoutSinFotos);
        tvNombreLugar = findViewById(R.id.tvNombreLugar);
        FloatingActionButton fabTomarFoto = findViewById(R.id.fabTomarFoto);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Mostrar nombre del lugar
        tvNombreLugar.setText(nombreLugar != null ? nombreLugar : "Lugar desconocido");

        // Configurar RecyclerView en grid de 2 columnas
        adapter = new FotoAdapter();
        adapter.setOnFotoClickListener(this);
        recyclerFotos.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerFotos.setAdapter(adapter);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(GaleriaFotosViewModel.class);

        // Observar fotos del lugar
        viewModel.getFotosPorLugar(idPunto, categoria).observe(this, fotos -> {
            adapter.setFotos(fotos);
            if (fotos == null || fotos.isEmpty()) {
                recyclerFotos.setVisibility(View.GONE);
                layoutSinFotos.setVisibility(View.VISIBLE);
            } else {
                recyclerFotos.setVisibility(View.VISIBLE);
                layoutSinFotos.setVisibility(View.GONE);
            }
        });

        // Configurar launchers
        configurarLaunchers();

        // Botón tomar foto
        fabTomarFoto.setOnClickListener(v -> verificarPermisoYTomarFoto());
    }

    // ========================
    // CÁMARA
    // ========================

    private void configurarLaunchers() {
        // Launcher para el resultado de la cámara
        camaraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        guardarFotoEnBD();
                    }
                }
        );

        // Launcher para pedir permiso de cámara
        permisoLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                concedido -> {
                    if (concedido) {
                        abrirCamara();
                    } else {
                        Snackbar.make(recyclerFotos, "Permiso de cámara denegado", Snackbar.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void verificarPermisoYTomarFoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            abrirCamara();
        } else {
            permisoLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void abrirCamara() {
        Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Crear archivo para guardar la foto
        File archivoFoto = crearArchivoFoto();
        if (archivoFoto != null) {
            Uri fotoUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    archivoFoto
            );
            intentCamara.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
            camaraLauncher.launch(intentCamara);
        }
    }

    private File crearArchivoFoto() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(new Date());
            String nombreArchivo = "MAPANO_" + timestamp;

            File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File archivo = File.createTempFile(nombreArchivo, ".jpg", directorio);

            rutaFotoActual = archivo.getAbsolutePath();
            return archivo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void guardarFotoEnBD() {
        if (rutaFotoActual == null) return;

        File archivo = new File(rutaFotoActual);
        if (!archivo.exists()) return;

        FotoLugar foto = new FotoLugar(idPunto, nombreLugar, categoria, rutaFotoActual);
        foto.setTamanioBytes(archivo.length());

        viewModel.guardarFoto(foto);

        Snackbar.make(recyclerFotos, "📷 Foto guardada", Snackbar.LENGTH_SHORT).show();
    }

    // ========================
    // CLICK HANDLERS
    // ========================

    @Override
    public void onFotoClick(FotoLugar foto) {
        // Ver foto en pantalla completa
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File archivo = new File(foto.getRutaArchivo());
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", archivo);
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    public void onEliminarClick(FotoLugar foto) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar foto")
                .setMessage("¿Quieres eliminar esta foto?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    // Eliminar archivo físico
                    File archivo = new File(foto.getRutaArchivo());
                    if (archivo.exists()) {
                        archivo.delete();
                    }
                    // Eliminar de la base de datos
                    viewModel.eliminarFoto(foto);
                    Snackbar.make(recyclerFotos, "Foto eliminada 🗑️", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}