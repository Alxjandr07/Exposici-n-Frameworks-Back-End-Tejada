package com.example.demo.roles;
import com.example.demo.model.PreSustentacion;
public interface PuedeEvaluar {
    void emitirObservacion(PreSustentacion p, String texto);
}