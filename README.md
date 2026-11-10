# Laboratorio-4.-Polimorfismo Ricardo Sandoval, 26353
# Laboratorio 4 – Polimorfismo
## ArenaGT

### Descripción
ArenaGT es una aplicación de consola desarrollada en Java para administrar torneos deportivos de distintas disciplinas mediante programación orientada a objetos.

El sistema permite registrar participantes, crear torneos, gestionar inscripciones, programar eventos, registrar resultados y consultar tablas de posiciones, calendarios y reportes.

### Requisitos
- Java JDK 17 o superior.
- Una terminal para compilar y ejecutar el programa.

### Estructura del proyecto
- `ArenaGT/src/arenagt/`: archivos fuente Java.
- `ArenaGT/bin/`: archivos compilados.
- `ArenaGT/docs/`: informe y diagramas UML.

### Instrucciones de compilación

Desde la raíz del repositorio, ingresar a la carpeta del proyecto:

```bash
cd ArenaGT
```

Compilar los archivos Java:

```bash
javac -encoding UTF-8 -d bin src/arenagt/*.java
```

### Instrucciones de ejecución

Ejecutar el programa principal:

```bash
java -cp bin arenagt.Main
```

El sistema mostrará un menú interactivo con las opciones disponibles.

### Ejecución de pruebas

Para ejecutar las pruebas automatizadas:

```bash
java -cp bin arenagt.Pruebas
```

### Funcionalidades principales
- Registro de participantes y torneos.
- Cotización y cobro de inscripciones.
- Gestión de cinco disciplinas deportivas.
- Programación y reprogramación de eventos.
- Registro y validación de resultados.
- Tablas de posiciones y desempates.
- Calendarios ordenados cronológicamente.
- Reportes de ingresos, premios y ranking de empresas.

### Documentación
El informe de análisis, diseño UML y evidencia de pruebas se encuentra en la carpeta `docs`.
