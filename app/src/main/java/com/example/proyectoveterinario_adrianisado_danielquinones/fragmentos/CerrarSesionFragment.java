package com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.UsuarioCompartido;
import com.example.proyectoveterinario_adrianisado_danielquinones.actividades.inicioSesionRegistro.IniciarSesion_Registrarse_Activity;
import com.example.proyectoveterinario_adrianisado_danielquinones.databinding.FragmentCerrarSesionBinding;

public class CerrarSesionFragment extends Fragment {

    private FragmentCerrarSesionBinding binding;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCerrarSesionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getContext();

        mostrarDialogoConfirmacion();

        return root;
    }

    private void mostrarDialogoConfirmacion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.confirmaci_n);
        builder.setMessage(R.string.est_s_seguro_de_que_quieres_cerrar_la_sesi_n_de_tu_usuario);
        builder.setIcon(R.drawable.baseline_info_outline_24);

        builder.setPositiveButton(getString(R.string.si), (dialogInterface, i) -> cerrarSesion());

        builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> noCierraSesion());

        builder.show();
    }

    private void noCierraSesion(){
        Navigation.findNavController(requireView()).navigate(R.id.nav_miPerfil);
    }

    private void cerrarSesion(){
        try {
            UsuarioCompartido.setUsuario(null);
            Intent intent = new Intent(getContext(), IniciarSesion_Registrarse_Activity.class);
            startActivity(intent);
        } catch (Exception ignored) {}
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}