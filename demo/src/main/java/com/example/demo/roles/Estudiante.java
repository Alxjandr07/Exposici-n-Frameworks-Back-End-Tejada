package com.example.demo.roles;
public class Estudiante implements PuedeSubirDocumento {
    public void subirDocumento(String archivo) {
        System.out.println("[Estudiante] documento subido: " +
                archivo);
    }
}