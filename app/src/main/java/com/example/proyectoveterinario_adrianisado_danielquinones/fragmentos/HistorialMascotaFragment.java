package com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores.AdaptadorHistorialMedico;
import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.UsuarioCompartido;
import com.example.proyectoveterinario_adrianisado_danielquinones.databinding.FragmentHistorialMascotaBinding;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.HistorialMedico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class HistorialMascotaFragment extends Fragment {

    private FragmentHistorialMascotaBinding binding;
    private ListView lvHistorial;
    private ArrayList<HistorialMedico> historialesMedicos = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistorialMascotaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lvHistorial = root.findViewById(R.id.lvHistorial);
        lvHistorial.setAdapter(new AdaptadorHistorialMedico(root.getContext(), historialesMedicos));

        recogerHistorialesDeBBDD();
        //insertarHistorialesDePrueba();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void recogerHistorialesDeBBDD(){
        try {

            historialesMedicos.clear();

            Connection connection = MySQLConnection.getConnection();

            String sql = "SELECT Citas.* " +
                    "FROM Citas, Mascotas " +
                    "WHERE Citas.IdMascota = Mascotas.IdMascota " +
                    "AND (Mascotas.IdUsuario = ? OR Mascotas.IdUsuario = ?);";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "DEL" + String.valueOf(UsuarioCompartido.getUsuario().getId()));
            statement.setInt(2, UsuarioCompartido.getUsuario().getId());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                if(!resultSet.getString("TipoCita").equals("Otros - Por favor, consulte con el veterinario")){
                    int idCita = resultSet.getInt("IdCita");
                    String tituloCita = resultSet.getString("TipoCita");
                    String descripcionCita = resultSet.getString("DescripcionCita");
                    Date fechaCita = resultSet.getDate("FechaHora");
                    double importeHistorial = obtenerImporteCita(tituloCita);
                    int idMascota = resultSet.getInt("IdMascota");

                    historialesMedicos.add(new HistorialMedico(idCita, fechaCita, descripcionCita, importeHistorial, tituloCita, idMascota));
                } else {
                    int idCita = resultSet.getInt("IdCita");
                    String tituloCita = resultSet.getString("TipoCita");
                    String descripcionCita = resultSet.getString("DescripcionCita");
                    Date fechaCita = resultSet.getDate("FechaHora");
                    double importeHistorial = 0.0;
                    int idMascota = resultSet.getInt("IdMascota");

                    historialesMedicos.add(new HistorialMedico(idCita, fechaCita, descripcionCita, importeHistorial, tituloCita, idMascota));
                }
            }

            resultSet.close();
            connection.close();

        } catch (SQLException e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private double obtenerImporteCita(String citaElegida){
        //String[] partesCitaElegida = citaElegida.split("-");
        //return Double.parseDouble(partesCitaElegida[1]);

        // Eliminar el símbolo del euro y espacios en blanco al principio y al final
        if(!citaElegida.equals("Otros - Por favor, consulte con el veterinario")) {
            String cantidad = citaElegida.replaceAll("[^0-9.]", "").trim();
            return Double.parseDouble(cantidad);
        } else {
            return 0.0;
        }
    }

    private void insertarHistorialesDePrueba() {
        historialesMedicos.add(new HistorialMedico(1, new Date(2020 - 1900, 3, 6), "Consulta de rutina", 50.0, "Examen físico y revisión general", 1));
        historialesMedicos.add(new HistorialMedico(2, new Date(2023 - 1900, 11, 25), "Vacunación", 30.0, "Vacuna anual contra la rabia", 1));
        historialesMedicos.add(new HistorialMedico(3, new Date(2022 - 1900, 7, 10), "Control de peso", 25.0, "Seguimiento de la dieta y peso corporal", 1));
        historialesMedicos.add(new HistorialMedico(4, new Date(2024 - 1900, 2, 14), "Cirugía", 150.0, "Esterilización", 1));
        historialesMedicos.add(new HistorialMedico(5, new Date(2023 - 1900, 5, 30), "Consulta de emergencia", 80.0, "Tratamiento para infección respiratoria", 1));
        historialesMedicos.add(new HistorialMedico(6, new Date(2024 - 1900, 0, 1), "Chequeo dental", 40.0, "Limpieza y revisión de la salud dental", 1));
        historialesMedicos.add(new HistorialMedico(7, new Date(2023 - 1900, 8, 18), "Análisis de sangre", 60.0, "Control de parásitos y enfermedades", 1));
        historialesMedicos.add(new HistorialMedico(8, new Date(2022 - 1900, 4, 20), "Tratamiento de heridas", 35.0, "Limpieza y vendaje de heridas", 1));
        historialesMedicos.add(new HistorialMedico(9, new Date(2023 - 1900, 6, 8), "Consulta dermatológica", 70.0, "Tratamiento para alergias cutáneas", 1));
        historialesMedicos.add(new HistorialMedico(10, new Date(2019 - 1900, 1, 28), "Examen de sangre", 55.0, "Control de niveles hormonales", 1));

    }

}