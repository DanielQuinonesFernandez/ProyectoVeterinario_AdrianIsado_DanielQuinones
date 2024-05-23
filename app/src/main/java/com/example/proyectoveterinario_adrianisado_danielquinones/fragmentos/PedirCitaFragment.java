package com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.UsuarioCompartido;
import com.example.proyectoveterinario_adrianisado_danielquinones.databinding.FragmentPedirCitaBinding;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mascota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PedirCitaFragment extends Fragment {

    private FragmentPedirCitaBinding binding;
    private final ArrayList<Mascota> mascotasDeEsteUsuario = new ArrayList<>();
    private EditText etDescripcionCita, edOtroTipoCita, etFechaCita;
    private Spinner spinnerSelecMascota, spinnerSelecTipoCita;
    private TimePicker timePicker;
    private TextView tvFechaHoraSeleccionada;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPedirCitaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvFechaHoraSeleccionada = root.findViewById(R.id.tvFechaSeleccionada);

        ImageButton btnFechaCita = root.findViewById(R.id.btnFechaCita);

        timePicker = root.findViewById(R.id.timePickerPedirCita);

        edOtroTipoCita = root.findViewById(R.id.edOtroTipoCita);
        etDescripcionCita = root.findViewById(R.id.etOtrosDatosRelevantes);
        etFechaCita = root.findViewById(R.id.etFechaCita);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();
            etFechaCita.setText(dtf.format(now));
        } else {
            etFechaCita.setText(R.string.fecha_por_defecto);
        }

        rellenarArraylistDesdeBBDD();

        spinnerSelecMascota = root.findViewById(R.id.combobox_seleccionMascota);
        ArrayAdapter<Mascota> adapterSelecMascota = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item, mascotasDeEsteUsuario);
        adapterSelecMascota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelecMascota.setAdapter(adapterSelecMascota);


        spinnerSelecTipoCita = root.findViewById(R.id.combobox_seleccionTipoCita);
        ArrayAdapter<CharSequence> adapterSelecTipoCita = ArrayAdapter.createFromResource(root.getContext(),
                R.array.tipos_de_cita, android.R.layout.simple_spinner_item);
        adapterSelecTipoCita.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelecTipoCita.setAdapter(adapterSelecTipoCita);

        btnFechaCita.setOnClickListener(v -> mostrarCalendario());

        Button btnPedirCita = root.findViewById(R.id.btnPedirCita);

        btnPedirCita.setOnClickListener(v -> pedirCita());

        etFechaCita.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int hora = timePicker.getHour();
                @SuppressLint("DefaultLocale") String minutoStr = String.format("%02d", timePicker.getMinute());

                tvFechaHoraSeleccionada.setText("Fecha seleccionada: " + etFechaCita.getText().toString() + " " + hora + ":" + minutoStr);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int hora = timePicker.getHour();
                @SuppressLint("DefaultLocale") String minutoStr = String.format("%02d", timePicker.getMinute());

                tvFechaHoraSeleccionada.setText("Fecha seleccionada: " + etFechaCita.getText().toString() + " " + hora + ":" + minutoStr);
            }
        });

        spinnerSelecTipoCita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                edOtroTipoCita.setText("");

                if (spinnerSelecTipoCita.getSelectedItem().toString().equals("Otros - Por favor, consulte con el veterinario")) {
                    edOtroTipoCita.setVisibility(View.VISIBLE);
                    edOtroTipoCita.requestFocus();
                } else {
                    edOtroTipoCita.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    private void rellenarArraylistDesdeBBDD() {
        try {
            Connection connection = MySQLConnection.getConnection();

            String sql = "SELECT * FROM Mascotas WHERE idUsuario = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, UsuarioCompartido.getUsuario().getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idMascota = resultSet.getInt("IdMascota");
                String nombreMascota = resultSet.getString("NombreMascota");
                String especie = resultSet.getString("Especie");
                Date fechaNacimiento = resultSet.getDate("FechaNacimiento");
                byte[] imagenBytes = resultSet.getBytes("Imagen");
                Bitmap imagenMascota = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                String idUsuario = resultSet.getString("IdUsuario");

                mascotasDeEsteUsuario.add(new Mascota(idMascota, nombreMascota, especie, fechaNacimiento, imagenMascota, idUsuario));
            }

            resultSet.close();
            connection.close();

        } catch (SQLException ignored) {
        }
    }

    private void pedirCita() {
        try {
            int hora = timePicker.getHour();
            @SuppressLint("DefaultLocale") String minutoStr = String.format("%02d", timePicker.getMinute());

            Connection connection = MySQLConnection.getConnection();

            String sql = "INSERT INTO Citas (TipoCita, FechaHora, DescripcionCita, IdMascota) VALUES (?, ?, ?, ?)";
            int idMascotaElegida = obtenerIdMascota(spinnerSelecMascota.getSelectedItem().toString());

            PreparedStatement statement = connection.prepareStatement(sql);
            if (!spinnerSelecTipoCita.getSelectedItem().toString().equals("Otros - Por favor, consulte con el veterinario")) {
                statement.setString(1, spinnerSelecTipoCita.getSelectedItem().toString());
            } else {
                statement.setString(1, edOtroTipoCita.getText().toString().trim() + " - 0.0€");
            }
            statement.setString(2, etFechaCita.getText().toString() + " " + hora + ":" + minutoStr);
            statement.setString(3, "Para: " + spinnerSelecMascota.getSelectedItem().toString().toUpperCase() + "\n" + etDescripcionCita.getText().toString());
            statement.setInt(4, idMascotaElegida);

            statement.executeUpdate();

            statement.close();
            connection.close();

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Cita pedida para la mascota: " + spinnerSelecMascota.getSelectedItem().toString());
            builder.setMessage("Has pedido correctamente la cita:\n" +
                    "- Fecha: " + etFechaCita.getText().toString() + " " + hora + ":" + minutoStr + "\n" +
                    "- Tipo de cita: " + spinnerSelecTipoCita.getSelectedItem().toString() + "\n" +
                    "- Descripción: " + etDescripcionCita.getText().toString() + "\n" +
                    "- Mascota: " + spinnerSelecMascota.getSelectedItem().toString());
            builder.setPositiveButton("OK",null);
            builder.create();
            builder.show();

            limpiarCampos();
        } catch (SQLException ignored) {
        }
    }

    private int obtenerIdMascota(String mascotaElegia) {
        for (Mascota m : mascotasDeEsteUsuario) {
            if ((m.getNombre() + " [" + m.getEspecie() + "]").equalsIgnoreCase(mascotaElegia)) {
                return m.getId();
            }
        }
        return 0;
    }

    private void mostrarCalendario() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear un DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Mostrar la fecha seleccionada en el EditText
                        etFechaCita.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%04d", year));
                    }
                }, year, month, day);

        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }

    private void limpiarCampos() {
        spinnerSelecMascota.setSelection(0);
        spinnerSelecTipoCita.setSelection(0);
        etDescripcionCita.setText("");
        tvFechaHoraSeleccionada.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}