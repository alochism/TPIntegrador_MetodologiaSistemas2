# Registro de asistencia con IA

Fecha: 4 de noviembre de 2026. Herramienta: Codex (OpenAI).

## Solicitud y aclaración

Santiago solicitó leer la carpeta de Metodología II, especialmente TPI, y mejorar este proyecto según sus consignas: «incorpora mejora razonables y no hagas sobre ingeniería ni pises todo el proyecto, debe verse el aporte de mis compañeros y el mio».

Luego pidió revisar nuevamente los archivos de contexto porque no debían estar vacíos. Se comprobó que los del disco externo tenían 0 bytes y se localizó y leyó la copia completa de `TPI/CONTEXTO.md` en la carpeta local de la materia. No se modificaron los originales.

## Finalidad y grado de intervención

La asistencia incluyó lectura del código y los documentos disponibles, consulta del historial Git, propuesta e implementación de validación de fecha y manejo del filtro vacío/null, ampliación de pruebas, script de comparación antes/después y documentación de diagnóstico y revisión. Codex generó estos cambios y ejecutó las verificaciones; no fue únicamente asistencia de redacción.

Se conservaron la arquitectura, las clases del proyecto y los aportes previos identificables de Matías y Joel. La revisión del equipo y su comprensión para la defensa siguen pendientes de realizar por los integrantes; este registro no certifica aprobación humana.

## Evidencias y decisiones

- [Diagnóstico, decisiones y aportes](diagnostico-y-mejoras.md).
- [Checklist actualizado](checklist-revision-codigo.md).
- `PruebasSolicitudes.java` y `scripts/verificar.sh`, para reproducir la validación.
- Commits de la rama `santiago/mejoras-tpi`, consultables con `git log main..santiago/mejoras-tpi` y `git diff main...santiago/mejoras-tpi`. La rama parte de `main` (`0e398bd`), rama principal del repositorio; no existe una rama `master`.

Este archivo resume la intervención y no sustituye la exportación del intercambio relevante para el anexo PDF/ZIP que solicita la consigna. No se reconstruyen ni inventan conversaciones anteriores de otros integrantes con herramientas de IA.
