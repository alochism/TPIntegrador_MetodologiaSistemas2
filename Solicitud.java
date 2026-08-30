public class Solicitud {
    public int id;
    public String tipo;
    public int prioridad; // 1 alta, 2 media, 3 baja (sin constantes, a propósito)
    public String estado; // "PENDIENTE", "EN_PROCESO", "RESUELTA"
    public String fecha;
    public Estudiante estudiante;
    public Responsable responsable;

    public Solicitud(int id, String tipo, int prioridad, String fecha, Estudiante estudiante) {
        this.id = id;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.fecha = fecha;
        this.estudiante = estudiante;
        this.estado = "PENDIENTE";
        this.responsable = null;
    }
}