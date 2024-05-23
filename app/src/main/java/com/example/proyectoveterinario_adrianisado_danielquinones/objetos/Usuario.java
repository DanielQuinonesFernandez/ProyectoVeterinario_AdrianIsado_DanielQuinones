package com.example.proyectoveterinario_adrianisado_danielquinones.objetos;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String apellidos;
    private String correoElectronico;
    private String contrasenia;
    private int numTelefono;
    private boolean esAdmin;
    private boolean estaVetado;
    private String razonVeto;

    public Usuario(int id, String nombre, String apellidos, String correoElectronico, String contrasenia, int numTelefono, boolean esAdmin, boolean estaVetado, String razonVeto) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
        this.contrasenia = contrasenia;
        this.numTelefono = numTelefono;
        this.esAdmin = esAdmin;
        this.estaVetado = estaVetado;
        this.razonVeto = razonVeto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int getNumTelefono() {
        return numTelefono;
    }

    public void setNumTelefono(int numTelefono) {
        this.numTelefono = numTelefono;
    }

    public boolean isAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public boolean isVetado() {
        return estaVetado;
    }

    public void setEstaVetado(boolean estaVetado) {
        this.estaVetado = estaVetado;
    }

    public String getRazonVeto() {
        return razonVeto;
    }

    public void setRazonVeto(String razonVeto) {
        this.razonVeto = razonVeto;
    }
}
