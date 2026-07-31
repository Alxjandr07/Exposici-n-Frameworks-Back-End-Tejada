package com.example.demo.validation;
import com.example.demo.model.PreSustentacion;
import org.springframework.stereotype.Component;
@Component
public class PreSustentacionValidator {
    public void validar(PreSustentacion p) {
        if (p.getPromedio() < 7.0) {
            throw new IllegalArgumentException(
                    "El estudiante " + p.getEstudiante() + " no cumple el promedio minimo (7.0)");
        }
    }}