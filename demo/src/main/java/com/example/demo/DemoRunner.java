package com.example.demo;

import com.example.demo.acta.GeneradorActa;
import com.example.demo.acta.PdfActaGenerator;
import com.example.demo.model.EstadoPreSustentacion;
import com.example.demo.model.PreSustentacion;
import com.example.demo.notificacion.*;
import com.example.demo.roles.Docente;
import com.example.demo.roles.Estudiante;
import com.example.demo.tribunal.MiembroTitular;
import com.example.demo.tribunal.MiembroTribunal;
import com.example.demo.validation.PreSustentacionValidator;

import java.util.ArrayList;
import java.util.List;

public class DemoRunner {

    // Version original: devuelve el paso a paso como texto (util para consola o para
    // una vista tipo "log tecnico" si la necesitas mas adelante).
    public static List<String> ejecutar() {
        List<String> pasos = new ArrayList<>();

        Estudiante estudiante = new Estudiante();
        Docente docente = new Docente();
        estudiante.subirDocumento("tesis_final.pdf");
        pasos.add("I | Estudiante sube su documento: tesis_final.pdf");

        PreSustentacion p = new PreSustentacion("Ana Torres", 8.5);

        new PreSustentacionValidator().validar(p);
        pasos.add("S | Validacion superada: promedio de " + p.getEstudiante() + " es " + p.getPromedio());

        MiembroTribunal tribunal = new MiembroTitular("Dr. Perez");
        docente.emitirObservacion(p, "Revisar el capitulo 3");
        pasos.add("I | Docente emite observacion: \"Revisar el capitulo 3\"");
        tribunal.emitirVoto(p, EstadoPreSustentacion.OBSERVADA);
        pasos.add("L | El tribunal (titular) vota: " + p.getEstado());

        GeneradorActa generador = new PdfActaGenerator();
        String acta = generador.generar(p);
        pasos.add("D | Acta generada mediante la interfaz GeneradorActa: " + acta);

        List<ResultadoNotificador> notificadores = List.of(
                new AprobacionNotificador(), new ObservacionNotificador(), new RechazoNotificador()
        );
        for (ResultadoNotificador n : notificadores) {
            if (n.aplicaA(p.getEstado())) {
                n.notificar(p);
                pasos.add("O | Notificador seleccionado automaticamente: " + n.getClass().getSimpleName());
            }
        }
        return pasos;
    }

    // Version nueva: corre el mismo flujo, pero devuelve el objeto PreSustentacion
    // ya resuelto, para que DemoController arme una interfaz real con sus datos.
    public static PreSustentacion ejecutarDetallado() {
        Estudiante estudiante = new Estudiante();
        Docente docente = new Docente();
        estudiante.subirDocumento("tesis_final.pdf");

        PreSustentacion p = new PreSustentacion("Ana Torres", 8.5);

        new PreSustentacionValidator().validar(p);

        MiembroTribunal tribunal = new MiembroTitular("Dr. Perez");
        docente.emitirObservacion(p, "Revisar el capitulo 3");
        tribunal.emitirVoto(p, EstadoPreSustentacion.OBSERVADA);

        GeneradorActa generador = new PdfActaGenerator();
        generador.generar(p);

        List<ResultadoNotificador> notificadores = List.of(
                new AprobacionNotificador(), new ObservacionNotificador(), new RechazoNotificador()
        );
        for (ResultadoNotificador n : notificadores) {
            if (n.aplicaA(p.getEstado())) {
                n.notificar(p);
            }
        }

        return p;
    }
}