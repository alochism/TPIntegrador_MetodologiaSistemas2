import java.util.List;

public class ReporteService {
    public Reporte generarReporte(List<Solicitud> solicitudes) {
        long pendientes = contar(solicitudes, EstadoSolicitud.PENDIENTE);
        long enProceso = contar(solicitudes, EstadoSolicitud.EN_PROCESO);
        long resueltas = contar(solicitudes, EstadoSolicitud.RESUELTA);
        return new Reporte(solicitudes, pendientes, enProceso, resueltas);
    }

    private long contar(List<Solicitud> solicitudes, EstadoSolicitud estado) {
        return solicitudes.stream().filter(s -> s.getEstado() == estado).count();
    }
}