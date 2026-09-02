package com.myaplicacion.mapano.network.model;

public class DeseoRespuesta {
    private Long id;
    private Long usuarioId;
    private Long puntoInteresId;
    private String nombrePunto;
    private String tipoPunto;
    private double latitud;
    private double longitud;
    private String nota;
    private boolean visitado;

    public Long getId()
    {
        return id;
    }

    public Long getUsuarioId()
    {
        return usuarioId;
    }

    public Long getPuntoInteresId()
    {
        return puntoInteresId;
    }

    public String getNombrePunto()
    {
        return nombrePunto;
    }
    public String getTipoPunto()
    {
        return tipoPunto;
    }

    public double getLatitud()
    {
        return latitud;
    }

    public double getLongitud()
    {
        return longitud;
    }

    public String getNota()
    {
        return nota;
    }
    public boolean isVisitado()
    {
        return visitado;
    }
}
