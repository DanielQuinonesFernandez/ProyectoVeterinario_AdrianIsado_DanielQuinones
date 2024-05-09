package com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.UsuarioCompartido;
import com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores.AdaptadorHistorialMedicoRecyclerView;
import com.example.proyectoveterinario_adrianisado_danielquinones.databinding.FragmentHistorialMascotaBinding;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.HistorialMedico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class HistorialMascotaFragment extends Fragment {

    private FragmentHistorialMascotaBinding binding;
    private final ArrayList<HistorialMedico> historialesMedicos = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_historial_mascota, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rvHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new FadeInAnimator(new OvershootInterpolator(5f)));

        ScaleInAnimationAdapter scaleAnimation = new ScaleInAnimationAdapter(new AdaptadorHistorialMedicoRecyclerView(getContext(), historialesMedicos));
        scaleAnimation.setDuration(500);
        scaleAnimation.setInterpolator(new OvershootInterpolator(1f));
        scaleAnimation.setFirstOnly(false);

        recyclerView.setAdapter(new ScaleInAnimationAdapter(scaleAnimation));

        recogerHistorialesDeBBDD();

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
            statement.setString(1, "DEL" + UsuarioCompartido.getUsuario().getId());
            statement.setInt(2, UsuarioCompartido.getUsuario().getId());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                if(!resultSet.getString("TipoCita").equals("Otros - Por favor, consulte con el veterinario")){
                    int idCita = resultSet.getInt("IdCita");
                    String tituloCita = resultSet.getString("TipoCita");
                    String descripcionCita = resultSet.getString("DescripcionCita");
                    String fechaCita = resultSet.getString("FechaHora");
                    double importeHistorial = obtenerImporteCita(tituloCita);
                    int idMascota = resultSet.getInt("IdMascota");

                    historialesMedicos.add(new HistorialMedico(idCita, fechaCita, descripcionCita, importeHistorial, tituloCita, idMascota));
                } else {
                    int idCita = resultSet.getInt("IdCita");
                    String tituloCita = resultSet.getString("TipoCita");
                    String descripcionCita = resultSet.getString("DescripcionCita");
                    String fechaCita = resultSet.getString("FechaHora");
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

        // Eliminar el símbolo del euro y espacios en blanco al principio y al final
        if(!citaElegida.equals("Otros - Por favor, consulte con el veterinario")) {
            String cantidad = citaElegida.replaceAll("[^0-9.]", "").trim();
            return Double.parseDouble(cantidad);
        } else {
            return 0.0;
        }
    }

}