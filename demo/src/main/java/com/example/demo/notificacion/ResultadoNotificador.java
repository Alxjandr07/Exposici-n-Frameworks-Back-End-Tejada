package com.example.demo.notificacion;
import com.example.demo.model.EstadoPreSustentacion;
import com.example.demo.model.PreSustentacion;

public interface  ResultadoNotificador {
    boolean aplicaA(EstadoPreSustentacion estado);
    void notificar(PreSustentacion p);
}