package com.myaplicacion.mapano.network;

import com.myaplicacion.mapano.network.model.LoginPeticion;
import com.myaplicacion.mapano.network.model.RegistroUsuarioPeticion;
import com.myaplicacion.mapano.network.model.UsuarioRespuesta;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BackendApiService
{
    @POST("api/usuarios/login")
    Call<UsuarioRespuesta> iniciarSesion(
            @Body LoginPeticion peticion);

    @POST("api/usuarios/registro")
    Call<UsuarioRespuesta> registrar(
            @Body RegistroUsuarioPeticion peticion);
}
