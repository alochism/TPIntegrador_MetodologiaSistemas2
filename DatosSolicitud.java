// agrupo acá los datos de alta para no pasar 5 parámetros sueltos al service
public class DatosSolicitud {
    private final String tipo;
    private final Prioridad prioridad;
    private final String fecha;
    private final String nombreEstudiante;
    private final String legajoEstudiante;

    public DatosSolicitud(String tipo, Prioridad prioridad, String fecha, String nombreEstudiante, String legajoEstudiante) {
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.fecha = fecha;
        this.nombreEstudiante = nombreEstudiante;
        this.legajoEstudiante = legajoEstudiante;
    }

    public String getTipo() { return tipo; }
    public Prioridad getPrioridad() { return prioridad; }
    public String getFecha() { return fecha; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public String getLegajoEstudiante() { return legajoEstudiante; }
}