# Sistema de Gestión de Solicitudes Académicas

TP Integrador de Metodología de Sistemas II. Sistema que permite registrar solicitudes de estudiantes, cambiar su estado, asignar un responsable, buscar/filtrar solicitudes y generar un reporte básico.

## Tecnologías

- Java 21

## Estructura del proyecto

- `Estudiante`, `Responsable`, `Solicitud`: entidades del dominio
- `EstadoSolicitud`, `Prioridad`: enums de dominio
- `DatosSolicitud`, `FiltroBusqueda`: objetos de parámetros
- `SolicitudRepository` / `InMemorySolicitudRepository`: persistencia en memoria
- `SolicitudService`: lógica de negocio
- `ReporteService` / `ReportePresenter`: generación y presentación del reporte
- `Main`: demo de ejecución
- `PruebasSolicitudes`: pruebas con `assert`

## Instalación y uso

Requiere JDK 21 instalado.

Compilar:
javac *.java

Ejecutar la demo:
java Main

Ejecutar las pruebas (con assertions activadas):
java -ea PruebasSolicitudes


## Funcionalidades

- Registrar solicitud
- Cambiar estado de una solicitud
- Asignar responsable
- Buscar/filtrar solicitudes por tipo, estado y prioridad
- Listar reporte con totales por estado

## Historial del proyecto

El repositorio conserva el historial de evolución del sistema mediante tags:

- `estado-inicial`: versión original con problemas de diseño intencionales
- `etapa-refactoring`: aplicación de SOLID, Clean Code y refactorings
- `etapa-pruebas`: pruebas automatizadas con assert
- `estado-refactorizado`: cierre del refactor validado
- `etapa-docs`: checklist de code review

Documentación adicional en la carpeta `/docs`.