import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class SolicitudService {
    private final SolicitudRepository repositorio; // DIP: depende de la interfaz

    public SolicitudService(SolicitudRepository repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio es obligatorio");
        }
        this.repositorio = repositorio;
    }

    public Solicitud registrarSolicitud(DatosSolicitud datos) {
        validarDatos(datos);
        Estudiante estudiante = new Estudiante(datos.getNombreEstudiante(), datos.getLegajoEstudiante());
        int id = repositorio.generarNuevoId();
        Solicitud solicitud = new Solicitud(id, datos.getTipo(), datos.getPrioridad(), datos.getFecha(), estudiante);
        return repositorio.guardar(solicitud);
    }

    public void cambiarEstado(int id, EstadoSolicitud nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio");
        }
        obtenerOFallar(id).setEstado(nuevoEstado);
    }

    public void asignarResponsable(int id, Responsable responsable) {
        if (responsable == null) {
            throw new IllegalArgumentException("El responsable es obligatorio");
        }
        obtenerOFallar(id).setResponsable(responsable);
    }

    public List<Solicitud> buscarSolicitudes(FiltroBusqueda filtro) {
        if (filtro == null) {
            throw new IllegalArgumentException("El filtro es obligatorio; use criterios null para listar todas");
        }
        return repositorio.obtenerTodas().stream()
                .filter(s -> cumpleFiltro(s, filtro)) // Extract Method
                .collect(Collectors.toList());
    }

    public List<Solicitud> obtenerTodas() {
        return repositorio.obtenerTodas();
    }

    private boolean cumpleFiltro(Solicitud solicitud, FiltroBusqueda filtro) {
        // Criterios combinados con AND; tipo vacío o criterio null significa "no filtrar por él".
        if (!estaVacio(filtro.getTipo()) && !filtro.getTipo().equals(solicitud.getTipo())) return false;
        if (filtro.getEstado() != null && solicitud.getEstado() != filtro.getEstado()) return false;
        if (filtro.getPrioridad() != null && solicitud.getPrioridad() != filtro.getPrioridad()) return false;
        return true;
    }

    private void validarDatos(DatosSolicitud datos) {
        if (datos == null) throw new IllegalArgumentException("Los datos de la solicitud son obligatorios");
        if (estaVacio(datos.getTipo())) throw new IllegalArgumentException("El tipo es obligatorio");
        if (datos.getPrioridad() == null) throw new IllegalArgumentException("La prioridad es obligatoria");
        validarFecha(datos.getFecha());
        if (estaVacio(datos.getNombreEstudiante())) throw new IllegalArgumentException("El nombre del estudiante es obligatorio");
        if (estaVacio(datos.getLegajoEstudiante())) throw new IllegalArgumentException("El legajo del estudiante es obligatorio");
    }

    private void validarFecha(String fecha) {
        if (estaVacio(fecha)) throw new IllegalArgumentException("La fecha es obligatoria");
        try {
            LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha debe ser válida y estar en formato ISO-8601 (ejemplo: 2026-08-01)", e);
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }

    private Solicitud obtenerOFallar(int id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe una solicitud con id " + id));
    }
}
