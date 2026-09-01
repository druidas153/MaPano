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
import com.myaplicacion.mapano.network.model.RegistroUsuarioPeticion;
import com.myaplicacion.mapano.network.model.UsuarioRespuesta;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private EditText editNombre;
    private EditText editEmail;
    private EditText editContrasena;

    private BackendApiService backendApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editNombre = findViewById(R.id.editNombre);
        editEmail = findViewById(R.id.editEmail);
        editContrasena = findViewById(R.id.editContrasena);

        Button botonRegistrar = findViewById(R.id.botonRegistrar);
        Button botonVolverLogin = findViewById(R.id.botonVolverLogin);

        backendApiService = BackendRetrofitClient.getApiService();

        botonRegistrar.setOnClickListener(v -> registrarUsuario());

        botonVolverLogin.setOnClickListener(v -> finish());
    }

    private void registrarUsuario()
    {
        String nombre = editNombre.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String contrasena = editContrasena.getText().toString();

        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty())
        {
            Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        RegistroUsuarioPeticion peticion = new RegistroUsuarioPeticion(
                        nombre,
                        email,
                        contrasena);

        backendApiService.registrar(peticion)
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
                                    RegistroActivity.this,
                                    "Usuario registrado correctamente",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegistroActivity.this,
                                            MainActivity.class);

                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(
                                    RegistroActivity.this,
                                    "No se pudo registrar el usuario",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UsuarioRespuesta> call, Throwable t)
                    {
                        Toast.makeText(
                                RegistroActivity.this,
                                "No se pudo conectar con el servidor",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarUsuario(UsuarioRespuesta usuario)
    {
        SharedPreferences preferencias = getSharedPreferences("sesion", MODE_PRIVATE);

        preferencias.edit()
                .putLong("usuarioId", usuario.getId())
                .putString("nombre", usuario.getNombre())
                .putString("email", usuario.getEmail())
                .apply();
    }
}
