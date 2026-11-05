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
- `PruebasSolicitudes`: pruebas que lanzan `AssertionError` ante una falla, sin depender de `-ea`

## Instalación y uso

Requiere JDK 21 instalado.

Compilar:

```sh
javac -encoding UTF-8 *.java
```

Ejecutar la demo:

```sh
java Main
```

Ejecutar las pruebas:

```sh
java PruebasSolicitudes
```

En Linux o con Bash, también se puede compilar y probar en un directorio temporal y comparar la demo con el tag `estado-inicial`:

```sh
bash scripts/verificar.sh
```

Esta comparación requiere Git y el tag `estado-inicial` disponible localmente. Normaliza las prioridades numéricas originales y excluye los mensajes informativos eliminados del gestor; compara las solicitudes, responsables, estados y totales. No modifica la rama ni los archivos del proyecto.


## Funcionalidades

- Registrar solicitud
- Cambiar estado de una solicitud
- Asignar responsable
- Buscar/filtrar solicitudes por tipo, estado y prioridad
- Listar reporte con totales por estado

## Validaciones y búsquedas

El registro requiere tipo, prioridad, fecha, nombre y legajo. La fecha debe ser válida en formato ISO-8601, por ejemplo `2026-08-01`. Los datos inválidos se rechazan con `IllegalArgumentException` antes de guardar o generar el id.

Los filtros combinan los criterios con AND. Un criterio `null` se omite; un tipo vacío o en blanco también se omite. Los tipos con contenido se comparan exactamente. Para listar todo, usar `new FiltroBusqueda(null, null, null)`; pasar el objeto filtro en `null` produce `IllegalArgumentException`.

La persistencia es en memoria y dura una ejecución. Se conservan los cambios de estado que ya permitía el proyecto; no se agregan reglas de transición.

## Historial del proyecto

El repositorio conserva el historial de evolución del sistema mediante tags:

- `estado-inicial`: versión original con problemas de diseño intencionales
- `etapa-diagnostico`: diagnóstico documentado en docs/diagnostico-y-mejoras.md
- `etapa-refactoring`: aplicación de SOLID, Clean Code y refactorings
- `etapa-pruebas`: pruebas automatizadas con assert
- `etapa-docs`: checklist de code review
- `estadorefactorizado`: versión final refactorizada y validada

El diagnóstico se documentó después del código inicial (commit bb6a17a), por eso `etapa-diagnostico` apunta a ese commit y no a la versión inicial.

## Documentación de la revisión

- [Diagnóstico, decisiones, aportes y evidencia](docs/diagnostico-y-mejoras.md)
- [Checklist de code review](docs/checklist-revision-codigo.md)
- [Registro de asistencia con IA](docs/registro-ia.md)

La documentación complementa el trabajo previo del equipo. El informe PDF grupal, el anexo y el video deben integrar los aportes y la participación real de cada integrante.