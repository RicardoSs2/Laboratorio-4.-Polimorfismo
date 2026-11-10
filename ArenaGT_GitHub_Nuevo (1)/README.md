# ArenaGT — Laboratorio 4, Polimorfismo

Aplicación de consola Java; no utiliza bibliotecas externas, archivos de datos ni base de datos.

## Estructura
- `src/arenagt/`: código fuente Java.
- `bin/`: archivos `.class` compilados.
- `docs/`: informe PDF y evidencias.

## Compilar (Java 11+)
Desde la carpeta `ArenaGT`:

```bash
javac -encoding UTF-8 -d bin src/arenagt/*.java
java -cp bin arenagt.Main
```

## Pruebas automatizadas

```bash
java -cp bin arenagt.Pruebas
```

Se precargan cinco torneos y ocho participantes (sin cobrar inscripciones iniciales). Todos los eventos de los torneos en juego se programan; algunos tienen resultados para demostrar desempates. Los códigos de eventos se generan comenzando por `EV1`.

En el menú, para resultados de relevos use `D` para descalificación. Las fechas siguen `AAAA-MM-DD` y horas enteras de 0 a 23.

## Entrega
Adjuntar PDF de `docs/Informe_ArenaGT.pdf` en Canvas y publicar todo el proyecto en un repositorio GitHub público. La URL real del repositorio debe copiarse a Canvas tras publicarlo.

## Revisión de la entrega

- Ejecutar las pruebas: **115/115 verificaciones correctas** en la versión comprobada.
- Consultar `docs/pruebas_ejecutadas.txt` y `docs/sesion_consola.txt` como evidencias.
- El diagrama está **incluido en el informe PDF** y adicionalmente en `docs/UML_ArenaGT_Ampliable.pdf` para verlo con zoom.
- El proyecto no se ha publicado en GitHub: publicar el repositorio público y copiar su URL real al entregar.
- Las pruebas automatizadas no sustituyen la evaluación de la persona docente.

## Correcciones según observaciones docentes

- Se reemplazó el uso de `instanceof` en la lógica de resultados por despacho polimórfico sobre `Evento` y `Resultado`.
- Los diagramas vectoriales completos (`docs/UML_ArenaGT_Ampliable.pdf`) muestran firmas compiladas, visibilidad, constructores, relaciones y multiplicidades. Se anexaron al informe entregable.
- Se reforzaron las pruebas de ingresos, cotizaciones, resultados incompatibles, estados y descalificaciones.
- **Resultado actual:** 115 pruebas aprobadas de 115 ejecutadas (sin fallos detectados).
