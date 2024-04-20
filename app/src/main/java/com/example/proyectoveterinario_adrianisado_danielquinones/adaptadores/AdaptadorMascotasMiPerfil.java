package com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.UsuarioCompartido;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mascota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class AdaptadorMascotasMiPerfil extends BaseAdapter {
    private final Context context;
    private final ArrayList<Mascota> mascotas;

    public AdaptadorMascotasMiPerfil(Context context, ArrayList<Mascota> mascotas) {
        this.context = context;
        this.mascotas = mascotas;
    }

    @Override
    public int getCount() {
        return mascotas.size();
    }

    @Override
    public Object getItem(int position) {
        return mascotas.get(position);
    }

    public long getItemId(int position) {
        return mascotas.get(position).getId();
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        view = layoutInflater.inflate(R.layout.item_mascotas_perfil, null);

        ImageView imagenMascota = view.findViewById(R.id.imagenMascotaItem);
        TextView tvNombreMascota = view.findViewById(R.id.tvNombreMascota);
        TextView tvDescripcionMascota = view.findViewById(R.id.tvDescripcionMascota);
        ImageView iconoEliminarMascota = view.findViewById(R.id.iconoEliminarMascota);

        tvNombreMascota.setText(mascotas.get(i).getNombre());
        tvDescripcionMascota.setText(mascotas.get(i).getEspecie());
        imagenMascota.setImageBitmap(mascotas.get(i).getFotoMascota());
        iconoEliminarMascota.setOnClickListener(v -> {
            Mascota m = (Mascota) getItem(i);
            for (Mascota mascota : mascotas) {
                if (mascota.getId() == m.getId()) {
                    Toast.makeText(context, m.getNombre() + " eliminado de tu lista", Toast.LENGTH_SHORT).show();
                    eliminarMascotaDeBBDD(mascota);
                    mascotas.remove(mascota);
                    notifyDataSetChanged();
                    break;
                }
            }
        });

        return view;
    }

    private void eliminarMascotaDeBBDD(Mascota mascota) {
        try {
            Connection connection = MySQLConnection.getConnection();

            // Eliminar la mascota
            String deleteMascotaQuery = "UPDATE Mascotas SET IdUsuario = ? WHERE IdMascota = ?;";
            PreparedStatement deleteMascotaStatement = connection.prepareStatement(deleteMascotaQuery);
            deleteMascotaStatement.setString(1, "DEL" + UsuarioCompartido.getUsuario().getId());
            deleteMascotaStatement.setInt(2, mascota.getId());
            deleteMascotaStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.println(Log.ERROR, "AdaptadorMascotasMiPerfil", Objects.requireNonNull(e.getMessage()));
        }
    }
}
