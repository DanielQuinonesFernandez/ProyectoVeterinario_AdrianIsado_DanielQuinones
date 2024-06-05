package com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.UsuarioCompartido;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mensaje;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdaptadorMensajeRecyclerView extends RecyclerView.Adapter<AdaptadorMensajeRecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<Mensaje> mensajes;

    public AdaptadorMensajeRecyclerView(Context context, ArrayList<Mensaje> mensajes) {
        this.context = context;
        this.mensajes = mensajes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mensaje, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Mensaje mensaje = mensajes.get(position);

        holder.tvTituloMensaje.setText(mensaje.getAsunto());
        holder.tvTipoMensaje.setText(mensaje.getTipoMensaje());
        holder.tvContenidoMensaje.setText(mensaje.getContenido());
        holder.btnBorrarMensaje.setOnClickListener(v -> {
            try{
                Connection connection = MySQLConnection.getConnection();

                String sql = "DELETE FROM Mensajes Where IdMensaje = ? AND IdUsuario = ?";

                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, mensajes.get(position).getId());
                statement.setInt(2, UsuarioCompartido.getUsuario().getId());

                statement.executeUpdate();

                mensajes.remove(position);
                notifyItemRemoved(position);

                connection.close();
            } catch (SQLException ignored){}
        });
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTituloMensaje, tvTipoMensaje, tvContenidoMensaje;
        ImageButton btnBorrarMensaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnBorrarMensaje = itemView.findViewById(R.id.btnBorrarMensaje);
            tvTituloMensaje = itemView.findViewById(R.id.tvTituloMensaje);
            tvTipoMensaje = itemView.findViewById(R.id.tvTipoMensaje);
            tvContenidoMensaje = itemView.findViewById(R.id.tvContenidoMensaje);
        }
    }
}

