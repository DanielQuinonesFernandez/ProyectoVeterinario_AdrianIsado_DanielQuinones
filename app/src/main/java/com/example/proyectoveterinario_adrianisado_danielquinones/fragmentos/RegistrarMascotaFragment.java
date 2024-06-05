package com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.UsuarioCompartido;
import com.example.proyectoveterinario_adrianisado_danielquinones.databinding.FragmentRegistrarMascotaBinding;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class RegistrarMascotaFragment extends Fragment {

    private FragmentRegistrarMascotaBinding binding;
    private EditText etNombre, etEspecie, etFechaNacimiento;
    private ImageView imageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CAMERA_PERMISSION = 100;
    private Button btnCargarImagen;
    private Bitmap imageBitmap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegistrarMascotaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etFechaNacimiento = root.findViewById(R.id.etFechaNacimientoMascota);
        etNombre = root.findViewById(R.id.etNombreMascota);
        etEspecie = root.findViewById(R.id.etEspecieMascota);
        ImageView btnFechaNacimiento = root.findViewById(R.id.btnFechaNacimiento);
        imageView = root.findViewById(R.id.imageView);
        btnCargarImagen = root.findViewById(R.id.btnCargarImagen);
        TextView btnRegistrarMascota = root.findViewById(R.id.btnRegistrarMascota);
        TextView btnLimpiarCampos = root.findViewById(R.id.limpiarCamposRegMascota);

        //Toast.makeText(requireContext(), UsuarioCompartido.getUsuario().getNombre() + " " + UsuarioCompartido.getUsuario().getApellidos(), Toast.LENGTH_SHORT).show();

        btnRegistrarMascota.setOnClickListener(v -> {
            if(!hayCamposVacios()){
                registrarMascota();
            } else {
                Toast.makeText(getContext(), "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        btnFechaNacimiento.setOnClickListener(v -> showDatePickerDialog());

        btnCargarImagen.setOnClickListener(v -> {

            // Antes de iniciar la cámara, verificamos si tenemos permiso
            if (checkCameraPermission()) {
                dispatchTakePictureIntent();
            } else {
                // Si no tenemos permiso, solicitamos al usuario
                requestCameraPermission();
            }
        });

        btnLimpiarCampos.setOnClickListener(v -> limpiarCampos());

        imageView.setVisibility(View.GONE);

        imageView.setOnClickListener(v -> {
            // Mostrar el DialogFragment con la imagen en detalle
            mostrarDialogoImagenDetalle();
        });

        return root;
    }

    // Método para mostrar el DialogFragment con la imagen en detalle
    private void mostrarDialogoImagenDetalle() {
        ImageDetailDialogFragment dialogFragment = ImageDetailDialogFragment.newInstance(imageBitmap);
        dialogFragment.show(getParentFragmentManager(), "ImageDetailDialogFragment");
    }

    private void registrarMascota() {

        Connection connection = MySQLConnection.getConnection();

        try {
            String nombre = etNombre.getText().toString();
            String especie = etEspecie.getText().toString();
            String fechaNacString = etFechaNacimiento.getText().toString();

            String format = "dd/MM/yyyy";

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(format);
            java.util.Date fechaNacimientoUtil = dateFormat.parse(fechaNacString);

            // Convert java.util.Date to java.sql.Date
            assert fechaNacimientoUtil != null;
            java.sql.Date fechaNacimiento = new java.sql.Date(fechaNacimientoUtil.getTime());

            try {
                if (nombre.isEmpty() || especie.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show();
                } else {
                    // Construir la consulta INSERT INTO
                    String query = "INSERT INTO `Mascotas` (`NombreMascota`, `Especie`, `FechaNacimiento`, `Imagen`, `IdUsuario`) VALUES (?, ?, ?, ?, ?)";

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] imagenBytes = outputStream.toByteArray();

                    // Preparar la declaración SQL
                    PreparedStatement statement = connection.prepareStatement(query);

                    // Establecer los parámetros de la consulta
                    statement.setString(1, nombre);
                    statement.setString(2, especie);
                    statement.setDate(3, fechaNacimiento);
                    statement.setBytes(4, imagenBytes);
                    statement.setInt(5, UsuarioCompartido.getUsuario().getId());

                    // Ejecutar la consulta
                    int filasInsertadas = statement.executeUpdate();

                    // Verificar si se insertaron filas
                    if (filasInsertadas > 0) {
                        Toast.makeText(getContext(), "Mascota " + nombre + " registrada correctamente a nombre de " + UsuarioCompartido.getUsuario().getNombre(), Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    } else {
                        Toast.makeText(getContext(), "Error al registrar mascota", Toast.LENGTH_SHORT).show();
                    }

                    // Cerrar la conexión
                    connection.close();
                }
            } catch (SQLException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("RegistrarMascotaFragment", e.getMessage());
            }
        } catch (ParseException ignored) {}
    }

    private boolean hayCamposVacios(){
        return etNombre.getText().toString().isEmpty() || etEspecie.getText().toString().isEmpty() || etFechaNacimiento.getText().toString().isEmpty();
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etEspecie.setText("");
        etFechaNacimiento.setText("");
        imageView.setImageDrawable(null);
        imageView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Método para mostrar el DatePickerDialog
    private void showDatePickerDialog() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear un DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Mostrar la fecha seleccionada en el EditText
                        etFechaNacimiento.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);

        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }

    // Método para capturar una imagen
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    // Método para verificar si tenemos permiso de cámara
    private boolean checkCameraPermission() {
        // Verificar si el permiso de la cámara está concedido
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Método para solicitar el permiso de cámara
    private void requestCameraPermission() {
        // Solicitar el permiso de cámara al usuario
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA_PERMISSION);
    }

    // Método para manejar la respuesta de la solicitud de permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El usuario concedió el permiso, simulamos un clic en el botón de cargar imagen
                btnCargarImagen.performClick();
            } else {
                // El usuario negó el permiso, muestra un mensaje o toma otras acciones
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Método para recibir el resultado de la captura de imagen
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            requireActivity();
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
                imageView.setImageBitmap(imageBitmap);
                imageView.setVisibility(View.VISIBLE);
            }
        }
    }
}