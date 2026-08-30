import java.util.ArrayList;
import java.util.List;

public class GestorSolicitudes {
    private List<Solicitud> solicitudes = new ArrayList<>();
    private int contadorId = 1;

    public void registrarSolicitud(String tipo, int prioridad, String fecha, String nombreEstudiante, String legajoEstudiante) {
        if (tipo != null) {
            Estudiante e = new Estudiante(nombreEstudiante, legajoEstudiante);
            Solicitud s = new Solicitud(contadorId, tipo, prioridad, fecha, e);
            solicitudes.add(s);
            contadorId = contadorId + 1;
            System.out.println("Solicitud registrada con id " + s.id);
        }
    }

    public void cambiarEstado(int id, String nuevoEstado) {
        for (int i = 0; i < solicitudes.size(); i++) {
            if (solicitudes.get(i).id == id) {
                solicitudes.get(i).estado = nuevoEstado;
                System.out.println("Estado actualizado");
            }
        }
    }

    public void asignarResponsable(int id, String nombreResp, String areaResp) {
        for (int i = 0; i < solicitudes.size(); i++) {
            if (solicitudes.get(i).id == id) {
                solicitudes.get(i).responsable = new Responsable(nombreResp, areaResp);
                System.out.println("Responsable asignado");
            }
        }
    }

    public void buscarSolicitudes(String tipo, String estado, int prioridad) {
        for (int i = 0; i < solicitudes.size(); i++) {
            Solicitud s = solicitudes.get(i);
            if (tipo != null && s.tipo.equals(tipo)) {
                System.out.println(s.id + " - " + s.tipo + " - " + s.estado);
            } else if (estado != null && s.estado.equals(estado)) {
                System.out.println(s.id + " - " + s.tipo + " - " + s.estado);
            } else if (prioridad != 0 && s.prioridad == prioridad) {
                System.out.println(s.id + " - " + s.tipo + " - " + s.estado);
            }
        }
    }

    public void listarReporte() {
        int pendientes = 0;
        int enProceso = 0;
        int resueltas = 0;
        for (int i = 0; i < solicitudes.size(); i++) {
            Solicitud s = solicitudes.get(i);
            if (s.estado.equals("PENDIENTE")) {
                pendientes = pendientes + 1;
            } else if (s.estado.equals("EN_PROCESO")) {
                enProceso = enProceso + 1;
            } else if (s.estado.equals("RESUELTA")) {
                resueltas = resueltas + 1;
            }
            System.out.println(s.id + " | " + s.tipo + " | prioridad " + s.prioridad + " | " + s.estado + " | " + (s.responsable != null ? s.responsable.nombre : "sin asignar"));
        }
        System.out.println("Total pendientes: " + pendientes);
        System.out.println("Total en proceso: " + enProceso);
        System.out.println("Total resueltas: " + resueltas);
    }
}