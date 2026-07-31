package com.example.demo.tribunal;
import com.example.demo.model.EstadoPreSustentacion;
import com.example.demo.model.PreSustentacion;
public abstract class MiembroTribunal {
    protected String nombre;
    public MiembroTribunal(String nombre) { this.nombre = nombre; }
    public abstract void emitirVoto(PreSustentacion p,
                                    EstadoPreSustentacion voto);
}