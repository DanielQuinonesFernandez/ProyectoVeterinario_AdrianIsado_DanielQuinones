package com.example.proyectoveterinario_adrianisado_danielquinones.actividades.inicioSesionRegistro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class Registrarse_Activity extends AppCompatActivity {

    private Connection connection;
    private Context context;
    private EditText etNombre, etApellidos, etCorreoElectronico, etContrasenia, etRepiteContrasenia, etNumTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        context = this;

        connection = MySQLConnection.getConnection();

        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etCorreoElectronico = findViewById(R.id.etCorreoElectronico);
        etContrasenia = findViewById(R.id.etContrasena);
        etRepiteContrasenia = findViewById(R.id.etRepiteContrasena);
        etNumTelefono = findViewById(R.id.etNumTelefono);
        Button btnRegistrarse = findViewById(R.id.btnRegistrarse);
        Button btnLimpiarCampos = findViewById(R.id.btnLimpiarCampos);

        // Al pulsar el botón de registrarse, inserta el usuario en la base de datos
        btnRegistrarse.setOnClickListener(v -> registrarUsuario(connection, etNombre.getText().toString(), etApellidos.getText().toString(), etCorreoElectronico.getText().toString(), etContrasenia.getText().toString(), etNumTelefono.getText().toString()));
        btnLimpiarCampos.setOnClickListener(v -> limpiarCampos());
    }

    private void registrarUsuario(Connection connection, String nombreUsuario, String apellidosUsuario, String correoElectronico, String contrasenia, String numTelefono) {
        if (connection == null) {
            Toast.makeText(context, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (etContrasenia.getText().toString().equals(etRepiteContrasenia.getText().toString())) {
                    if (!hayCamposVacios()) {
                        // Verificar si el correo electrónico ya existe
                        if (correoExiste(connection, correoElectronico)) {
                            Toast.makeText(context, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Verificar si el número de teléfono ya existe
                        if (telefonoExiste(connection, numTelefono)) {
                            Toast.makeText(context, "El número de teléfono ya está registrado", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Construir la consulta INSERT INTO
                        String query = "INSERT INTO `Usuarios` (`NombreUsuario`, `ApellidosUsuario`, `CorreoElectronico`, `Contrasenia`, `numTelefono`) VALUES (?, ?, ?, ?, ?)";

                        // Preparar la declaración SQL
                        PreparedStatement statement = connection.prepareStatement(query);

                        // Establecer los parámetros de la consulta
                        statement.setString(1, nombreUsuario);
                        statement.setString(2, apellidosUsuario);
                        statement.setString(3, correoElectronico);
                        statement.setString(4, SeguridadContrasena.hashearContrasena(contrasenia));
                        statement.setInt(5, Integer.parseInt(numTelefono));

                        // Ejecutar la consulta
                        int filasInsertadas = statement.executeUpdate();

                        // Verificar si se insertaron filas
                        if (filasInsertadas > 0) {
                            Toast.makeText(context, "Usuario " + nombreUsuario + " registrado correctamente", Toast.LENGTH_SHORT).show();
                            iniciarSesion();
                            limpiarCampos();
                        } else {
                            Toast.makeText(context, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                        }

                        // Cerrar la conexión
                        statement.close();
                    } else {
                        Toast.makeText(context, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    etContrasenia.setText("");
                    etRepiteContrasenia.setText("");
                }
            } catch (SQLException | NumberFormatException e) {
                Toast.makeText(context, "Error al realizar la inserción del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void iniciarSesion() {
        // Obtener el correo electrónico y contraseña ingresados por el usuario
        String correo = etCorreoElectronico.getText().toString();
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

                    Usuario usuarioIniciado = new Usuario(idUsuario, nombre, apellidos, correoElectronico, SeguridadContrasena.hashearContrasena(contraseniaUsuario), numTelefono);

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

            } catch (SQLException ignored) {}
        } else {
            // No se pudo establecer la conexión a la base de datos
            // Puedes mostrar un mensaje de error al usuario
            Toast.makeText(context, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean correoExiste(@NonNull Connection connection, String correoElectronico) throws SQLException {
        String query = "SELECT COUNT(*) FROM Usuarios WHERE CorreoElectronico = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, correoElectronico);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        statement.close();
        return count > 0;
    }

    private boolean telefonoExiste(@NonNull Connection connection, String numTelefono) throws SQLException {
        String query = "SELECT COUNT(*) FROM Usuarios WHERE numTelefono = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, numTelefono);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        statement.close();
        return count > 0;
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etApellidos.setText("");
        etCorreoElectronico.setText("");
        etContrasenia.setText("");
        etRepiteContrasenia.setText("");
        etNumTelefono.setText("");
    }

    private boolean hayCamposVacios() {
        return etNombre.getText().toString().isEmpty() ||
                etApellidos.getText().toString().isEmpty() ||
                etCorreoElectronico.getText().toString().isEmpty() ||
                etContrasenia.getText().toString().isEmpty() ||
                etRepiteContrasenia.getText().toString().isEmpty() ||
                etNumTelefono.getText().toString().isEmpty();
    }
}