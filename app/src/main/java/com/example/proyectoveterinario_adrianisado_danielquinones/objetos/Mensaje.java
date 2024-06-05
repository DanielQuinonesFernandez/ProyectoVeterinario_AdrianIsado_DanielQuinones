package com.example.proyectoveterinario_adrianisado_danielquinones.objetos;

import java.util.Date;

public class Mensaje {
    private int id;
    private int idUsuario;
    private String asunto;
    private String contenido;
    private Date fecha;
    private String tipoMensaje;
    private boolean leido;

    public Mensaje(int id, int idUsuario, String asunto, String contenido, Date fecha, String tipoMensaje, boolean leido) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.asunto = asunto;
        this.contenido = contenido;
        this.fecha = fecha;
        this.tipoMensaje = tipoMensaje;
        this.leido = leido;
    }

    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public boolean isLeido() {
        return leido;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", asunto='" + asunto + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fecha=" + fecha +
                ", tipoMensaje='" + tipoMensaje + '\'' +
                ", leido=" + leido +
                '}';
    }
}
