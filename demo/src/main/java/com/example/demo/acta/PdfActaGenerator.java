package com.example.demo.acta;
import com.example.demo.model.PreSustentacion;
import org.springframework.stereotype.Component;
@Component
public class PdfActaGenerator implements GeneradorActa {
    public String generar(PreSustentacion p) {
        return "acta_" + p.getEstudiante().replace(" ", "_") + ".pdf";
    }
}