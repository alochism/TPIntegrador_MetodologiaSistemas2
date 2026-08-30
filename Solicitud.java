public class Solicitud {
    private final int id;
    private final String tipo;
    private final Prioridad prioridad;
    private final String fecha;
    private final Estudiante estudiante;
    private EstadoSolicitud estado;
    private Responsable responsable;

    public Solicitud(int id, String tipo, Prioridad prioridad, String fecha, Estudiante estudiante) {
        this.id = id;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.fecha = fecha;
        this.estudiante = estudiante;
        this.estado = EstadoSolicitud.PENDIENTE; // toda solicitud arranca pendiente
    }

    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public Prioridad getPrioridad() { return prioridad; }
    public String getFecha() { return fecha; }
    public Estudiante getEstudiante() { return estudiante; }
    public EstadoSolicitud getEstado() { return estado; }
    public Responsable getResponsable() { return responsable; }

    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }
    public void setResponsable(Responsable responsable) { this.responsable = responsable; }
}