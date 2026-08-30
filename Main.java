public class Main {
    public static void main(String[] args) {
        GestorSolicitudes gestor = new GestorSolicitudes();

        gestor.registrarSolicitud("Cambio de comisión", 1, "2026-08-01", "Daniel Danielson", "12345");
        gestor.registrarSolicitud("Certificado alumno regular", 3, "2026-08-02", "Juan Janigues", "67890");
        gestor.registrarSolicitud("Equivalencia", 2, "2026-08-03", "Cecilia Cecinar", "11223");

        gestor.asignarResponsable(1, "Marta Magnaterra", "Alumnado");
        gestor.cambiarEstado(1, "EN_PROCESO");
        gestor.cambiarEstado(2, "RESUELTA");

        gestor.buscarSolicitudes("Equivalencia", null, 0);
        gestor.listarReporte();
    }
}