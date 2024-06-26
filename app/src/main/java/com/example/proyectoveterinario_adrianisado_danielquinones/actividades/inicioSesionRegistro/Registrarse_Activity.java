package com.example.proyectoveterinario_adrianisado_danielquinones.actividades.inicioSesionRegistro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrarse_Activity extends AppCompatActivity {

    private Connection connection;
    private Context context;
    private EditText etNombre, etApellidos, etCorreoElectronico, etContrasenia, etRepiteContrasenia, etNumTelefono;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

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
        TextView btnRegistrarse = findViewById(R.id.btnRegistrarse);
        TextView btnLimpiarCampos = findViewById(R.id.btnLimpiarCampos);

        // Al pulsar el botón de registrarse, inserta el usuario en la base de datos
        btnRegistrarse.setOnClickListener(v -> registrarUsuario(connection, etNombre.getText().toString(), etApellidos.getText().toString(), etCorreoElectronico.getText().toString(), etContrasenia.getText().toString(), etNumTelefono.getText().toString()));
        btnLimpiarCampos.setOnClickListener(v -> limpiarCampos());
    }

    private void registrarUsuario(Connection connection, String nombreUsuario, String apellidosUsuario, String correoElectronico, String contrasenia, String numTelefono) {
        if (connection == null) {
            Toast.makeText(context, getString(R.string.error_al_conectar_con_la_base_de_datos), Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (etContrasenia.getText().toString().equals(etRepiteContrasenia.getText().toString())) {
                    if (!hayCamposVacios()) {
                        // Verificar si el correo electrónico ya existe
                        if (correoExiste(connection, correoElectronico)) {
                            Toast.makeText(context, getString(R.string.el_correo_electr_nico_ya_est_registrado), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Verificar si el número de teléfono ya existe
                        if (telefonoExiste(connection, numTelefono)) {
                            Toast.makeText(context, getString(R.string.el_n_mero_de_tel_fono_ya_est_registrado), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Construir la consulta INSERT INTO
                        String query = "INSERT INTO `Usuarios` (`NombreUsuario`, `ApellidosUsuario`, `CorreoElectronico`, `Contrasenia`, `numTelefono`, `EsAdmin`, `EstaVetado`) VALUES (?, ?, ?, ?, ?, ?, ?)";

                        // Preparar la declaración SQL
                        PreparedStatement statement = connection.prepareStatement(query);

                        // Establecer los parámetros de la consulta
                        statement.setString(1, nombreUsuario);
                        statement.setString(2, apellidosUsuario);
                        statement.setString(3, correoElectronico);
                        statement.setString(4, SeguridadContrasena.hashearContrasena(contrasenia));
                        statement.setInt(5, Integer.parseInt(numTelefono));
                        statement.setInt(6, 0); // 0 == false | 1 == true
                        statement.setInt(7, 0); // 0 == false | 1 == true

                        // Ejecutar la consulta
                        int filasInsertadas = statement.executeUpdate();

                        // Verificar si se insertaron filas
                        if (filasInsertadas > 0) {
                            Toast.makeText(context, getString(R.string.usuario) + nombreUsuario + getString(R.string.registrado_correctamente), Toast.LENGTH_SHORT).show();
                            iniciarSesion();
                            limpiarCampos();
                        } else {
                            Toast.makeText(context, getString(R.string.error_al_registrar_usuario), Toast.LENGTH_SHORT).show();
                        }

                        // Cerrar la conexión
                        statement.close();
                    } else {
                        Toast.makeText(context, getString(R.string.rellene_todos_los_campos), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.las_contrase_as_no_coinciden), Toast.LENGTH_SHORT).show();
                    etContrasenia.setText("");
                    etRepiteContrasenia.setText("");
                }
            } catch (SQLException | NumberFormatException e) {
                Toast.makeText(context, getString(R.string.error_al_realizar_la_inserci_n_del_usuario) + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void iniciarSesion() {
        // Obtener el correo electrónico y contraseña ingresados por el usuario
        String correo = etCorreoElectronico.getText().toString();
        String contrasenia = etContrasenia.getText().toString();

        if(esCorreoValido(correo)){
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
                        boolean esAdmin = resultSet.getBoolean("EsAdmin");
                        boolean estaVetado = resultSet.getBoolean("EstaVetado");
                        String razonVeto = resultSet.getString("RazonVeto");

                        Usuario usuarioIniciado = new Usuario(idUsuario, nombre, apellidos, correoElectronico, SeguridadContrasena.hashearContrasena(contraseniaUsuario), numTelefono, esAdmin, estaVetado, razonVeto);

                        // Redirigir al usuario a la pantalla principal
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("usuarioIniciado", usuarioIniciado);
                        startActivity(intent);
                        finish(); // Cerrar la actividad actual

                    } else {
                        // El usuario no existe en la base de datos o las credenciales son incorrectas
                        // Puedes mostrar un mensaje de error al usuario
                        Toast.makeText(context, getString(R.string.correo_electr_nico_o_contrase_a_incorrectos), Toast.LENGTH_SHORT).show();
                    }

                    resultSet.close();
                    connection.close();

                } catch (SQLException ignored) {}
            } else {
                // No se pudo establecer la conexión a la base de datos
                // Puedes mostrar un mensaje de error al usuario
                Toast.makeText(context, getString(R.string.error_al_conectar_con_la_base_de_datos), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, getString(R.string.el_correo_no_tiene_un_formato_v_lido), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean esCorreoValido(String email) {
        // Compilar la expresión regular
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        // Crear un matcher que comparará el correo con la expresión regular
        Matcher matcher = pattern.matcher(email);
        // Devolver true si el correo coincide con la expresión regular, false en caso contrario
        return matcher.matches();
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