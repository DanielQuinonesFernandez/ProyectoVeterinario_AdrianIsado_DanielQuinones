package com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_mascotas_perfil, null);
        }

        ImageView imagenMascota = view.findViewById(R.id.imagenMascotaItem);
        TextView tvNombreMascota = view.findViewById(R.id.tvNombreMascota);
        TextView tvDescripcionMascota = view.findViewById(R.id.tvDescripcionMascota);
        ImageView iconoEliminarMascota = view.findViewById(R.id.iconoEliminarMascota);

        Mascota mascota = mascotas.get(i);

        tvNombreMascota.setText(mascota.getNombre());
        tvDescripcionMascota.setText(mascota.getEspecie());
        imagenMascota.setImageBitmap(mascota.getFotoMascota());

        iconoEliminarMascota.setOnClickListener(v -> mostrarDialogoConfirmacionEliminarMascota(mascota));

        return view;
    }

    private void mostrarDialogoConfirmacionEliminarMascota(@NonNull final Mascota mascota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Eliminar Mascota");
        builder.setMessage("¿Estás seguro de que quieres eliminar a " + mascota.getNombre() + "?");
        builder.setIcon(R.drawable.baseline_info_outline_24);

        builder.setPositiveButton("Sí", (dialogInterface, i) -> eliminarMascotaDeBBDD(mascota));

        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }

    private void eliminarMascotaDeBBDD(@NonNull Mascota mascota) {
        try {
            Connection connection = MySQLConnection.getConnection();

            // Eliminar la mascota
            String deleteMascotaQuery = "DELETE FROM Mascotas WHERE IdMascota = ?;";
            PreparedStatement deleteMascotaStatement = connection.prepareStatement(deleteMascotaQuery);
            deleteMascotaStatement.setInt(1, mascota.getId());
            deleteMascotaStatement.executeUpdate();

            connection.close();

            // Eliminar la mascota de la lista local y actualizar la vista
            mascotas.remove(mascota);
            notifyDataSetChanged();

            Toast.makeText(context, mascota.getNombre() + context.getString(R.string.eliminado_de_tu_lista), Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
