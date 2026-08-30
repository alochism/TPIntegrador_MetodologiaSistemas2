public class Estudiante {
    private final String nombre;
    private final String legajo;

    public Estudiante(String nombre, String legajo) {
        this.nombre = nombre;
        this.legajo = legajo;
    }

    public String getNombre() { return nombre; }
    public String getLegajo() { return legajo; }
}