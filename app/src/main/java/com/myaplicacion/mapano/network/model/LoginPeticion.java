package com.myaplicacion.mapano.network.model;

public class LoginPeticion {

    private String email;
    private String contrasena;

    public LoginPeticion(String email, String contrasena)
    {
        this.email = email;
        this.contrasena = contrasena;
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
