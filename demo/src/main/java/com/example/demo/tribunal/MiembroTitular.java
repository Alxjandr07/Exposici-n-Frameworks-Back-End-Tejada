package com.example.demo.tribunal;
import com.example.demo.model.EstadoPreSustentacion;
import com.example.demo.model.PreSustentacion;
public class MiembroTitular extends MiembroTribunal {
    public MiembroTitular(String nombre) { super(nombre); }
    public void emitirVoto(PreSustentacion p, EstadoPreSustentacion
            voto) {
        System.out.println("[Tribunal-Titular] " + nombre + " vota: " + voto);
                p.setEstado(voto);
    }
}