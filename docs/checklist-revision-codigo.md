# Checklist de revisión de código

Actualizado el 4 de noviembre de 2026 sobre la versión refactorizada y las mejoras posteriores del equipo. El checklist original se conserva en el tag `etapa-docs`.

| # | Criterio | Cumple | Observación |
|---|----------|--------|-------------|
| 1 | Responsabilidad única por clase (SRP) | Sí | Separado en SolicitudService, InMemorySolicitudRepository, ReporteService y ReportePresenter, en vez de una God Class |
| 2 | Nombres claros y significativos | Sí | Variables y métodos con nombres descriptivos (solicitud, estudiante, cumpleFiltro), sin abreviaturas sueltas |
| 3 | Manejo de errores explícito | Sí | Id inexistente, datos de alta inválidos y argumentos null se rechazan con IllegalArgumentException; pruebas de rechazo sin alterar datos |
| 4 | Sin números o strings mágicos | Sí | Estado y prioridad son enums (EstadoSolicitud, Prioridad) |
| 5 | Bajo acoplamiento entre componentes | Sí | SolicitudService depende de la interfaz SolicitudRepository, no de una implementación concreta (DIP) |
| 6 | Sin duplicación de lógica | Sí | Filtro combinado extraído a cumpleFiltro, reemplaza los else if duplicados |
| 7 | Pruebas que validen el comportamiento | Sí | PruebasSolicitudes ejecuta 51 verificaciones sin necesitar -ea |
| 8 | Comportamiento esperado conservado en la demo original | Sí | scripts/verificar.sh compara ambas demos normalizando prioridades y omitiendo los mensajes informativos originales |
| 9 | Contrato de búsqueda consistente | Sí | Tipo vacío se omite, criterios se combinan con AND y filtro null se rechaza aun sin datos |
| 10 | Evidencia y límites documentados | Sí | Diagnóstico con 8 problemas de la base, 4 refactorings previos, decisiones y distinción de aportes en diagnostico-y-mejoras.md |

## Feedback profesional

La observación original sobre datos de alta nulos o vacíos fue atendida por Joel en `9d0f3c7`. Esta revisión la complementa con validación de fecha y casos de prueba adicionales. También corrige la diferencia entre el comentario del filtro y su comportamiento ante tipo vacío.

La demo conserva la información funcional de la base, con diferencias deliberadas en mensajes y representación de prioridades. Los filtros combinados ya habían sido corregidos respecto de la base. Las nuevas validaciones mejoran entradas inválidas; no son cambios de estructura sin efecto observable. La comparación de la demo no demuestra equivalencia de todos los escenarios posibles.

Como límite conocido, las entidades permiten cambios directos mediante setters y el reporte conserva referencias a solicitudes mutables. El flujo probado utiliza SolicitudService; endurecer todo el modelo requeriría una decisión adicional del equipo.

## Criterio de aceptación técnica

La aceptación técnica de esta revisión requiere compilar con Java 21, ejecutar las 51 verificaciones y pasar la comparación de la demo con `bash scripts/verificar.sh`. La aceptación académica final también requiere completar el informe grupal, anexo, video y trazabilidad de integrantes según la consigna.
