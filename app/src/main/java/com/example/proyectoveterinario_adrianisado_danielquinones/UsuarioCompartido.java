package com.example.proyectoveterinario_adrianisado_danielquinones;

import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Usuario;

public class UsuarioCompartido {

    public static Usuario usuario = null;

    public static Usuario getUsuario() {
        return usuario;
    }
    public static void setUsuario(Usuario usuario) {
        UsuarioCompartido.usuario = usuario;
    }
}
