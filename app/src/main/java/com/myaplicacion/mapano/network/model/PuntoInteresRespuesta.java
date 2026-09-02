package com.myaplicacion.mapano.network.model;

public class PuntoInteresRespuesta {

    private Long id;
    private String nombre;
    private String descripcion;
    private double latitud;
    private double longitud;
    private String direccion;
    private String tipo;

    public Long getId()
    {
        return id;
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
