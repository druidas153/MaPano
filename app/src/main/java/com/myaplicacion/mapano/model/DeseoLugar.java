package com.myaplicacion.mapano.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Modelo de datos para la Lista de Deseos del usuario.
 * CRUD completo: el usuario añade, consulta, modifica y elimina lugares.
 * Registra el engagement (visita confirmada por geolocalización).
 */
@Entity(tableName = "deseos_lugar")
public class DeseoLugar {

    @PrimaryKey(autoGenerate = true)
    private long id;

    // === Referencia al punto de interés ===
    private long idPuntoInteres;        // ID del restaurante/evento/farmacia/taxi
    private long idPuntoBackend;
    private long idDeseoBackend;
    private String categoria;           // "restaurante", "evento", "farmacia", "taxi"
    private String nombreLugar;         // Para mostrar en la lista sin consultar otra tabla

    public long getIdPuntoBackend() {
        return idPuntoBackend;
    }

    public long getIdDeseoBackend() {
        return idDeseoBackend;
    }

    public void setIdPuntoBackend(long idPuntoBackend) {
        this.idPuntoBackend = idPuntoBackend;
    }

    public void setIdDeseoBackend(long idDeseoBackend) {
        this.idDeseoBackend = idDeseoBackend;
    }

    private double latitud;             // Para calcular ruta y geofencing
    private double longitud;

    // === Datos de la lista de deseos ===
    private long fechaAnadido;          // Cuándo lo añadió a la lista
    private String notaPersonal;        // "Quiero probar el menú del día"
    private int prioridad;              // 1 = baja, 2 = media, 3 = alta
    private boolean visitado;           // ¿Ya fue?

    // === Datos de engagement (monetización) ===
    private long fechaVisita;           // Cuándo llegó al lugar (timestamp)
    private double distanciaAlLlegar;   // Distancia en metros cuando se confirmó
    private boolean engagementConfirmado; // ¿Se confirmó la visita por geofencing?
    private String rutaFotoVisita;      // Foto tomada al llegar (prueba + recuerdo)

    // ========================
    // CONSTRUCTORES
    // ========================

    public DeseoLugar() {
        this.fechaAnadido = System.currentTimeMillis();
        this.prioridad = 2; // Media por defecto
        this.visitado = false;
        this.engagementConfirmado = false;
    }
    @Ignore
    public DeseoLugar(long idPuntoInteres, String categoria,
                      String nombreLugar, double latitud, double longitud) {
        this();
        this.idPuntoInteres = idPuntoInteres;
        this.categoria = categoria;
        this.nombreLugar = nombreLugar;
        this.latitud = latitud;
        this.longitud = longitud;
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

    public long getIdPuntoInteres() {
        return idPuntoInteres;
    }

    public void setIdPuntoInteres(long idPuntoInteres) {
        this.idPuntoInteres = idPuntoInteres;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public void setNombreLugar(String nombreLugar) {
        this.nombreLugar = nombreLugar;
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

    public long getFechaAnadido() {
        return fechaAnadido;
    }

    public void setFechaAnadido(long fechaAnadido) {
        this.fechaAnadido = fechaAnadido;
    }

    public String getNotaPersonal() {
        return notaPersonal;
    }

    public void setNotaPersonal(String notaPersonal) {
        this.notaPersonal = notaPersonal;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public boolean isVisitado() {
        return visitado;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }

    public long getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(long fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public double getDistanciaAlLlegar() {
        return distanciaAlLlegar;
    }

    public void setDistanciaAlLlegar(double distanciaAlLlegar) {
        this.distanciaAlLlegar = distanciaAlLlegar;
    }

    public boolean isEngagementConfirmado() {
        return engagementConfirmado;
    }

    public void setEngagementConfirmado(boolean engagementConfirmado) {
        this.engagementConfirmado = engagementConfirmado;
    }

    public String getRutaFotoVisita() {
        return rutaFotoVisita;
    }

    public void setRutaFotoVisita(String rutaFotoVisita) {
        this.rutaFotoVisita = rutaFotoVisita;
    }

    // ========================
    // MÉTODOS ÚTILES
    // ========================

    public String getPrioridadTexto() {
        switch (prioridad) {
            case 1: return "⭐ Baja";
            case 2: return "⭐⭐ Media";
            case 3: return "⭐⭐⭐ Alta";
            default: return "⭐⭐ Media";
        }
    }

    public String getEstadoTexto() {
        if (engagementConfirmado) {
            return "✅ Visitado";
        } else if (visitado) {
            return "🟡 Marcado como visitado";
        } else {
            return "📋 Pendiente";
        }
    }

    @Override
    public String toString() {
        return nombreLugar + " (" + categoria + ") - " + getEstadoTexto();
    }
}