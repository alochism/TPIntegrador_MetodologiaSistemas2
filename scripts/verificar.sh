#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")/.."
temporal=$(mktemp -d)
trap 'rm -rf "$temporal"' EXIT
mkdir -p "$temporal/actual" "$temporal/inicial"

javac -encoding UTF-8 -Xlint:all -d "$temporal/actual" ./*.java
java -cp "$temporal/actual" PruebasSolicitudes
java -cp "$temporal/actual" Main > "$temporal/actual.txt"

# Extrae la base a un directorio temporal sin cambiar la rama ni el árbol de trabajo.
git archive estado-inicial -- '*.java' | tar -x -C "$temporal/inicial"
javac -encoding UTF-8 -d "$temporal/inicial" "$temporal/inicial/"*.java
java -cp "$temporal/inicial" Main > "$temporal/inicial.txt"

# Las diferencias intencionales son los mensajes informativos del gestor original
# y las prioridades, que antes eran números y ahora son enums.
sed -E \
    -e '/^Solicitud registrada con id [0-9]+$/d' \
    -e '/^(Responsable asignado|Estado actualizado)$/d' \
    -e 's/ \| prioridad 1 \|/ | prioridad ALTA |/g' \
    -e 's/ \| prioridad 2 \|/ | prioridad MEDIA |/g' \
    -e 's/ \| prioridad 3 \|/ | prioridad BAJA |/g' \
    "$temporal/inicial.txt" > "$temporal/inicial-normalizado.txt"

diff -u "$temporal/inicial-normalizado.txt" "$temporal/actual.txt"
echo 'Demo equivalente a estado-inicial (sin mensajes informativos y con prioridades normalizadas).'
