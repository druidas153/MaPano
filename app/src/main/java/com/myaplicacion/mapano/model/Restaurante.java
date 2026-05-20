package com.myaplicacion.mapano.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Modelo de datos para restaurantes.
 * Almacenado en Room (datos mock inicialmente, luego API).
 * Solo lectura para el usuario.
 */
@Entity(tableName = "restaurantes")
public class Restaurante {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String idRemoto;            // ID de la API (cuando se conecte)

    @Embedded
    private DatosComunes datosComunes;

    // === Datos específicos de Restaurante ===
    private int tenedores;              // Clasificación: 1-5 tenedores
    private String tipoCocina;          // "Italiana", "Española", "Japonesa"...
    private String telefono;
    private String horario;
    private String urlWeb;

    // === Campos PREMIUM (futuro modelo de negocio) ===
    private String urlReserva;          // Enlace para reservar (PREMIUM)
    private boolean aceptaReservas;     // ¿Tiene sistema de reservas? (PREMIUM)
    private String mensajePromo;        // Mensaje promocional (PREMIUM)
    private int prioridadMapa;          // 0 = normal, 1-10 = destacado (PREMIUM)

    // ========================
    // CONSTRUCTORES
    // ========================

    public Restaurante() {
        this.datosComunes = new DatosComunes();
        this.prioridadMapa = 0;
        this.aceptaReservas = false;
    }

    public Restaurante(String nombre, String descripcion,
                       double latitud, double longitud, String direccion,
                       int tenedores, String tipoCocina) {
        this.datosComunes = new DatosComunes(nombre, descripcion, latitud, longitud, direccion);
        this.tenedores = tenedores;
        this.tipoCocina = tipoCocina;
        this.prioridadMapa = 0;
        this.aceptaReservas = false;
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

    public int getTenedores() {
        return tenedores;
    }

    public void setTenedores(int tenedores) {
        this.tenedores = tenedores;
    }

    public String getTipoCocina() {
        return tipoCocina;
    }

    public void setTipoCocina(String tipoCocina) {
        this.tipoCocina = tipoCocina;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getUrlWeb() {
        return urlWeb;
    }

    public void setUrlWeb(String urlWeb) {
        this.urlWeb = urlWeb;
    }

    public String getUrlReserva() {
        return urlReserva;
    }

    public void setUrlReserva(String urlReserva) {
        this.urlReserva = urlReserva;
    }

    public boolean isAceptaReservas() {
        return aceptaReservas;
    }

    public void setAceptaReservas(boolean aceptaReservas) {
        this.aceptaReservas = aceptaReservas;
    }

    public String getMensajePromo() {
        return mensajePromo;
    }

    public void setMensajePromo(String mensajePromo) {
        this.mensajePromo = mensajePromo;
    }

    public int getPrioridadMapa() {
        return prioridadMapa;
    }

    public void setPrioridadMapa(int prioridadMapa) {
        this.prioridadMapa = prioridadMapa;
    }

    // ========================
    // MÉTODOS ÚTILES
    // ========================

    public String getTenedoresVisual() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tenedores; i++) {
            sb.append("🍴");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return datosComunes.getNombre() + " - " + tenedores + " tenedores";
    }
}
