package com.example.proyectoveterinario_adrianisado_danielquinones.actividades.inicioSesionRegistro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoveterinario_adrianisado_danielquinones.MainActivity;
import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.SeguridadContrasena;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IniciarSesion_Activity extends AppCompatActivity {

    private Context context;
    private EditText etCorreo, etContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        context = this;

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

                    // Obtener los datos del usuario
                    int idUsuario = resultSet.getInt("IdUsuario");
                    String nombre = resultSet.getString("NombreUsuario");
                    String apellidos = resultSet.getString("ApellidosUsuario");
                    String correoElectronico = resultSet.getString("CorreoElectronico");
                    String contraseniaUsuario = resultSet.getString("Contrasenia");
                    int numTelefono = resultSet.getInt("NumTelefono");

                    Usuario usuarioIniciado = new Usuario(idUsuario, nombre, apellidos, correoElectronico, contraseniaUsuario, numTelefono);

                    // Redirigir al usuario a la pantalla principal
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("usuarioIniciado", usuarioIniciado);
                    startActivity(intent);
                    finish(); // Cerrar la actividad actual

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