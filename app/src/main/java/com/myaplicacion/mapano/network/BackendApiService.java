package com.myaplicacion.mapano.network;

import com.myaplicacion.mapano.network.model.DeseoNotaPeticion;
import com.myaplicacion.mapano.network.model.DeseoPeticion;
import com.myaplicacion.mapano.network.model.DeseoRespuesta;
import com.myaplicacion.mapano.network.model.LoginPeticion;
import com.myaplicacion.mapano.network.model.PuntoInteresPeticion;
import com.myaplicacion.mapano.network.model.PuntoInteresRespuesta;
import com.myaplicacion.mapano.network.model.RegistroUsuarioPeticion;
import com.myaplicacion.mapano.network.model.UsuarioRespuesta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BackendApiService
{
    @POST("api/usuarios/login")
    Call<UsuarioRespuesta> iniciarSesion(
            @Body LoginPeticion peticion);

    @POST("api/usuarios/registro")
    Call<UsuarioRespuesta> registrar(
            @Body RegistroUsuarioPeticion peticion);

    @POST("api/puntos")
    Call<PuntoInteresRespuesta> guardarPunto(
            @Body PuntoInteresPeticion peticion);

    @GET("api/deseos/usuario/{usuarioId}")
    Call<List<DeseoRespuesta>> listarDeseosUsuario(
            @Path("usuarioId") Long usuarioId);

    @POST("api/deseos")
    Call<DeseoRespuesta> crearDeseo(
            @Body DeseoPeticion peticion);

    @PUT("api/deseos/{id}/nota")
    Call<DeseoRespuesta> actualizarNotaDeseo(
            @Path("id") Long id,
            @Body DeseoNotaPeticion peticion);

    @DELETE("api/deseos/{id}")
    Call<Void> eliminarDeseo(
            @Path("id") Long id);
}
