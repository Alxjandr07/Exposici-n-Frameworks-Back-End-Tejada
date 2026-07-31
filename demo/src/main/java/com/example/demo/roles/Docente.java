package com.example.demo.roles;
import com.example.demo.model.PreSustentacion;
public class Docente implements PuedeEvaluar {
    public void emitirObservacion(PreSustentacion p, String texto) {
        System.out.println("[Docente] observacion para " +
                p.getEstudiante() + ": " + texto);
    }
}