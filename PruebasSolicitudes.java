import java.util.List;

// Pruebas sin depender de -ea: si una verificación falla, el programa termina con error.
public class PruebasSolicitudes {
    private static int ejecutadas = 0;

    public static void main(String[] args) {
        SolicitudRepository repositorio = new InMemorySolicitudRepository(); // tipado por interfaz
        SolicitudService service = new SolicitudService(repositorio);

        Solicitud s1 = service.registrarSolicitud(datos("Equivalencia", Prioridad.ALTA, "Daniel Danielson", "12345"));
        verificar(s1.getEstado() == EstadoSolicitud.PENDIENTE, "una solicitud nueva debe arrancar PENDIENTE");

        service.cambiarEstado(s1.getId(), EstadoSolicitud.EN_PROCESO);
        verificar(s1.getEstado() == EstadoSolicitud.EN_PROCESO, "el estado no se actualizó");

        service.asignarResponsable(s1.getId(), new Responsable("Marta Magnaterra", "Alumnado"));
        verificar(s1.getResponsable() != null && s1.getResponsable().getNombre().equals("Marta Magnaterra"),
                "el responsable no quedó asignado");

        service.registrarSolicitud(datos("Equivalencia", Prioridad.BAJA, "Juan Janigues", "67890"));
        service.registrarSolicitud(datos("Certificado alumno regular", Prioridad.ALTA, "Cecilia Cecinar", "11223"));

        List<Solicitud> combinado = service.buscarSolicitudes(new FiltroBusqueda("Equivalencia", EstadoSolicitud.EN_PROCESO, null));
        verificar(combinado.size() == 1, "filtro combinado tipo+estado debe devolver 1");

        List<Solicitud> porPrioridad = service.buscarSolicitudes(new FiltroBusqueda(null, null, Prioridad.ALTA));
        verificar(porPrioridad.size() == 2, "filtro por prioridad ALTA debe devolver 2");

        List<Solicitud> sinFiltro = service.buscarSolicitudes(new FiltroBusqueda(null, null, null));
        verificar(sinFiltro.size() == 3, "sin criterios debe devolver todas");

        Reporte reporte = new ReporteService().generarReporte(service.obtenerTodas());
        verificar(reporte.getPendientes() == 2 && reporte.getEnProceso() == 1 && reporte.getResueltas() == 0,
                "totales del reporte incorrectos");

        verificarExcepcion(() -> service.cambiarEstado(999, EstadoSolicitud.RESUELTA), "id inexistente en cambiarEstado");
        verificarExcepcion(() -> service.asignarResponsable(999, new Responsable("X", "Y")), "id inexistente en asignarResponsable");
        verificarExcepcion(() -> service.registrarSolicitud(datos(null, Prioridad.ALTA, "A", "1")), "tipo null");
        verificarExcepcion(() -> service.registrarSolicitud(datos("  ", Prioridad.ALTA, "A", "1")), "tipo vacío");
        verificarExcepcion(() -> service.registrarSolicitud(datos("T", null, "A", "1")), "prioridad null");
        verificarExcepcion(() -> service.registrarSolicitud(null), "datos null");
        verificar(service.obtenerTodas().size() == 3, "una solicitud inválida no debe quedar guardada");

        System.out.println("Todas las pruebas pasaron correctamente (" + ejecutadas + " verificaciones)");
    }

    private static DatosSolicitud datos(String tipo, Prioridad prioridad, String nombre, String legajo) {
        return new DatosSolicitud(tipo, prioridad, "2026-08-01", nombre, legajo);
    }

    private static void verificar(boolean condicion, String mensaje) {
        ejecutadas++;
        if (!condicion) throw new AssertionError("FALLÓ: " + mensaje);
    }

    private static void verificarExcepcion(Runnable accion, String caso) {
        try {
            accion.run();
        } catch (IllegalArgumentException esperada) {
            ejecutadas++;
            return;
        }
        throw new AssertionError("FALLÓ: se esperaba IllegalArgumentException en " + caso);
    }
}
