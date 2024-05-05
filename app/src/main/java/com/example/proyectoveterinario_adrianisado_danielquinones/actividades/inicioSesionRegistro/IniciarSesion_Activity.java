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

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoveterinario_adrianisado_danielquinones.MainActivity;
import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.SeguridadContrasena;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Usuario;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;

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

        // Realizar la consulta SQL para buscar el usuario en la base de datos
        Connection connection = MySQLConnection.getConnection();
        if (connection != null) {
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

                    // Mostrar el CircularProgressIndicator y ocular los demás elementos
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

                    Usuario usuarioIniciado = new Usuario(idUsuario, nombre, apellidos, correoElectronico, contraseniaUsuario, numTelefono);

                    // Redirigir al usuario a la pantalla principal después de 5 segundos
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("usuarioIniciado", usuarioIniciado);
                            startActivity(intent);
                            finish(); // Cerrar la actividad actual
                        }
                    }, 1500);

                } else {
                    // El usuario no existe en la base de datos o las credenciales son incorrectas
                    // Puedes mostrar un mensaje de error al usuario
                    Toast.makeText(context, "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }

                resultSet.close();
                connection.close();

            } catch (SQLException ignored) {
            }
        } else {
            // No se pudo establecer la conexión a la base de datos
            // Puedes mostrar un mensaje de error al usuario
            Toast.makeText(context, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show();
        }
    }


    private void limpiarCampos() {
        etCorreo.setText("");
        etContrasenia.setText("");
    }

    private boolean hayCamposVacios() {
        return etCorreo.getText().toString().isEmpty() || etContrasenia.getText().toString().isEmpty();
    }
}