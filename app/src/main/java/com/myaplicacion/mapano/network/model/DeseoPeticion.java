package com.myaplicacion.mapano.network.model;

public class DeseoPeticion {

    private Long usuarioId;
    private Long puntoInteresId;
    private String nota;

    public DeseoPeticion(Long usuarioId, Long puntoInteresId, String nota)
    {
        this.usuarioId = usuarioId;
        this.puntoInteresId = puntoInteresId;
        this.nota = nota;
    }

    public Long getUsuarioId()
    {
        return usuarioId;
    }

    public Long getPuntoInteresId()
    {
        return puntoInteresId;
    }

    public String getNota()
    {
        return nota;
    }
}
