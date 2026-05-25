package com.myaplicacion.mapano.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Modelo de datos para eventos.
 * Almacenado en Room. Solo lectura para el usuario.
 * En el futuro, los eventos se crearán desde un panel de administración.
 */
@Entity(tableName = "eventos")
public class Evento {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String idRemoto;

    @Embedded
    private DatosComunes datosComunes;

    // === Datos específicos de Evento ===
    private String fechaInicio;
    private String fechaFin;
    private String horaInicio;
    private String horaFin;
    private String urlEntradas;         // Enlace para comprar entradas
    private String organizador;
    private double precio;              // 0 = gratis
    private boolean esGratuito;
    private String tipoEvento;          // "teatro", "concierto", "fiesta", "deporte"

    // === Campos PREMIUM (empresa paga) ===
    private boolean esPatrocinado;
    private String bannerUrl;
    private String mensajePromo;
    private int prioridadMapa;

    // ========================
    // CONSTRUCTORES
    // ========================

    public Evento() {
        this.datosComunes = new DatosComunes();
        this.esPatrocinado = false;
        this.prioridadMapa = 0;
    }

    @Ignore
    public Evento(String nombre, String descripcion, double latitud,
                  double longitud, String direccion, String fechaInicio,
                  String horaInicio, String tipoEvento) {
        this.datosComunes = new DatosComunes(nombre, descripcion, latitud, longitud, direccion);
        this.fechaInicio = fechaInicio;
        this.horaInicio = horaInicio;
        this.tipoEvento = tipoEvento;
        this.esPatrocinado = false;
        this.prioridadMapa = 0;
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

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getUrlEntradas() {
        return urlEntradas;
    }

    public void setUrlEntradas(String urlEntradas) {
        this.urlEntradas = urlEntradas;
    }

    public String getOrganizador() {
        return organizador;
    }

    public void setOrganizador(String organizador) {
        this.organizador = organizador;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isEsGratuito() {
        return esGratuito;
    }

    public void setEsGratuito(boolean esGratuito) {
        this.esGratuito = esGratuito;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public boolean isEsPatrocinado() {
        return esPatrocinado;
    }

    public void setEsPatrocinado(boolean esPatrocinado) {
        this.esPatrocinado = esPatrocinado;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
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

    public String getPrecioFormateado() {
        if (esGratuito || precio == 0) {
            return "Gratis";
        }
        return String.format("%.2f €", precio);
    }

    @Override
    public String toString() {
        return datosComunes.getNombre() + " - " + tipoEvento + " (" + fechaInicio + ")"+" ("+fechaFin+")";
    }
}
