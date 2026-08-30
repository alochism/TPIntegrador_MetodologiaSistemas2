public class Main {
    public static void main(String[] args) {
        InMemorySolicitudRepository repositorio = new InMemorySolicitudRepository();
        SolicitudService service = new SolicitudService(repositorio);
        ReporteService reporteService = new ReporteService();
        ReportePresenter presenter = new ReportePresenter();

        Solicitud s1 = service.registrarSolicitud(new DatosSolicitud("Cambio de comisión", Prioridad.ALTA, "2026-08-01", "Daniel Danielson", "12345"));
        service.registrarSolicitud(new DatosSolicitud("Certificado alumno regular", Prioridad.BAJA, "2026-08-02", "Juan Janigues", "67890"));
        service.registrarSolicitud(new DatosSolicitud("Equivalencia", Prioridad.MEDIA, "2026-08-03", "Cecilia Cecinar", "11223"));

        service.asignarResponsable(s1.getId(), new Responsable("Marta Magnaterra", "Alumnado"));
        service.cambiarEstado(s1.getId(), EstadoSolicitud.EN_PROCESO);
        service.cambiarEstado(2, EstadoSolicitud.RESUELTA);

        service.buscarSolicitudes(new FiltroBusqueda("Equivalencia", null, null))
                .forEach(s -> System.out.println(s.getId() + " - " + s.getTipo() + " - " + s.getEstado()));

        presenter.imprimir(reporteService.generarReporte(repositorio.obtenerTodas()));
    }
}
