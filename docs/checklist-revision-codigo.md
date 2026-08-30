# Checklist de revisión de código

Aplicado sobre la versión refactorizada del sistema de gestión de solicitudes académicas.

| # | Criterio | Cumple | Observación |
|---|----------|--------|-------------|
| 1 | Responsabilidad única por clase (SRP) | Sí | Separado en SolicitudService, InMemorySolicitudRepository, ReporteService y ReportePresenter, en vez de una God Class |
| 2 | Nombres claros y significativos | Sí | Variables y métodos con nombres descriptivos (solicitud, estudiante, cumpleFiltro), sin abreviaturas sueltas |
| 3 | Manejo de errores explícito | Sí | obtenerOFallar lanza IllegalArgumentException ante id inexistente, validado con test |
| 4 | Sin números o strings mágicos | Sí | Estado y prioridad son enums (EstadoSolicitud, Prioridad) |
| 5 | Bajo acoplamiento entre componentes | Sí | SolicitudService depende de la interfaz SolicitudRepository, no de una implementación concreta (DIP) |
| 6 | Sin duplicación de lógica | Sí | Filtro combinado extraído a cumpleFiltro, reemplaza los else if duplicados |
| 7 | Pruebas que validen el comportamiento | Sí | PruebasSolicitudes con asserts, ejecutada con -ea, todos los casos pasan |
| 8 | Comportamiento externo conservado respecto a la versión original | Sí | Salida comparada y equivalente entre versión inicial y refactorizada |

## Feedback profesional

El refactor resuelve los 8 code smells identificados en la etapa de diagnóstico sin alterar el comportamiento externo. Como corrección pendiente a futuro (no bloqueante para este trabajo): agregar manejo de errores también en registrarSolicitud ante datos nulos o vacíos, hoy no hay esa validación.

## Criterio de aceptación técnica

Se acepta la versión refactorizada porque cumple los 8 puntos del checklist y las pruebas automatizadas pasan sin fallos.