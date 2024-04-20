package com.example.proyectoveterinario_adrianisado_danielquinones.objetos;

import androidx.annotation.NonNull;

import java.util.Date;

public class HistorialMedico {
    private final int id;
    private final Date fechaCita;
    private final String descripcionCita;
    private final double precioCita;
    private final String tituloCita;
    private final int idMascota;

    public HistorialMedico(int id, Date fechaCita, String descripcionCita, double precioCita, String tituloCita, int idMascota) {
        this.id = id;
        this.fechaCita = fechaCita;
        this.descripcionCita = descripcionCita;
        this.precioCita = precioCita;
        this.tituloCita = tituloCita;
        this.idMascota = idMascota;
    }

    public int getId() {
        return id;
    }

    public Date getFechaCita() {
        return fechaCita;
    }


    public String getDescripcionCita() {
        return descripcionCita;
    }


    public double getPrecioCita() {
        return precioCita;
    }


    public String getTituloCita() {
        return tituloCita;
    }


    public int getIdMascota() {
        return idMascota;
    }


    @NonNull
    @Override
    public String toString() {
        return "HistorialMedico{" +
                "id=" + id +
                ", fechaCita=" + fechaCita +
                ", descripcionCita='" + descripcionCita + '\'' +
                ", precioCita=" + precioCita +
                ", tituloCita='" + tituloCita + '\'' +
                ", idMascota=" + idMascota +
                '}';
    }
}
