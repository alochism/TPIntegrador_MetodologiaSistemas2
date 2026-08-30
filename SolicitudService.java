import java.util.List;
import java.util.stream.Collectors;

public class SolicitudService {
    private final InMemorySolicitudRepository repositorio;

    public SolicitudService(InMemorySolicitudRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Solicitud registrarSolicitud(DatosSolicitud datos) {
        Estudiante estudiante = new Estudiante(datos.getNombreEstudiante(), datos.getLegajoEstudiante());
        int id = repositorio.generarNuevoId();
        Solicitud solicitud = new Solicitud(id, datos.getTipo(), datos.getPrioridad(), datos.getFecha(), estudiante);
        return repositorio.guardar(solicitud);
    }

    public void cambiarEstado(int id, EstadoSolicitud nuevoEstado) {
        Solicitud solicitud = obtenerOFallar(id); // valido que exista antes de operar (resuelve smell de validación defensiva)
        solicitud.setEstado(nuevoEstado);
    }

    public void asignarResponsable(int id, Responsable responsable) {
        Solicitud solicitud = obtenerOFallar(id);
        solicitud.setResponsable(responsable);
    }

    public List<Solicitud> buscarSolicitudes(FiltroBusqueda filtro) {
        return repositorio.obtenerTodas().stream()
                .filter(s -> cumpleFiltro(s, filtro)) // Extract Method: saco la condición a su propio método
                .collect(Collectors.toList());
    }

    private boolean cumpleFiltro(Solicitud solicitud, FiltroBusqueda filtro) {
        // combino los criterios con AND (antes se ignoraban entre sí con el else if)
        if (filtro.getTipo() != null && !solicitud.getTipo().equals(filtro.getTipo())) return false;
        if (filtro.getEstado() != null && solicitud.getEstado() != filtro.getEstado()) return false;
        if (filtro.getPrioridad() != null && solicitud.getPrioridad() != filtro.getPrioridad()) return false;
        return true;
    }

    private Solicitud obtenerOFallar(int id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe una solicitud con id " + id));
    }
}