package com.example.proyectoveterinario_adrianisado_danielquinones.objetos;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.util.Date;

public class Mascota {
    private final int id;
    private final String nombre;
    private final String especie;
    private final Date fechaNacimiento;
    private final Bitmap fotoMascota;
    private final String idUsuario;

    public Mascota(int id, String nombre, String especie, Date fechaNacimiento, Bitmap fotoMascota, String idUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoMascota = fotoMascota;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }


    public String getEspecie() {
        return especie;
    }


    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }


    public Bitmap getFotoMascota() {
        return fotoMascota;
    }


    public String getIdUsuario() {
        return idUsuario;
    }


    @NonNull
    @Override
    public String toString() {
        return nombre + " [" + especie + "]";
    }
}
