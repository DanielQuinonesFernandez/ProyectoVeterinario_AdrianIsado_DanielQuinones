package com.example.proyectoveterinario_adrianisado_danielquinones;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores.AdaptadorMensajes;
import com.example.proyectoveterinario_adrianisado_danielquinones.databinding.ActivityMainBinding;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mensaje;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Usuario;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = getDrawable(R.drawable.desktop_wallpaper_blue_linear_gradient_pink_aqua_cyan_hot_pink_00ffff_ff69b4_15a_);
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

        if (item.getItemId() == R.id.menu_bar_mensajes) {
            Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_mensajes);
            return true;
        } else if (item.getItemId() == R.id.menu_bar_perfil) {
            Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_miPerfil);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}