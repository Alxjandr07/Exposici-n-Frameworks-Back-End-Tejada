package com.example.demo;

import com.example.demo.model.EstadoPreSustentacion;
import com.example.demo.model.PreSustentacion;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        // DemoRunner.ejecutarDetallado() corre el flujo completo (validar, votar,
        // generar acta, notificar) y devuelve el objeto final ya resuelto.
        PreSustentacion p = DemoRunner.ejecutarDetallado();
        return construirHtml(p);
    }

    private String colorEstado(EstadoPreSustentacion estado) {
        return switch (estado) {
            case APROBADA -> "#22c55e";
            case OBSERVADA -> "#f59e0b";
            case RECHAZADA -> "#ef4444";
            default -> "#6b7280";
        };
    }

    private String construirHtml(PreSustentacion p) {
        String colorBadge = colorEstado(p.getEstado());
        String nombreActa = "acta_" + p.getEstudiante().replace(" ", "_") + ".pdf";

        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <title>Sistema de Pre-Sustentaciones - UTEQ</title>
                <style>
                    * { box-sizing: border-box; font-family: 'Segoe UI', Arial, sans-serif; }
                    body { margin: 0; background: #f4f6f9; color: #1f2937; }

                    nav {
                        background: #14532d;
                        color: white;
                        padding: 16px 32px;
                        display: flex;
                        align-items: center;
                        justify-content: space-between;
                    }
                    nav .marca { font-weight: 600; font-size: 1rem; }
                    nav .marca span { opacity: 0.75; font-weight: 400; }
                    nav .usuario {
                        display: flex; align-items: center; gap: 10px; font-size: 0.85rem;
                    }
                    nav .avatar {
                        width: 30px; height: 30px; border-radius: 50%%;
                        background: #22c55e; display: flex; align-items: center;
                        justify-content: center; font-weight: 600; color: #14532d;
                    }

                    main { max-width: 760px; margin: 32px auto; padding: 0 20px; }

                    .titulo-pagina { margin-bottom: 20px; }
                    .titulo-pagina h1 { font-size: 1.3rem; margin: 0 0 4px 0; }
                    .titulo-pagina p { margin: 0; color: #6b7280; font-size: 0.9rem; }

                    .panel {
                        background: white;
                        border-radius: 12px;
                        padding: 24px;
                        margin-bottom: 20px;
                        box-shadow: 0 1px 3px rgba(0,0,0,0.08);
                        border: 1px solid #e5e7eb;
                    }
                    .panel h2 {
                        font-size: 0.95rem;
                        margin: 0 0 16px 0;
                        color: #374151;
                        border-bottom: 1px solid #e5e7eb;
                        padding-bottom: 10px;
                    }

                    .cabecera-estudiante {
                        display: flex; align-items: center; justify-content: space-between; gap: 16px;
                    }
                    .info-estudiante { display: flex; align-items: center; gap: 14px; }
                    .foto {
                        width: 48px; height: 48px; border-radius: 50%%;
                        background: #e5e7eb; display: flex; align-items: center;
                        justify-content: center; font-weight: 600; color: #6b7280;
                    }
                    .info-estudiante h3 { margin: 0; font-size: 1rem; }
                    .info-estudiante p { margin: 2px 0 0 0; font-size: 0.82rem; color: #6b7280; }

                    .estado-badge {
                        padding: 6px 14px; border-radius: 20px; color: white;
                        font-size: 0.8rem; font-weight: 600; background: %s;
                        white-space: nowrap;
                    }

                    .fila { display: flex; justify-content: space-between; padding: 10px 0; font-size: 0.9rem; border-bottom: 1px solid #f1f2f4; }
                    .fila:last-child { border-bottom: none; }
                    .fila .etiqueta { color: #6b7280; }
                    .fila .valor { font-weight: 500; }

                    .observacion {
                        background: #fff7ed; border: 1px solid #fed7aa;
                        border-radius: 8px; padding: 12px 14px; font-size: 0.87rem;
                        color: #9a3412; margin-top: 4px;
                    }

                    .documento-link {
                        display: inline-flex; align-items: center; gap: 8px;
                        background: #eff6ff; color: #1d4ed8; border: 1px solid #bfdbfe;
                        padding: 8px 14px; border-radius: 8px; font-size: 0.85rem;
                        text-decoration: none; font-weight: 500;
                    }

                    .notificacion {
                        display: flex; gap: 12px; align-items: flex-start;
                        background: #f0fdf4; border: 1px solid #bbf7d0;
                        border-radius: 8px; padding: 12px 14px; font-size: 0.87rem; color: #166534;
                    }
                    .notificacion .icono { font-size: 1.1rem; }

                    footer { text-align: center; color: #9ca3af; font-size: 0.78rem; padding: 24px 0; }
                </style>
            </head>
            <body>
                <nav>
                    <div class="marca">UTEQ <span>· Sistema de Gestion de Pre-Sustentaciones</span></div>
                    <div class="usuario">
                        <div class="avatar">DP</div>
                        Dr. Perez (Tribunal)
                    </div>
                </nav>

                <main>
                    <div class="titulo-pagina">
                        <h1>Detalle de Pre-Sustentacion</h1>
                        <p>Facultad de Ciencias de la Ingenieria &mdash; Periodo 2026</p>
                    </div>

                    <div class="panel">
                        <div class="cabecera-estudiante">
                            <div class="info-estudiante">
                                <div class="foto">%s</div>
                                <div>
                                    <h3>%s</h3>
                                    <p>Promedio de aprobacion: %.1f / 10</p>
                                </div>
                            </div>
                            <span class="estado-badge">%s</span>
                        </div>
                    </div>

                    <div class="panel">
                        <h2>Documento entregado</h2>
                        <a class="documento-link" href="#">&#128196; tesis_final.pdf</a>
                    </div>

                    <div class="panel">
                        <h2>Evaluacion del tribunal</h2>
                        <div class="fila">
                            <span class="etiqueta">Miembro</span>
                            <span class="valor">Dr. Perez (Titular)</span>
                        </div>
                        <div class="fila">
                            <span class="etiqueta">Voto</span>
                            <span class="valor">%s</span>
                        </div>
                        <div class="observacion">
                            &#128172; "Revisar el capitulo 3 antes de la sustentacion final."
                        </div>
                    </div>

                    <div class="panel">
                        <h2>Acta generada</h2>
                        <a class="documento-link" href="#">&#128190; %s</a>
                    </div>

                    <div class="panel">
                        <h2>Notificacion enviada</h2>
                        <div class="notificacion">
                            <span class="icono">&#9993;</span>
                            <span>Se notifico por correo a <b>%s</b>: su pre-sustentacion tiene observaciones pendientes por resolver.</span>
                        </div>
                    </div>
                </main>

                <footer>Sistema de Gestion de Pre-Sustentaciones &mdash; Universidad Tecnica Estatal de Quevedo</footer>
            </body>
            </html>
            """.formatted(
                colorBadge,
                p.getEstudiante().substring(0, 1),
                p.getEstudiante(),
                p.getPromedio(),
                p.getEstado(),
                p.getEstado(),
                nombreActa,
                p.getEstudiante()
        );
    }
}