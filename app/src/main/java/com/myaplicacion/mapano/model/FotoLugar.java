package com.myaplicacion.mapano.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Modelo de datos para las fotos tomadas por el usuario.
 * Se almacena en Room (base de datos local).
 * Cada foto está asociada a un punto de interés.
 * Operaciones: Crear, Leer y Eliminar (sin edición).
 */
@Entity(tableName = "fotos_lugar")
public class FotoLugar {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String idPuntoInteres;
    private String nombreLugar;
    private String categoria;
    private String rutaArchivo;
    private long fechaCaptura;
    private String comentario;
    private int anchoPixeles;
    private int altoPixeles;
    private long tamanioBytes;

    // ========================
    // CONSTRUCTORES
    // ========================

    public FotoLugar() {
    }

    @Ignore
    public FotoLugar(String idPuntoInteres, String nombreLugar,
                     String categoria, String rutaArchivo) {
        this.idPuntoInteres = idPuntoInteres;
        this.nombreLugar = nombreLugar;
        this.categoria = categoria;
        this.rutaArchivo = rutaArchivo;
        this.fechaCaptura = System.currentTimeMillis();
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

    public String getIdPuntoInteres() {
        return idPuntoInteres;
    }

    public void setIdPuntoInteres(String idPuntoInteres) {
        this.idPuntoInteres = idPuntoInteres;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public void setNombreLugar(String nombreLugar) {
        this.nombreLugar = nombreLugar;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public long getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(long fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getAnchoPixeles() {
        return anchoPixeles;
    }

    public void setAnchoPixeles(int anchoPixeles) {
        this.anchoPixeles = anchoPixeles;
    }

    public int getAltoPixeles() {
        return altoPixeles;
    }

    public void setAltoPixeles(int altoPixeles) {
        this.altoPixeles = altoPixeles;
    }

    public long getTamanioBytes() {
        return tamanioBytes;
    }

    public void setTamanioBytes(long tamanioBytes) {
        this.tamanioBytes = tamanioBytes;
    }

    // ========================
    // MÉTODOS ÚTILES
    // ========================

    public String getTamanioFormateado() {
        if (tamanioBytes < 1024) {
            return tamanioBytes + " B";
        } else if (tamanioBytes < 1024 * 1024) {
            return String.format("%.1f KB", tamanioBytes / 1024.0);
        } else {
            return String.format("%.1f MB", tamanioBytes / (1024.0 * 1024.0));
        }
    }

    public String getResolucion() {
        return anchoPixeles + " x " + altoPixeles;
    }

    @Override
    public String toString() {
        return nombreLugar + " - " + categoria + " (" + getTamanioFormateado() + ")";
    }
}
