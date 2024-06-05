package com.example.proyectoveterinario_adrianisado_danielquinones.actividades.inicioSesionRegistro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoveterinario_adrianisado_danielquinones.R;

public class IniciarSesion_Registrarse_Activity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion_registrarse);

        context = this;

        TextView btnRegistrarse = findViewById(R.id.btnRegistrarse);
        TextView btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // Para cuando se presione el boton de Registrarse, nos llevara a la pantalla de Registrarse
        btnRegistrarse.setOnClickListener(v -> {
            Intent i = new Intent(context, Registrarse_Activity.class);
            startActivity(i);
        });

        // Para cuando se presione el boton de Iniciar Sesion, nos llevara a la pantalla de Iniciar Sesion
        btnIniciarSesion.setOnClickListener(v -> {
            Intent i = new Intent(context, IniciarSesion_Activity.class);
            startActivity(i);
        });
    }
}