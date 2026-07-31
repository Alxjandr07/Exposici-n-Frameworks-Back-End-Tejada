package com.example.demo.model;

public class PreSustentacion {
    private String estudiante;
    private double promedio;
    private EstadoPreSustentacion estado;

    public PreSustentacion(String estudiante, double promedio) {
        this.estudiante = estudiante;
        this.promedio = promedio;
        this.estado = EstadoPreSustentacion.PENDIENTE;
    }

    public String getEstudiante() { return estudiante; }
    public double getPromedio() { return promedio; }
    public EstadoPreSustentacion getEstado() { return estado; }
    public void setEstado(EstadoPreSustentacion estado) { this.estado = estado; }

}
