package com.example.demo.notificacion;

import com.example.demo.model.EstadoPreSustentacion;
import com.example.demo.model.PreSustentacion;
import org.springframework.stereotype.Component;

@Component
public class RechazoNotificador implements ResultadoNotificador {
    public boolean aplicaA(EstadoPreSustentacion estado) {
        return estado == EstadoPreSustentacion.RECHAZADA;
    }
    public void notificar(PreSustentacion p) {
        System.out.println("[Correo] " + p.getEstudiante() + ": tu pre-sustentacion fue RECHAZADA.");
    }
}