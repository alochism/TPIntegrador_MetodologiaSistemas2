public class ReportePresenter {
    public void imprimir(Reporte reporte) {
        for (Solicitud s : reporte.getSolicitudes()) {
            String responsable = s.getResponsable() != null ? s.getResponsable().getNombre() : "sin asignar";
            System.out.println(s.getId() + " | " + s.getTipo() + " | prioridad " + s.getPrioridad() + " | " + s.getEstado() + " | " + responsable);
        }
        System.out.println("Total pendientes: " + reporte.getPendientes());
        System.out.println("Total en proceso: " + reporte.getEnProceso());
        System.out.println("Total resueltas: " + reporte.getResueltas());
    }
}