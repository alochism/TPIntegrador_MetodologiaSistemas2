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

        probarValidaciones();
        probarFiltros();
        probarReporte();

        System.out.println("Todas las pruebas pasaron correctamente (" + ejecutadas + " verificaciones)");
    }

    private static void probarValidaciones() {
        SolicitudService service = new SolicitudService(new InMemorySolicitudRepository());
        verificarExcepcion(() -> new SolicitudService(null), "repositorio null");

        for (String fecha : new String[] {null, "", "  ", "01/08/2026", "2026-02-29", "2026-02-30", "2026-13-01"}) {
            verificarExcepcion(() -> service.registrarSolicitud(
                    new DatosSolicitud("Equivalencia", Prioridad.ALTA, fecha, "Ana", "1")), "fecha inválida: " + fecha);
        }
        for (String texto : new String[] {null, "", "  "}) {
            verificarExcepcion(() -> service.registrarSolicitud(datos("T", Prioridad.ALTA, texto, "1")), "nombre inválido");
            verificarExcepcion(() -> service.registrarSolicitud(datos("T", Prioridad.ALTA, "Ana", texto)), "legajo inválido");
        }
        verificar(service.obtenerTodas().isEmpty(), "los registros rechazados no deben persistirse");

        Solicitud solicitud = service.registrarSolicitud(
                new DatosSolicitud("Equivalencia", Prioridad.ALTA, "2024-02-29", "Ana", "1"));
        verificar(solicitud.getId() == 1, "los registros rechazados no deben consumir ids");
        verificar(solicitud.getFecha().equals("2024-02-29"), "una fecha bisiesta válida debe conservarse");

        Responsable responsable = new Responsable("Marta", "Alumnado");
        service.asignarResponsable(solicitud.getId(), responsable);
        verificarExcepcion(() -> service.cambiarEstado(solicitud.getId(), null), "estado null");
        verificarExcepcion(() -> service.asignarResponsable(solicitud.getId(), null), "responsable null");
        verificar(solicitud.getEstado() == EstadoSolicitud.PENDIENTE, "un estado inválido no debe alterar la solicitud");
        verificar(solicitud.getResponsable() == responsable, "un responsable inválido no debe reemplazar al asignado");
    }

    private static void probarFiltros() {
        SolicitudService service = new SolicitudService(new InMemorySolicitudRepository());
        verificarExcepcion(() -> service.buscarSolicitudes(null), "filtro null con repositorio vacío");
        verificar(service.buscarSolicitudes(new FiltroBusqueda(null, null, null)).isEmpty(), "búsqueda sin datos");

        Solicitud primera = service.registrarSolicitud(datos("Equivalencia", Prioridad.ALTA, "Ana", "1"));
        service.registrarSolicitud(datos("Equivalencia", Prioridad.BAJA, "Bruno", "2"));
        service.registrarSolicitud(datos("Certificado", Prioridad.ALTA, "Carla", "3"));
        service.cambiarEstado(primera.getId(), EstadoSolicitud.EN_PROCESO);

        List<Solicitud> combinado = service.buscarSolicitudes(
                new FiltroBusqueda("Equivalencia", EstadoSolicitud.EN_PROCESO, Prioridad.ALTA));
        verificar(combinado.size() == 1 && combinado.get(0).getId() == primera.getId(), "los tres criterios deben combinarse con AND");
        verificar(service.buscarSolicitudes(new FiltroBusqueda("Equivalencia", EstadoSolicitud.EN_PROCESO, Prioridad.BAJA)).isEmpty(),
                "si un criterio no coincide, la solicitud debe quedar excluida");
        for (String tipo : new String[] {null, "", "  "}) {
            verificar(service.buscarSolicitudes(new FiltroBusqueda(tipo, null, null)).size() == 3, "tipo vacío no debe filtrar");
            verificar(service.buscarSolicitudes(new FiltroBusqueda(tipo, EstadoSolicitud.EN_PROCESO, Prioridad.ALTA)).size() == 1,
                    "omitir el tipo debe respetar los otros criterios");
        }
        verificar(service.buscarSolicitudes(new FiltroBusqueda("Inexistente", null, null)).isEmpty(), "tipo sin coincidencias");
        verificarExcepcion(() -> service.buscarSolicitudes(null), "filtro null con datos");

        List<Solicitud> copia = service.obtenerTodas();
        copia.clear();
        verificar(service.obtenerTodas().size() == 3, "modificar el listado devuelto no debe borrar el repositorio");
    }

    private static void probarReporte() {
        SolicitudService service = new SolicitudService(new InMemorySolicitudRepository());
        ReporteService reportes = new ReporteService();
        Reporte vacio = reportes.generarReporte(service.obtenerTodas());
        verificar(vacio.getSolicitudes().isEmpty() && vacio.getPendientes() == 0 && vacio.getEnProceso() == 0 && vacio.getResueltas() == 0,
                "el reporte vacío debe tener todos sus totales en cero");

        service.registrarSolicitud(datos("Equivalencia", Prioridad.ALTA, "Ana", "1"));
        Solicitud enProceso = service.registrarSolicitud(datos("Certificado", Prioridad.BAJA, "Bruno", "2"));
        Solicitud resuelta = service.registrarSolicitud(datos("Cambio de comisión", Prioridad.MEDIA, "Carla", "3"));
        service.cambiarEstado(enProceso.getId(), EstadoSolicitud.EN_PROCESO);
        service.cambiarEstado(resuelta.getId(), EstadoSolicitud.RESUELTA);

        Reporte reporte = reportes.generarReporte(service.obtenerTodas());
        verificar(reporte.getPendientes() == 1 && reporte.getEnProceso() == 1 && reporte.getResueltas() == 1,
                "el reporte debe contar los tres estados");
        verificar(reporte.getPendientes() + reporte.getEnProceso() + reporte.getResueltas() == reporte.getSolicitudes().size(),
                "los totales deben sumar la cantidad de solicitudes");
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
