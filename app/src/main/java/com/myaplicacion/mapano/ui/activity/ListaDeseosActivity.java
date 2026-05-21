package com.myaplicacion.mapano.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.myaplicacion.mapano.R;
import com.myaplicacion.mapano.model.DeseoLugar;
import com.myaplicacion.mapano.ui.adapter.DeseoAdapter;
import com.myaplicacion.mapano.viewmodel.ListaDeseosViewModel;

/**
 * Activity para la Lista de Deseos del usuario.
 * CRUD completo: ver, editar, eliminar deseos.
 */
public class ListaDeseosActivity extends AppCompatActivity
        implements DeseoAdapter.OnDeseoClickListener {

    private ListaDeseosViewModel viewModel;
    private DeseoAdapter adapter;
    private RecyclerView recyclerDeseos;
    private LinearLayout layoutVacio;
    private ChipGroup chipGroupFiltros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_deseos);

        // Inicializar vistas
        recyclerDeseos = findViewById(R.id.recyclerDeseos);
        layoutVacio = findViewById(R.id.layoutVacio);
        chipGroupFiltros = findViewById(R.id.chipGroupFiltros);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Configurar RecyclerView
        adapter = new DeseoAdapter();
        adapter.setOnDeseoClickListener(this);
        recyclerDeseos.setLayoutManager(new LinearLayoutManager(this));
        recyclerDeseos.setAdapter(adapter);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(ListaDeseosViewModel.class);

        // Observar todos los deseos por defecto
        observarTodos();

        // Configurar filtros
        configurarFiltros();
    }

    /**
     * Observa todos los deseos.
     */
    private void observarTodos() {
        viewModel.getTodosLosDeseos().observe(this, deseos -> {
            adapter.setDeseos(deseos);
            actualizarVisibilidad(deseos == null || deseos.isEmpty());
        });
    }

    /**
     * Configura los chips de filtro.
     */
    private void configurarFiltros() {
        chipGroupFiltros.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                observarTodos();
                return;
            }

            int checkedId = checkedIds.get(0);

            if (checkedId == R.id.chipTodos) {
                viewModel.getTodosLosDeseos().observe(this, deseos -> {
                    adapter.setDeseos(deseos);
                    actualizarVisibilidad(deseos == null || deseos.isEmpty());
                });
            } else if (checkedId == R.id.chipRestaurantes) {
                viewModel.getDeseosPorCategoria("restaurante").observe(this, deseos -> {
                    adapter.setDeseos(deseos);
                    actualizarVisibilidad(deseos == null || deseos.isEmpty());
                });
            } else if (checkedId == R.id.chipEventos) {
                viewModel.getDeseosPorCategoria("evento").observe(this, deseos -> {
                    adapter.setDeseos(deseos);
                    actualizarVisibilidad(deseos == null || deseos.isEmpty());
                });
            } else if (checkedId == R.id.chipFarmacias) {
                viewModel.getDeseosPorCategoria("farmacia").observe(this, deseos -> {
                    adapter.setDeseos(deseos);
                    actualizarVisibilidad(deseos == null || deseos.isEmpty());
                });
            } else if (checkedId == R.id.chipTaxis) {
                viewModel.getDeseosPorCategoria("taxi").observe(this, deseos -> {
                    adapter.setDeseos(deseos);
                    actualizarVisibilidad(deseos == null || deseos.isEmpty());
                });
            } else if (checkedId == R.id.chipPendientes) {
                viewModel.getDeseosPendientes().observe(this, deseos -> {
                    adapter.setDeseos(deseos);
                    actualizarVisibilidad(deseos == null || deseos.isEmpty());
                });
            } else if (checkedId == R.id.chipVisitados) {
                viewModel.getDeseosVisitados().observe(this, deseos -> {
                    adapter.setDeseos(deseos);
                    actualizarVisibilidad(deseos == null || deseos.isEmpty());
                });
            }
        });
    }

    /**
     * Muestra/oculta el mensaje de lista vacía.
     */
    private void actualizarVisibilidad(boolean listaVacia) {
        if (listaVacia) {
            recyclerDeseos.setVisibility(View.GONE);
            layoutVacio.setVisibility(View.VISIBLE);
        } else {
            recyclerDeseos.setVisibility(View.VISIBLE);
            layoutVacio.setVisibility(View.GONE);
        }
    }

    // ========================
    // CLICK HANDLERS
    // ========================

    /**
     * Click normal → Editar nota y prioridad.
     */
    @Override
    public void onDeseoClick(DeseoLugar deseo) {
        mostrarDialogoEditar(deseo);
    }

    /**
     * Click largo → Eliminar con confirmación.
     */
    @Override
    public void onDeseoLongClick(DeseoLugar deseo) {
        mostrarDialogoEliminar(deseo);
    }

    // ========================
    // DIÁLOGOS
    // ========================

    /**
     * Diálogo para editar nota personal y prioridad (UPDATE).
     */
    private void mostrarDialogoEditar(DeseoLugar deseo) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_editar_deseo, null);

        EditText etNota = dialogView.findViewById(R.id.etNotaPersonal);
        RadioGroup rgPrioridad = dialogView.findViewById(R.id.rgPrioridad);
        RadioButton rbBaja = dialogView.findViewById(R.id.rbBaja);
        RadioButton rbMedia = dialogView.findViewById(R.id.rbMedia);
        RadioButton rbAlta = dialogView.findViewById(R.id.rbAlta);

        // Rellenar con datos actuales
        if (deseo.getNotaPersonal() != null) {
            etNota.setText(deseo.getNotaPersonal());
        }

        // Seleccionar prioridad actual
        switch (deseo.getPrioridad()) {
            case 1:
                rbBaja.setChecked(true);
                break;
            case 2:
                rbMedia.setChecked(true);
                break;
            case 3:
                rbAlta.setChecked(true);
                break;
        }

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    // Obtener nueva nota
                    String nuevaNota = etNota.getText().toString().trim();

                    // Obtener nueva prioridad
                    int nuevaPrioridad = 2; // Media por defecto
                    int checkedId = rgPrioridad.getCheckedRadioButtonId();
                    if (checkedId == R.id.rbBaja) {
                        nuevaPrioridad = 1;
                    } else if (checkedId == R.id.rbMedia) {
                        nuevaPrioridad = 2;
                    } else if (checkedId == R.id.rbAlta) {
                        nuevaPrioridad = 3;
                    }

                    // Actualizar en la base de datos
                    deseo.setNotaPersonal(nuevaNota);
                    deseo.setPrioridad(nuevaPrioridad);
                    viewModel.actualizarDeseo(deseo);

                    Snackbar.make(recyclerDeseos, "Deseo actualizado ✅", Snackbar.LENGTH_SHORT).show();
                })
                .setNeutralButton("📷 Fotos", (dialog, which) -> {
                    // Abrir galería de fotos del lugar
                    Intent intent = new Intent(ListaDeseosActivity.this, GaleriaFotosActivity.class);
                    intent.putExtra(GaleriaFotosActivity.EXTRA_ID_PUNTO, String.valueOf(deseo.getIdPuntoInteres()));
                    intent.putExtra(GaleriaFotosActivity.EXTRA_CATEGORIA, deseo.getCategoria());
                    intent.putExtra(GaleriaFotosActivity.EXTRA_NOMBRE_LUGAR, deseo.getNombreLugar());
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Diálogo para confirmar eliminación (DELETE).
     */
    private void mostrarDialogoEliminar(DeseoLugar deseo) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar de la lista")
                .setMessage("¿Quieres eliminar \"" + deseo.getNombreLugar() + "\" de tu lista de deseos?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    viewModel.eliminarDeseo(deseo);
                    Snackbar.make(recyclerDeseos, "Eliminado de tu lista 🗑️", Snackbar.LENGTH_LONG)
                            .setAction("Deshacer", v -> {
                                // Volver a insertar si el usuario pulsa "Deshacer"
                                viewModel.agregarDeseo(deseo);
                            })
                            .show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
