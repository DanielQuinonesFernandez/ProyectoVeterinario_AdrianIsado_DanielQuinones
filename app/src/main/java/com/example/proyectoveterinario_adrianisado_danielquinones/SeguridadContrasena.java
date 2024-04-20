package com.example.proyectoveterinario_adrianisado_danielquinones;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SeguridadContrasena {
    public static String hashearContrasena(String contrasena) {
        try {
            // Crear un objeto MessageDigest con el algoritmo SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Calcular el hash de la contraseña
            byte[] hashedBytes = md.digest(contrasena.getBytes());

            // Convertir el arreglo de bytes a una representación hexadecimal
            BigInteger bigInt = new BigInteger(1, hashedBytes);
            StringBuilder hashedPassword = new StringBuilder(bigInt.toString(16));

            // Asegurarse de que el hash tenga 64 caracteres
            while (hashedPassword.length() < 64) {
                hashedPassword.insert(0, "0");
            }

            return hashedPassword.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error: Algoritmo SHA-256 no disponible");
            return null;
        }
    }
}
