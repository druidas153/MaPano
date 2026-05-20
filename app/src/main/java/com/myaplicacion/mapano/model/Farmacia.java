package com.myaplicacion.mapano.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Modelo de datos para farmacias.
 * Almacenado en Room (datos mock inicialmente, luego API).
 * Solo lectura para el usuario.
 */
@Entity(tableName = "farmacias")
public class Farmacia {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String idRemoto;

    @Embedded
    private DatosComunes datosComunes;

    // === Datos específicos de Farmacia ===
    private boolean estaAbierta;
    private boolean esDeGuardia;
    private String horarioApertura;
    private String horarioCierre;
    private String telefono;
    private String titular;

    // ========================
    // CONSTRUCTORES
    // ========================

    public Farmacia() {
        this.datosComunes = new DatosComunes();
    }

    public Farmacia(String nombre, String descripcion,
                    double latitud, double longitud, String direccion,
                    boolean estaAbierta, boolean esDeGuardia) {
        this.datosComunes = new DatosComunes(nombre, descripcion, latitud, longitud, direccion);
        this.estaAbierta = estaAbierta;
        this.esDeGuardia = esDeGuardia;
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

    public boolean isEstaAbierta() {
        return estaAbierta;
    }

    public void setEstaAbierta(boolean estaAbierta) {
        this.estaAbierta = estaAbierta;
    }

    public boolean isEsDeGuardia() {
        return esDeGuardia;
    }

    public void setEsDeGuardia(boolean esDeGuardia) {
        this.esDeGuardia = esDeGuardia;
    }

    public String getHorarioApertura() {
        return horarioApertura;
    }

    public void setHorarioApertura(String horarioApertura) {
        this.horarioApertura = horarioApertura;
    }

    public String getHorarioCierre() {
        return horarioCierre;
    }

    public void setHorarioCierre(String horarioCierre) {
        this.horarioCierre = horarioCierre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    // ========================
    // MÉTODOS ÚTILES
    // ========================

    public String getEstadoTexto() {
        if (esDeGuardia) {
            return "🟢 De guardia";
        } else if (estaAbierta) {
            return "🟢 Abierta";
        } else {
            return "🔴 Cerrada";
        }
    }

    @Override
    public String toString() {
        return datosComunes.getNombre() + " - " + getEstadoTexto();
    }
}
