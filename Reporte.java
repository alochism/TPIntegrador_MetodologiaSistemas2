import java.util.List;

// separo el cálculo de totales de la impresión (resuelve el acoplamiento del smell #8)
public class Reporte {
    private final List<Solicitud> solicitudes;
    private final long pendientes;
    private final long enProceso;
    private final long resueltas;

    public Reporte(List<Solicitud> solicitudes, long pendientes, long enProceso, long resueltas) {
        this.solicitudes = solicitudes;
        this.pendientes = pendientes;
        this.enProceso = enProceso;
        this.resueltas = resueltas;
    }

    public List<Solicitud> getSolicitudes() { return solicitudes; }
    public long getPendientes() { return pendientes; }
    public long getEnProceso() { return enProceso; }
    public long getResueltas() { return resueltas; }
}