package com.example.proyectoveterinario_adrianisado_danielquinones.actividades.inicioSesionRegistro;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.SeguridadContrasena;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContrasenaOlvidadaActivity extends AppCompatActivity {

    private Button btnEnviarSMS;
    private EditText etNumeroTelefono;
    private String nuevaContrasena = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contrasena_olvidada);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nuevaContrasena = generarContrasena(10);

        etNumeroTelefono = findViewById(R.id.etNumTelefonoPasswdOlvidada);
        btnEnviarSMS = findViewById(R.id.btnEnviarSMS);
        Button btnAtras = findViewById(R.id.btnAtras);

        btnAtras.setOnClickListener(v -> finish());

        btnEnviarSMS.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                String numeroTelefono = etNumeroTelefono.getText().toString();
                if (numTelefonoExisteEnBBDD(numeroTelefono)) {
                    sendSMS(numeroTelefono);
                } else {
                    Toast.makeText(this, "El número de telefono no existe", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 100);
            }

            btnEnviarSMS.setEnabled(false);
            new Handler().postDelayed(() -> btnEnviarSMS.setEnabled(true), 5000);
        });
    }

    private boolean numTelefonoExisteEnBBDD(String numeroTelefono) {
        Connection connection = MySQLConnection.getConnection();
        boolean existe = false;

        try {

            String sql = "SELECT NumTelefono FROM Usuarios WHERE NumTelefono = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, numeroTelefono);
            ResultSet resultSet = statement.executeQuery();

            existe = resultSet.next();

        } catch (SQLException ignored) {
        }

        return existe;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendSMS(etNumeroTelefono.getText().toString());
        } else {
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(String numeroTelefono) {
        if (!numeroTelefono.isEmpty()) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numeroTelefono, null, "Tu nueva contraseña es: " + nuevaContrasena, null, null);
            Toast.makeText(this, "SMS enviado", Toast.LENGTH_SHORT).show();
            reiniciarContrasena(numeroTelefono);
            copiarPasswdAlPortapapeles();
        } else {
            Toast.makeText(this, "Ingrese un número de teléfono", Toast.LENGTH_SHORT).show();
        }
    }

    private void copiarPasswdAlPortapapeles(){
        // Obtener el servicio del portapapeles
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        // Crear un ClipData para almacenar el texto
        ClipData clip = ClipData.newPlainText("Contraseña copiada", nuevaContrasena);

        // Establecer el ClipData en el portapapeles
        clipboard.setPrimaryClip(clip);

        // Mostrar un mensaje de confirmación
        Toast.makeText(this, "Contraseña copiada al portapapeles", Toast.LENGTH_SHORT).show();
    }

    private String generarContrasena(int longitud) {
        // Caracteres posibles para generar la contraseña
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&!?";

        // Usamos SecureRandom para mayor seguridad en la generación
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(longitud);

        // Generamos la contraseña caracter por caracter
        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(indice));
        }

        return sb.toString();
    }

    private void reiniciarContrasena(String numeroTelefono) {
        Connection connection = MySQLConnection.getConnection();

        try {
            String sql = "UPDATE Usuarios SET Contrasenia = ? WHERE NumTelefono = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, SeguridadContrasena.hashearContrasena(nuevaContrasena));
            statement.setString(2, numeroTelefono);

            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException ignored) {
        }
    }
}