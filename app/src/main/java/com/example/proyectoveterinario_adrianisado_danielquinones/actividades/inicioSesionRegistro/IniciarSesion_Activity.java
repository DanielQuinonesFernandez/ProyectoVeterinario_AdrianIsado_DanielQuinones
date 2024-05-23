package com.example.proyectoveterinario_adrianisado_danielquinones.actividades.inicioSesionRegistro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoveterinario_adrianisado_danielquinones.MainActivity;
import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.SeguridadContrasena;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Usuario;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IniciarSesion_Activity extends AppCompatActivity {

    private Context context;
    private EditText etCorreo, etContrasenia;
    private CircularProgressIndicator circularProgressIndicator;
    private LinearLayout layoutIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        context = this;

        layoutIniciarSesion = findViewById(R.id.layoutIniciarSesion);
        circularProgressIndicator = findViewById(R.id.circularProgressIndicator);
        etCorreo = findViewById(R.id.etCorreoElectronico);
        etContrasenia = findViewById(R.id.etContrasena);
        Button btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        Button btnLimpiarCampos = findViewById(R.id.btnLimpiarCampos);
        TextView tvContraseniaOlvidada = findViewById(R.id.tvOlvidasteContrasena);

        tvContraseniaOlvidada.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContrasenaOlvidadaActivity.class);
            startActivity(intent);
        });

        btnIniciarSesion.setOnClickListener(v -> {
            if (!hayCamposVacios()) {
                iniciarSesion();
            } else {
                Toast.makeText(context, "No puedes dejar campos vacíos", Toast.LENGTH_SHORT).show();
            }
        });

        btnLimpiarCampos.setOnClickListener(v -> limpiarCampos());
    }

    private void iniciarSesion() {
        // Obtener el correo electrónico y contraseña ingresados por el usuario
        String correo = etCorreo.getText().toString();
        String contrasenia = etContrasenia.getText().toString();
        Connection connection = null;

        // Intentar obtener la conexión hasta que no sea null
        while (connection == null) {
            connection = MySQLConnection.getConnection();
            if (connection == null) {
                // Mostrar mensaje de error al usuario y esperar antes de volver a intentar
                Toast.makeText(context, "Intentando conectar con la base de datos...", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(2000); // Esperar 2 segundos antes de reintentar
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Si la conexión es exitosa, continuar con el inicio de sesión
        try {
            // Consulta SQL para buscar el usuario por correo electrónico y contraseña
            String consulta = "SELECT * FROM Usuarios WHERE CorreoElectronico = ? AND Contrasenia = ?";
            PreparedStatement statement = connection.prepareStatement(consulta);
            statement.setString(1, correo);
            statement.setString(2, SeguridadContrasena.hashearContrasena(contrasenia));
            ResultSet resultSet = statement.executeQuery();

            // Verificar si se encontró algún resultado
            if (resultSet.next()) {
                // El usuario existe en la base de datos

                // Mostrar el CircularProgressIndicator y ocultar los demás elementos
                circularProgressIndicator.setVisibility(View.VISIBLE);
                for (int i = 0; i < layoutIniciarSesion.getChildCount(); i++) {
                    View child = layoutIniciarSesion.getChildAt(i);
                    if (!(child instanceof CircularProgressIndicator)) {
                        child.setVisibility(View.GONE);
                    }
                }

                // Obtener los datos del usuario
                int idUsuario = resultSet.getInt("IdUsuario");
                String nombre = resultSet.getString("NombreUsuario");
                String apellidos = resultSet.getString("ApellidosUsuario");
                String correoElectronico = resultSet.getString("CorreoElectronico");
                String contraseniaUsuario = resultSet.getString("Contrasenia");
                int numTelefono = resultSet.getInt("NumTelefono");
                boolean esAdmin = resultSet.getBoolean("EsAdmin");
                boolean estaVetado = resultSet.getBoolean("EstaVetado");
                String razonVeto = resultSet.getString("RazonVeto");

                Usuario usuarioIniciado = new Usuario(idUsuario, nombre, apellidos, correoElectronico, contraseniaUsuario, numTelefono, esAdmin, estaVetado, razonVeto);

                if (!usuarioIniciado.isVetado()) {
                    // Redirigir al usuario a la pantalla principal después de 1 segundo
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("usuarioIniciado", usuarioIniciado);
                        startActivity(intent);
                        finish(); // Cerrar la actividad actual
                    }, 1000);
                } else {
                    mostrarDialogVeto(razonVeto);
                }
            } else {
                // El usuario no existe en la base de datos o las credenciales son incorrectas
                // Puedes mostrar un mensaje de error al usuario
                Toast.makeText(context, "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }

            resultSet.close();
            connection.close();

        } catch (SQLException ignored) {
            // Manejar la excepción adecuadamente
            Toast.makeText(context, "Error al realizar la consulta en la base de datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDialogVeto(String razonVeto){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Este usuario está vetado de la APP");
        builder.setMessage("Tu usuario está vetado en nuestra aplicación.\n\nPuedes contactar con el administrador de la aplicación para obtener ayuda.\n\nRazón: " + razonVeto);
        builder.setIcon(R.drawable.baseline_do_not_disturb_alt_24);

        builder.setPositiveButton("Volver", (dialog, which) -> volverALoginRegister());

        builder.show();
    }

    private void volverALoginRegister(){
        Intent intent = new Intent(context, IniciarSesion_Registrarse_Activity.class);
        startActivity(intent);
    }

    private void limpiarCampos() {
        etCorreo.setText("");
        etContrasenia.setText("");
    }

    private boolean hayCamposVacios() {
        return etCorreo.getText().toString().isEmpty() || etContrasenia.getText().toString().isEmpty();
    }
}