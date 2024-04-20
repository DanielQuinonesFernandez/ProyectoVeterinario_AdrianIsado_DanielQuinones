package com.example.proyectoveterinario_adrianisado_danielquinones.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        try {
            UsuarioCompartido.setUsuario(null);
            Intent intent = new Intent(getContext(), IniciarSesion_Registrarse_Activity.class);
            startActivity(intent);
        } catch (Exception ignored) {}

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}