package com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoveterinario_adrianisado_danielquinones.MySQLConnection;
import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.SeguridadContrasena;
import com.example.proyectoveterinario_adrianisado_danielquinones.UsuarioCompartido;
import com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores.AdaptadorMascotasMiPerfil;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mascota;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MiPerfilFragment extends Fragment {

    private Usuario usuario;
    private final ArrayList<Mascota> mascotas = new ArrayList<>();
    private EditText etContrasenaActual, etContrasenaNueva;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Obtener la vista raíz del fragmento
        View root = inflater.inflate(R.layout.fragment_mi_perfil, container, false);

        TextView tvNombreYApellidos = root.findViewById(R.id.tvNombreYApellidos);
        TextView tvCorreo = root.findViewById(R.id.tvEmail);
        ListView lvMascotas = root.findViewById(R.id.listViewMascotasUsuario);
        Button btnCambiarContrasena = root.findViewById(R.id.btnCambiarPasswd);
        etContrasenaActual = root.findViewById(R.id.edPasswdActual);
        etContrasenaNueva = root.findViewById(R.id.edPasswdNueva);


        AdaptadorMascotasMiPerfil adaptador = new AdaptadorMascotasMiPerfil(root.getContext(), mascotas);
        lvMascotas.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();

        recogerMascotasUsuario();

        usuario = UsuarioCompartido.getUsuario();
        tvNombreYApellidos.setText(usuario.getNombre() + " " + usuario.getApellidos());
        tvCorreo.setText(usuario.getCorreoElectronico());

        btnCambiarContrasena.setOnClickListener(v -> cambiarContrasena());

        return root;
    }

    private void recogerMascotasUsuario() {

        mascotas.clear();

        Connection connection = MySQLConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Mascotas WHERE IdUsuario = ?");
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

                mascotas.add(new Mascota(idMascota, nombreMascota, especie, fechaNacimiento, imagenMascota, idUsuario));
            }

            resultSet.close();
            connection.close();
        } catch (SQLException ignored) {
        }
    }

    private void cambiarContrasena() {

        String passwdActual = etContrasenaActual.getText().toString();
        String passwdActualHasheada = SeguridadContrasena.hashearContrasena(passwdActual);

        String passwdNueva = etContrasenaNueva.getText().toString();
        String passwdNuevaHasheada = SeguridadContrasena.hashearContrasena(passwdNueva);

        String sql = "UPDATE Usuarios SET Contrasenia = ? WHERE IdUsuario = ?";

        try {
            Connection connection = MySQLConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, passwdNuevaHasheada);
            statement.setInt(2, usuario.getId());

            if (Objects.equals(passwdActualHasheada, usuario.getContrasenia())) {
                if(usuario.getContrasenia().equals(passwdNuevaHasheada)) {
                    Toast.makeText(getContext(), "La nueva contraseña no puede ser igual que la actual", Toast.LENGTH_SHORT).show();
                } else {
                    int filasActualizadas = statement.executeUpdate();

                    if (filasActualizadas > 0) {
                        UsuarioCompartido.getUsuario().setContrasenia(passwdNuevaHasheada);
                        Toast.makeText(getContext(), "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "No se ha podido cambiar la contraseña", Toast.LENGTH_SHORT).show();
                    }
                }
                limpiarCampos();
            } else {
                Toast.makeText(getContext(), "La contraseña actual no coincide", Toast.LENGTH_SHORT).show();
            }

            connection.close();

        } catch (SQLException ignored) {
        }
    }

    private void limpiarCampos() {
        etContrasenaActual.setText("");
        etContrasenaNueva.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
