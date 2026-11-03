import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemorySolicitudRepository implements SolicitudRepository {
    private final List<Solicitud> solicitudes = new ArrayList<>();
    private int contadorId = 1; // Move Method: la generación de id es responsabilidad del repositorio

    @Override
    public int generarNuevoId() {
        return contadorId++;
    }

    @Override
    public Solicitud guardar(Solicitud solicitud) {
        solicitudes.add(solicitud);
        return solicitud;
    }

    @Override
    public Optional<Solicitud> buscarPorId(int id) {
        return solicitudes.stream().filter(s -> s.getId() == id).findFirst();
    }

    @Override
    public List<Solicitud> obtenerTodas() {
        return new ArrayList<>(solicitudes);
    }
}
