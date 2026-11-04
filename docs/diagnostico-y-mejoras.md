# Diagnóstico y mejoras incrementales

Revisión del 4 de noviembre de 2026. Complementa el proyecto y el informe previo de Matías; no los reemplaza. Fuentes: `ConsignaTPI.pdf`, el informe de solicitudes académicas de Matías y `TPI/CONTEXTO.md` de la carpeta local de la materia. En la copia del disco externo, ese contexto tenía 0 bytes al revisarlo; la copia local tenía 4037 bytes y se leyó completa.

## Alcance y calidad

El sistema mantiene los cinco casos de uso: registrar, cambiar estado, asignar responsable, buscar y reportar. El personal de Alumnado opera sobre solicitudes de estudiantes. Java 21 y persistencia en memoria permiten demostrar el caso sin infraestructura adicional.

La calidad externa se observa en los resultados de esos casos de uso. La calidad interna se evalúa en la separación de responsabilidades, la claridad de los tipos, los errores explícitos y la facilidad de comprobar cambios. La demo inicial funciona con datos válidos aunque su estructura concentra responsabilidades y sus filtros combinados son incorrectos.

## Diagnóstico de la base

La ubicación de esta tabla corresponde al tag `estado-inicial` (`8dae67c`), no al código actual. Se puede consultar con `git show estado-inicial:GestorSolicitudes.java` y los demás nombres indicados.

| # | Problema y evidencia | Ubicación en la base | Impacto y riesgo de mantenimiento | Prioridad |
|---|---|---|---|---|
| 1 | God Class: almacena, registra, busca, actualiza e imprime | `GestorSolicitudes`, clase completa | Un cambio de presentación puede afectar operaciones de negocio; riesgo alto | Alta |
| 2 | Atributos públicos sin encapsulamiento | `Solicitud`, `Estudiante`, `Responsable` | Cualquier consumidor puede sobrescribir datos; riesgo alto | Alta |
| 3 | Primitive Obsession: estado como String y prioridad como int | `Solicitud.estado`, `Solicitud.prioridad` | Permite estados y prioridades fuera del dominio; riesgo alto | Alta |
| 4 | Literales repetidos y códigos sin nombre: `1`, `2`, `3`, `"PENDIENTE"` | `Main`, constructor de `Solicitud`, `listarReporte` | Un cambio exige localizar valores dispersos; riesgo medio | Alta |
| 5 | Cadena `if/else if` acepta coincidir en cualquier criterio en vez de combinarlos | `GestorSolicitudes.buscarSolicitudes` | Búsqueda combinada devuelve solicitudes que no cumplen todo; riesgo alto | Alta |
| 6 | Id inexistente se ignora y datos inválidos no se rechazan explícitamente | `registrarSolicitud`, `cambiarEstado`, `asignarResponsable` | Errores silenciosos o datos inconsistentes; riesgo medio | Media |
| 7 | Solo hay una demo; no hay verificaciones automatizadas | `Main`, ausencia de clase de pruebas | Las regresiones dependen de una inspección manual; riesgo alto | Alta |
| 8 | Cálculo de totales e impresión mezclados | `GestorSolicitudes.listarReporte` | Cambiar el formato obliga a editar el cálculo; riesgo medio | Media |

Los puntos 3 y 4 están relacionados: uno trata del modelo de tipos y el otro de la dispersión de literales. No representan ocho problemas nuevos introducidos en esta revisión.

## Refactorings ya realizados por el equipo

Se conservan los refactorings de Matías en `3229cc4` (`etapa-refactoring`).

| Técnica | Antes | Después | Evidencia de verificación actual |
|---|---|---|---|
| Extract Method | Condiciones dentro de `buscarSolicitudes` | `SolicitudService.cumpleFiltro` | Búsquedas por prioridad, tipo y combinación de criterios |
| Move Method / reubicación de responsabilidad | `GestorSolicitudes.contadorId` se incrementa en el registro | `InMemorySolicitudRepository.generarNuevoId` | Id inicial y registros inválidos que no consumen ids |
| Introduce Parameter Object | Parámetros sueltos de registro y búsqueda | `DatosSolicitud`, `FiltroBusqueda` | Altas y consultas de las pruebas y la demo |
| Sustitución de códigos por enums | Prioridad int y estado String | `Prioridad`, `EstadoSolicitud` | Cambios de estado y totales del reporte |

La extracción del filtro estuvo acompañada por la corrección funcional de la combinación de criterios. La separación del cálculo y la impresión en `ReporteService` y `ReportePresenter` también reubica responsabilidades. No se presentan esas correcciones funcionales como refactorings de comportamiento idéntico.

## Aportes y decisiones

| Origen | Aporte verificable |
|---|---|
| Matías Alochis | Sistema inicial (`8dae67c`), refactorings (`3229cc4`), pruebas (`ab818b2`), checklist (`94b8d86`) y README (`bbd3b62`) |
| Joel, según el historial Git | `9d0f3c7`: contrato de generación de id en la interfaz, dependencia de SolicitudRepository, validaciones y pruebas sin depender de `-ea` |
| Revisión solicitada por Santiago con asistencia de Codex | Validación de fecha, contrato de filtro vacío/null, pruebas adicionales, comparación reproducible y actualización de documentación. Cambios registrados en la rama `santiago/mejoras-tpi`, creada desde `main` (`0e398bd`) |

Esta tabla identifica evidencia del repositorio y de la sesión. Cada integrante debe revisar y explicar su contribución para la defensa; no atribuye a una persona la autoría de código generado por IA sin revisión.

1. **Mantener la arquitectura existente.** Se conserva SRP en servicios, repositorio y presentación, y DIP mediante `SolicitudRepository`. No se agregan capas, base de datos ni frameworks.
2. **Validar antes de generar el id.** `validarFecha` usa `LocalDate.parse` para rechazar fechas inexistentes, nulas o vacías. Se conserva String en la API para limitar el cambio. Es una mejora funcional sobre entradas inválidas, además de una extracción de validación.
3. **Aclarar el contrato del filtro.** Tipo vacío o en blanco omite ese criterio, como indicaba el comentario original. Un objeto filtro null se rechaza antes de recorrer datos; antes fallaba con NullPointerException si había solicitudes y pasaba inadvertido si no había ninguna. El resto de las comparaciones sigue siendo exacto.
4. **Probar sin agregar dependencias.** Se conserva el mecanismo de verificación de Joel y se agregan casos de borde. El script usa directorios temporales y el tag original sin cambiar de rama.
5. **Aplicar SOLID donde aporta.** Los enums restringen valores válidos, pero no garantizan por sí solos OCP: agregar un estado requiere revisar reporte, presentación y pruebas. No se crea una jerarquía para demostrar LSP; solo hay una implementación del repositorio. La interfaz acotada agrupa operaciones de persistencia usadas por el servicio, sin justificar divisiones adicionales por ISP.

## Evidencia antes y después

Sobre `0e398bd`, antes de esta revisión, las pruebas existentes pasaban con **14 verificaciones**. Después, `PruebasSolicitudes` ejecuta **51 verificaciones**. El comando reproducible es:

```sh
bash scripts/verificar.sh
```

| Caso | Antes de esta revisión | Resultado esperado ahora |
|---|---|---|
| Demo con datos válidos | Tres solicitudes, una por estado | Misma salida que `0e398bd` |
| Fecha null, vacía, formato incorrecto o día inexistente | Se guardaba sin validar | IllegalArgumentException, sin guardar ni consumir id |
| Fecha `2024-02-29` | Se guardaba | Se acepta y conserva |
| Tipo vacío o en blanco en filtro | No había coincidencias | Omite tipo y respeta estado/prioridad |
| Filtro null | Error dependiente de si había datos | IllegalArgumentException siempre |
| Nombre/legajo inválidos, estado/responsable null | Validaciones de Joel | Se conservan y se verifican sin alterar datos |
| Reporte vacío y con los tres estados | Sin cobertura completa | Totales cero o un total por estado, respectivamente |

El script compila con advertencias activadas, ejecuta las pruebas sin `-ea` y compara la demo actual con `estado-inicial`. Excluye los mensajes informativos del gestor y transforma prioridades 1/2/3 en ALTA/MEDIA/BAJA. Esa comparación cubre el escenario de la demo; las pruebas adicionales cubren filtros combinados y entradas inválidas. No se afirma equivalencia literal de toda salida ni de todos los escenarios.

## Límites y cierre de la entrega

La persistencia sigue siendo temporal. No se imponen transiciones de estado ni obligatoriedad de responsable para resolver, porque no son requisitos establecidos. Las entidades aún exponen setters y los reportes conservan referencias mutables; se deja documentado el límite sin ampliar el alcance.

La consigna exige `etapa-diagnostico`, ausente en los tags locales revisados. También escribe `estadorefactorizado`, mientras que el proyecto usa `estado-refactorizado`; el equipo debe verificar la denominación al preparar la entrega. Esta revisión conserva los tags y el historial existentes.

El informe previo de Matías describe una entrega individual y pruebas con assert. Para la entrega grupal corresponde integrar los aportes posteriores, actualizar esas referencias, completar las conclusiones personales y enlazar el video y el anexo de evidencias. El registro de esta asistencia se encuentra en [registro-ia.md](registro-ia.md); debe incorporarse con el intercambio relevante al anexo requerido por la cátedra.
