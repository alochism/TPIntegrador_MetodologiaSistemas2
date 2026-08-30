// cualquier campo en null/vacío significa "no filtrar por este criterio"
public class FiltroBusqueda {
    private final String tipo;
    private final EstadoSolicitud estado;
    private final Prioridad prioridad;

    public FiltroBusqueda(String tipo, EstadoSolicitud estado, Prioridad prioridad) {
        this.tipo = tipo;
        this.estado = estado;
        this.prioridad = prioridad;
    }

    public String getTipo() { return tipo; }
    public EstadoSolicitud getEstado() { return estado; }
    public Prioridad getPrioridad() { return prioridad; }
}