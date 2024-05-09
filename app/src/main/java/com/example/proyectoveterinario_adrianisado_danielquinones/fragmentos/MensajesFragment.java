package com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.UsuarioCompartido;
import com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores.AdaptadorMensajeRecyclerView;
import com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores.AdaptadorSpinnerFiltro;
import com.example.proyectoveterinario_adrianisado_danielquinones.databinding.FragmentMensajesBinding;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mensaje;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class MensajesFragment extends Fragment {

    private FragmentMensajesBinding binding;
    public Context context;
    private final ArrayList<Mensaje> mensajes = new ArrayList<>();
    private final ArrayList<String> opcionesFiltro = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = requireContext();



        binding = FragmentMensajesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.lvMensajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new FadeInAnimator(new OvershootInterpolator(5f)));

        AlphaInAnimationAdapter alphaInAnimation = new AlphaInAnimationAdapter(new AdaptadorMensajeRecyclerView(getContext(), mensajes));
        alphaInAnimation.setDuration(500);
        alphaInAnimation.setInterpolator(new OvershootInterpolator(1f));
        alphaInAnimation.setFirstOnly(false);

        recogerMensajesDeBBDD("Todos");
        recogerOpcionesFiltro();

        recyclerView.setAdapter(new AlphaInAnimationAdapter(alphaInAnimation));

        Spinner spinnerFiltro = root.findViewById(R.id.spinnerFiltrarPor);

        AdaptadorSpinnerFiltro adaptadorSpinnerFiltro = new AdaptadorSpinnerFiltro(root.getContext(), opcionesFiltro);
        spinnerFiltro.setAdapter(adaptadorSpinnerFiltro);

        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String tipoSeleccionado = opcionesFiltro.get(position);
                recogerMensajesDeBBDD(tipoSeleccionado);
                recyclerView.setAdapter(new AlphaInAnimationAdapter(alphaInAnimation));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


        return root;
    }

    private void recogerMensajesDeBBDD(String tipoSeleccionado) {
        try {
            mensajes.clear();
            Connection connection = MySQLConnection.getConnection();

            String sql;
            if ("Todos".equalsIgnoreCase(tipoSeleccionado)) {
                sql = "SELECT * FROM Mensajes WHERE IdUsuario = ?";
            } else {
                sql = "SELECT * FROM Mensajes WHERE IdUsuario = ? AND TipoMensaje = ?";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, UsuarioCompartido.getUsuario().getId());
            if (!"Todos".equalsIgnoreCase(tipoSeleccionado)) {
                statement.setString(2, tipoSeleccionado);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idMensaje = resultSet.getInt("IdMensaje");
                int idUsuario = resultSet.getInt("IdUsuario");
                String titulo = resultSet.getString("AsuntoMensaje");
                String contenido = resultSet.getString("ContenidoMensaje");
                Date fecha = resultSet.getDate("FechaHoraMensaje");
                String tipo = resultSet.getString("TipoMensaje");
                boolean leido = resultSet.getBoolean("Leido");

                mensajes.add(new Mensaje(idMensaje, idUsuario, titulo, contenido, fecha, tipo, leido));
            }

            resultSet.close();
            connection.close();

        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void recogerOpcionesFiltro(){
        opcionesFiltro.clear();
        opcionesFiltro.add("Todos");
        for(int i = 0; i < mensajes.size(); i++){
            if(!opcionesFiltro.contains(mensajes.get(i).getTipoMensaje())){
                opcionesFiltro.add(mensajes.get(i).getTipoMensaje());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}