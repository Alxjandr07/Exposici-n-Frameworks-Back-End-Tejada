package com.example.demo.tribunal;
import com.example.demo.model.EstadoPreSustentacion;
import com.example.demo.model.PreSustentacion;
public class MiembroSuplenteActivo extends MiembroTribunal {
    public MiembroSuplenteActivo(String nombre) {
        super(nombre);
    }
    public void emitirVoto(PreSustentacion p, EstadoPreSustentacion
            voto) {
        System.out.println("[Tribunal-Suplente] " + nombre + " vota: " + voto);
                p.setEstado(voto);
    }
}