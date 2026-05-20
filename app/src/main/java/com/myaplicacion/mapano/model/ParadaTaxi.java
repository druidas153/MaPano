package com.myaplicacion.mapano.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Modelo de datos para paradas de taxi.
 * Almacenado en Room (datos mock inicialmente, luego API).
 * Solo lectura para el usuario.
 */
@Entity(tableName = "paradas_taxi")
public class ParadaTaxi {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String idRemoto;

    @Embedded
    private DatosComunes datosComunes;

    // === Datos específicos de ParadaTaxi ===
    private int taxisDisponibles;
    private int capacidadTotal;
    private boolean tieneAdaptados;
    private String estado;              // "disponible", "llena", "vacía"

    // ========================
    // CONSTRUCTORES
    // ========================

    public ParadaTaxi() {
        this.datosComunes = new DatosComunes();
    }

    public ParadaTaxi(String nombre, String descripcion,
                      double latitud, double longitud, String direccion,
                      int taxisDisponibles, int capacidadTotal) {
        this.datosComunes = new DatosComunes(nombre, descripcion, latitud, longitud, direccion);
        this.taxisDisponibles = taxisDisponibles;
        this.capacidadTotal = capacidadTotal;
        this.estado = calcularEstado();
    }

    // ========================
    // GETTERS Y SETTERS
    // ========================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdRemoto() {
        return idRemoto;
    }

    public void setIdRemoto(String idRemoto) {
        this.idRemoto = idRemoto;
    }

    public DatosComunes getDatosComunes() {
        return datosComunes;
    }

    public void setDatosComunes(DatosComunes datosComunes) {
        this.datosComunes = datosComunes;
    }

    public int getTaxisDisponibles() {
        return taxisDisponibles;
    }

    public void setTaxisDisponibles(int taxisDisponibles) {
        this.taxisDisponibles = taxisDisponibles;
        this.estado = calcularEstado();
    }

    public int getCapacidadTotal() {
        return capacidadTotal;
    }

    public void setCapacidadTotal(int capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
    }

    public boolean isTieneAdaptados() {
        return tieneAdaptados;
    }

    public void setTieneAdaptados(boolean tieneAdaptados) {
        this.tieneAdaptados = tieneAdaptados;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // ========================
    // MÉTODOS ÚTILES
    // ========================

    private String calcularEstado() {
        if (taxisDisponibles == 0) {
            return "vacía";
        } else if (taxisDisponibles >= capacidadTotal) {
            return "llena";
        } else {
            return "disponible";
        }
    }

    public String getEstadoVisual() {
        switch (estado != null ? estado : "") {
            case "disponible":
                return "🟢 " + taxisDisponibles + " taxis disponibles";
            case "llena":
                return "🟡 Parada llena";
            case "vacía":
                return "🔴 Sin taxis";
            default:
                return "⚪ Estado desconocido";
        }
    }

    public int getPorcentajeOcupacion() {
        if (capacidadTotal == 0) return 0;
        return (taxisDisponibles * 100) / capacidadTotal;
    }

    @Override
    public String toString() {
        return datosComunes.getNombre() + " - " + getEstadoVisual();
    }
}
