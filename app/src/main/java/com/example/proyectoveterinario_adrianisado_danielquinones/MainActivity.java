package com.example.proyectoveterinario_adrianisado_danielquinones;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyectoveterinario_adrianisado_danielquinones.actividades.ayudas.AyudaMensajes;
import com.example.proyectoveterinario_adrianisado_danielquinones.actividades.ayudas.AyudaMiPerfil;
import com.example.proyectoveterinario_adrianisado_danielquinones.actividades.ayudas.AyudaPedirCita;
import com.example.proyectoveterinario_adrianisado_danielquinones.actividades.ayudas.AyudaRegistrarMascota;
import com.example.proyectoveterinario_adrianisado_danielquinones.databinding.ActivityMainBinding;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Usuario;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Usuario usuarioIniciado = (Usuario) getIntent().getSerializableExtra("usuarioIniciado");
        UsuarioCompartido.setUsuario(usuarioIniciado);

        com.example.proyectoveterinario_adrianisado_danielquinones.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getDrawable(R.drawable.fondoazulgris2);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(drawable);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_miPerfil, R.id.nav_pedirCita, R.id.nav_mensajes, R.id.nav_registrarMascota, R.id.nav_historialMascotas, R.id.nav_FAQ)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Activar el icono de la barra de menu
        if (getClass() == MainActivity.class) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_bar, menu);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Funcionalidad del menú de la barra
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int fragmentActualId = Objects.requireNonNull(Navigation.findNavController(this, R.id.nav_host_fragment_content_main).getCurrentDestination()).getId();

        if (item.getItemId() == R.id.menu_bar_mensajes) {
            Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_mensajes);
            return true;
        } else if (item.getItemId() == R.id.menu_bar_perfil) {
            Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_miPerfil);
            return true;
        } else if (item.getItemId() == R.id.menu_bar_ayuda){
            abrirAyudaEnFuncionDelFragmentoAbierto(fragmentActualId);
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirAyudaEnFuncionDelFragmentoAbierto(int fragmentActualId){
        if(fragmentActualId == R.id.nav_miPerfil){
            Intent i = new Intent(this, AyudaMiPerfil.class);
            startActivity(i);
        }
        else if(fragmentActualId == R.id.nav_pedirCita){
            Intent i = new Intent(this, AyudaPedirCita.class);
            startActivity(i);
        }
        else if(fragmentActualId == R.id.nav_mensajes){
            Intent i = new Intent(this, AyudaMensajes.class);
            startActivity(i);
        }
        else if(fragmentActualId == R.id.nav_registrarMascota){
            Intent i = new Intent(this, AyudaRegistrarMascota.class);
            startActivity(i);
        }
        else if(fragmentActualId == R.id.nav_historialMascotas){

        }
        else if(fragmentActualId == R.id.nav_FAQ){

        }
    }
}