package com.example.proyectoveterinario_adrianisado_danielquinones.objetos;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Usuario implements Serializable {
    private final int id;
    private final String nombre;
    private final String apellidos;
    private final String correoElectronico;
    private String contrasenia;
    private final int numTelefono;
    private int idCita;

    public Usuario(int id, String nombre, String apellidos, String correoElectronico, String contrasenia, int numTelefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
        this.contrasenia = contrasenia;
        this.numTelefono = numTelefono;
    }

    public int getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }


    public String getApellidos() {
        return apellidos;
    }


    public String getCorreoElectronico() {
        return correoElectronico;
    }


    public String getContrasenia() {
        return contrasenia;
    }
    public void setContrasenia(String contrasenia){
        this.contrasenia = contrasenia;
    }


    public int getNumTelefono() {
        return numTelefono;
    }


    public int getIdCita() {
        return idCita;
    }


    @NonNull
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", numTelefono=" + numTelefono +
                ", idCita=" + idCita +
                '}';
    }
}
