package com.myaplicacion.mapano.model;

import androidx.room.Ignore;

/**
 * Campos comunes a todos los puntos de interés.
 * Se usa con @Embedded en las entidades de Room.
 * NO es una tabla, es solo un contenedor de campos reutilizables.
 */
public class DatosComunes {

    private String nombre;
    private String descripcion;
    private double latitud;
    private double longitud;
    private String direccion;
    private String origenDatos;         // "LOCAL", "API_PUBLICA" o "PREMIUM"
    private boolean esPremium;
    private String urlImagen;
    private long fechaActualizacion;

    // ========================
    // CONSTRUCTORES
    // ========================

    public DatosComunes() {
        this.origenDatos = "LOCAL";
        this.esPremium = false;
        this.fechaActualizacion = System.currentTimeMillis();
    }
    @Ignore
    public DatosComunes(String nombre, String descripcion,
                        double latitud, double longitud, String direccion) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
    }

    // ========================
    // GETTERS Y SETTERS
    // ========================

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getOrigenDatos() {
        return origenDatos;
    }

    public void setOrigenDatos(String origenDatos) {
        this.origenDatos = origenDatos;
    }

    public boolean isEsPremium() {
        return esPremium;
    }

    public void setEsPremium(boolean esPremium) {
        this.esPremium = esPremium;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public long getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(long fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}

