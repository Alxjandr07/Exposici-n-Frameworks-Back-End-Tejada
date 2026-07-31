package com.example.demo.notificacion;
import com.example.demo.model.EstadoPreSustentacion;
import com.example.demo.model.PreSustentacion;
import org.springframework.stereotype.Component;
@Component
public class ObservacionNotificador implements ResultadoNotificador
{
    public boolean aplicaA(EstadoPreSustentacion estado) {
        return estado == EstadoPreSustentacion.OBSERVADA;
    }
    public void notificar(PreSustentacion p) {
        System.out.println("[Correo] " + p.getEstudiante() + ": tu pre-sustentacion tiene OBSERVACIONES.");
    }
}