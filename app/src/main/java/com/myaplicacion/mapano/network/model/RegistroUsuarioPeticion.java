package com.myaplicacion.mapano.network.model;

public class RegistroUsuarioPeticion {
    private String nombre;
    private String email;
    private String contrasena;

    public RegistroUsuarioPeticion(
            String nombre,
            String email,
            String contrasena)
    {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getEmail()
    {
        return email;
    }

    public String getContrasena()
    {
        return contrasena;
    }
}
