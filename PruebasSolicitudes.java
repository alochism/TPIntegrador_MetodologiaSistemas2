public class PruebasSolicitudes {
    public static void main(String[] args) {
        InMemorySolicitudRepository repositorio = new InMemorySolicitudRepository();
        SolicitudService service = new SolicitudService(repositorio);

        // pruebo que registrar una solicitud la deja en estado PENDIENTE
        Solicitud s1 = service.registrarSolicitud(new DatosSolicitud("Equivalencia", Prioridad.ALTA, "2026-08-01", "Daniel Danielson", "12345"));
        assert s1.getEstado() == EstadoSolicitud.PENDIENTE : "una solicitud nueva debe arrancar PENDIENTE";

        // pruebo que cambiar estado se refleje correctamente
        service.cambiarEstado(s1.getId(), EstadoSolicitud.EN_PROCESO);
        assert s1.getEstado() == EstadoSolicitud.EN_PROCESO : "el estado no se actualizo correctamente";

        // pruebo que asignar responsable funcione
        service.asignarResponsable(s1.getId(), new Responsable("Marta Magnaterra", "Alumnado"));
        assert s1.getResponsable() != null : "el responsable no quedo asignado";
        assert s1.getResponsable().getNombre().equals("Marta Magnaterra") : "el responsable asignado no es el esperado";

        // pruebo que el filtro combine criterios correctamente (esto fallaba en la version original)
        service.registrarSolicitud(new DatosSolicitud("Certificado alumno regular", Prioridad.BAJA, "2026-08-02", "Juan Janigues", "67890"));
        var resultados = service.buscarSolicitudes(new FiltroBusqueda("Equivalencia", EstadoSolicitud.EN_PROCESO, null));
        assert resultados.size() == 1 : "el filtro combinado deberia devolver exactamente una solicitud";

        // pruebo que cambiar estado sobre un id inexistente lance excepcion (validacion defensiva)
        boolean lanzoExcepcion = false;
        try {
            service.cambiarEstado(999, EstadoSolicitud.RESUELTA);
        } catch (IllegalArgumentException e) {
            lanzoExcepcion = true;
        }
        assert lanzoExcepcion : "cambiar estado de un id inexistente deberia lanzar excepcion";

        System.out.println("Todas las pruebas pasaron correctamente");
    }
}