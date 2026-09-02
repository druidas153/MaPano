package com.myaplicacion.mapano.network.model;

public class PuntoInteresPeticion {
    private String nombre;
    private String descripcion;
    private double latitud;
    private double longitud;
    private String direccion;
    private String tipo;

    public PuntoInteresPeticion(
            String nombre,
            String descripcion,
            double latitud,
            double longitud,
            String direccion,
            String tipo)
    {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.tipo = tipo;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public double getLatitud()
    {
        return latitud;
    }

    public double getLongitud()
    {
        return longitud;
    }

    public String getDireccion()
    {
        return direccion;
    }

    public String getTipo()
    {
        return tipo;
    }
}
