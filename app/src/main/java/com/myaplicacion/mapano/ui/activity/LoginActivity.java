package com.myaplicacion.mapano.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myaplicacion.mapano.R;
import com.myaplicacion.mapano.network.BackendApiService;
import com.myaplicacion.mapano.network.BackendRetrofitClient;
import com.myaplicacion.mapano.network.model.LoginPeticion;
import com.myaplicacion.mapano.network.model.UsuarioRespuesta;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editContrasena;

    private BackendApiService backendApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editContrasena = findViewById(R.id.editContrasena);

        Button botonIniciarSesion = findViewById(R.id.botonIniciarSesion);
        Button botonRegistro = findViewById(R.id.botonRegistro);

        backendApiService = BackendRetrofitClient.getApiService();

        botonIniciarSesion.setOnClickListener(v -> iniciarSesion());

        botonRegistro.setOnClickListener(v ->
        {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);

            startActivity(intent);
        });
    }

    private void iniciarSesion()
    {
        String email = editEmail.getText().toString().trim();
        String contrasena = editContrasena.getText().toString();

        if (email.isEmpty() || contrasena.isEmpty())
        {
            Toast.makeText(
                    this,
                    "Introduce email y contraseña",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        LoginPeticion peticion = new LoginPeticion(email, contrasena);

        backendApiService.iniciarSesion(peticion)
                .enqueue(new Callback<UsuarioRespuesta>()
                {
                    @Override
                    public void onResponse(
                            Call<UsuarioRespuesta> call,
                            Response<UsuarioRespuesta> response)
                    {
                        if (response.isSuccessful() && response.body() != null)
                        {
                            guardarUsuario(response.body());

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Sesión iniciada correctamente",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(
                                    LoginActivity.this,
                                    "Email o contraseña incorrectos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<UsuarioRespuesta> call,
                            Throwable t)
                    {
                        Toast.makeText(
                                LoginActivity.this,
                                "No se pudo conectar con el servidor",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarUsuario(UsuarioRespuesta usuario)
    {
        SharedPreferences preferencias =
                getSharedPreferences("sesion", MODE_PRIVATE);

        preferencias.edit()
                .putLong("usuarioId", usuario.getId())
                .putString("nombre", usuario.getNombre())
                .putString("email", usuario.getEmail())
                .apply();
    }
}
