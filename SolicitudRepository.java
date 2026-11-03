import java.util.List;
import java.util.Optional;

// Abstracción de persistencia: el service depende de esta interfaz (DIP), no de la implementación.
public interface SolicitudRepository {
    int generarNuevoId();
    Solicitud guardar(Solicitud solicitud);
    Optional<Solicitud> buscarPorId(int id);
    List<Solicitud> obtenerTodas();
}
