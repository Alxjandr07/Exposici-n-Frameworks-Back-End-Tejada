package com.example.demo.notificacion;
import com.example.demo.model.EstadoPreSustentacion;
import com.example.demo.model.PreSustentacion;
import org.springframework.stereotype.Component;
@Component
public class AprobacionNotificador implements ResultadoNotificador {
    public boolean aplicaA(EstadoPreSustentacion estado) {
        return estado == EstadoPreSustentacion.APROBADA;
    }
    public void notificar(PreSustentacion p) {
        System.out.println("[Correo] " + p.getEstudiante() + ": tu pre-sustentacion fue APROBADA.");
    }
}