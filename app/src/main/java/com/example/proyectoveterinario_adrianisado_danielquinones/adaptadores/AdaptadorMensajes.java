package com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.example.proyectoveterinario_adrianisado_danielquinones.MainActivity;
import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.UsuarioCompartido;
import com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos.MensajesFragment;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mascota;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mensaje;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdaptadorMensajes extends BaseAdapter {
    private final Context context;
    private final ArrayList<Mensaje> mensajes;

    public AdaptadorMensajes(Context context, ArrayList<Mensaje> mensajes) {
        this.context = context;
        this.mensajes = mensajes;
    }

    @Override
    public int getCount() {
        return mensajes.size();
    }

    @Override
    public Object getItem(int position) {
        return mensajes.get(position);
    }

    public long getItemId(int position) {
        return mensajes.get(position).getId();
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        view = layoutInflater.inflate(R.layout.item_mensaje, null);

        TextView tvTituloMensaje = view.findViewById(R.id.tvTituloMensaje);
        ImageButton btnBorrarMensaje = view.findViewById(R.id.btnBorrarMensaje);
        TextView tvTipoMensaje = view.findViewById(R.id.tvTipoMensaje);
        TextView tvContenidoMensaje = view.findViewById(R.id.tvContenidoMensaje);

        tvTituloMensaje.setText(mensajes.get(i).getAsunto());
        tvTipoMensaje.setText(mensajes.get(i).getTipoMensaje());
        tvContenidoMensaje.setText(mensajes.get(i).getContenido());

        btnBorrarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Connection connection = MySQLConnection.getConnection();

                    String sql = "DELETE FROM Mensajes Where IdMensaje = ? AND IdUsuario = ?";

                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setInt(1, mensajes.get(i).getId());
                    statement.setInt(2, UsuarioCompartido.getUsuario().getId());

                    statement.executeUpdate();

                    mensajes.remove(i);
                    notifyDataSetChanged();

                    connection.close();
                } catch (SQLException ignored){}
            }
        });

        return view;
    }
}
