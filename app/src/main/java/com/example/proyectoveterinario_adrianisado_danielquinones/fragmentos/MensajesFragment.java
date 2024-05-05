package com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.UsuarioCompartido;
import com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores.AdaptadorMensajes;
import com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores.AdaptadorSpinnerFiltro;
import com.example.proyectoveterinario_adrianisado_danielquinones.databinding.FragmentMensajesBinding;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mensaje;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class MensajesFragment extends Fragment {

    private Spinner spinnerFiltro;
    private FragmentMensajesBinding binding;
    public Context context;
    private ArrayList<Mensaje> mensajes = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMensajesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView lvMensajes = root.findViewById(R.id.lvMensajes);
        spinnerFiltro = root.findViewById(R.id.spinnerFiltrarPor);

        AdaptadorMensajes adaptadorMensajes = new AdaptadorMensajes(root.getContext(), mensajes);
        lvMensajes.setAdapter(adaptadorMensajes);
        adaptadorMensajes.notifyDataSetChanged();

        AdaptadorSpinnerFiltro adaptadorSpinnerFiltro = new AdaptadorSpinnerFiltro(root.getContext(), mensajes);
        spinnerFiltro.setAdapter(adaptadorSpinnerFiltro);

        recogerMensajesDeBBDD();

        return root;
    }

    private void recogerMensajesDeBBDD(){
        try{
            mensajes.clear();
            Connection connection = MySQLConnection.getConnection();

            String sql = "SELECT * FROM Mensajes WHERE IdUsuario = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, UsuarioCompartido.getUsuario().getId());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
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

        }catch (SQLException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}