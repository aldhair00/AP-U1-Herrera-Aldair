# AP-U1 — AforoSeguro: control de aforo distribuido

**Asignatura:** Aplicaciones Distribuidas (ISR-701)  
**Universidad:** Universidad Técnica Estatal de Quevedo (UTEQ)  
**Carrera:** Software  
**Estudiante:** [Apellidos y nombres]  
**Periodo:** SPA 2026–2027

## 1. Descripción

AforoSeguro es un prototipo de control de aforo distribuido para un recinto con tres puertas de acceso. Un coordinador central mantiene el contador global de personas, autoriza o rechaza las solicitudes de entrada, procesa las salidas y supervisa el estado de las puertas.

El aporte práctico integra comunicación entre procesos, autenticación mediante token, control de concurrencia, detección de fallos mediante latidos (*heartbeats*) y relojes lógicos de Lamport para ordenar los eventos.

> **Importante:** completa este README para que describa exactamente tu implementación. No declares mecanismos, comandos, resultados ni código reutilizado que no existan en el repositorio.

## 2. Arquitectura

| Proceso | Identificador | Función |
|---|---:|---|
| Coordinador | 0 | Mantiene el aforo global, valida tokens, responde solicitudes y supervisa puertas. |
| Puerta Norte (P1) | 1 | Solicita entradas, informa salidas y consulta el estado. |
| Puerta Sur (P2) | 2 | Solicita entradas, informa salidas y consulta el estado. |
| Puerta Este (P3) | 3 | Solicita entradas, informa salidas y consulta el estado. |

**Tecnología utilizada:** [Java 21 + sockets TCP / Java RMI / gRPC / Python 3.11+]  
**Protocolo de comunicación:** [describir el protocolo real]  
**Host y puertos:** [indicar los valores configurados]

## 3. Estructura del repositorio

```text
AP-U1-Herrera-Aldair/
├── src/                    # Código fuente del coordinador, puertas y utilidades
├── config.properties       # Aforo, token, host y puertos (no publicar secretos reales)
├── logs/                   # Registros de procesos y orden total
├── docs/
│   ├── informe.tex         # Fuente LaTeX del informe
│   ├── informe.pdf         # Informe compilado
│   └── img/                # Capturas de los escenarios E0–E6
└── README.md
```

Ajusta esta estructura si tus nombres o directorios reales son diferentes.

## 4. Requisitos previos

- JDK 21, si la implementación está hecha en Java; o Python 3.11 o superior, si está hecha en Python.
- Git.
- Una distribución LaTeX con `pdflatex` instalada (por ejemplo, TeX Live o MiKTeX).
- Los puertos indicados en `config.properties` disponibles en el equipo.

## 5. Configuración

Completa `config.properties` con los parámetros que utiliza tu programa. Como mínimo, la actividad solicita:

```properties
aforo=10
token=CAMBIAR_POR_UN_TOKEN_LOCAL
host=127.0.0.1
# Agregar aquí los puertos reales del coordinador y de las puertas.
```

No incluyas tokens personales ni credenciales reales en un repositorio público. Si el programa utiliza otros nombres de propiedades, documenta los nombres efectivos.

## 6. Compilación y ejecución

Los comandos siguientes son una **plantilla**: reemplaza los marcadores por las clases, módulos, paquetes y argumentos que realmente implementaste.

### 6.1. Compilar

**Si utilizaste Java:**

```powershell
# Desde la raíz del proyecto; adaptar al sistema de compilación usado.
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

Si utilizaste paquetes Java, classpath o una herramienta de construcción, documenta aquí el comando exacto que probaste.

**Si utilizaste Python:** no se requiere una compilación previa; verifica la sintaxis con el comando apropiado para tus módulos.

### 6.2. Orden de arranque

1. Iniciar el coordinador.
2. Iniciar P1 (Norte).
3. Iniciar P2 (Sur).
4. Iniciar P3 (Este).
5. Ejecutar las órdenes `entrar`, `salir` y `estado` desde las consolas de las puertas.

**Comandos reales de ejecución:**

```text
Coordinador: [comando exacto]
Puerta P1:   [comando exacto y argumentos]
Puerta P2:   [comando exacto y argumentos]
Puerta P3:   [comando exacto y argumentos]
```

### 6.3. Prueba de carga

```text
[comando exacto para ejecutar PruebaCarga]
```

La prueba debe lanzar 30 solicitudes concurrentes (10 por puerta) con aforo 10. Registra los valores realmente obtenidos en el informe: permitidas, rechazadas y total final.

### 6.4. Modo inseguro

```text
[comando exacto para iniciar el coordinador con el argumento --inseguro]
```

Documenta el nombre exacto del argumento si tu implementación utiliza una variante distinta.

### 6.5. Orden total de eventos

```text
[comando exacto para ejecutar OrdenTotal]
```

El resultado esperado según el planteamiento es `logs/orden_total.log`, ordenado por marca de Lamport y, en caso de empate, por identificador de proceso.

## 7. Formato de mensajes

Documenta el formato que implementaste. Si utilizaste el formato JSON sugerido en el planteamiento, puedes describirlo así:

**Solicitud de ejemplo (ilustrativa):**

```json
{"tipo":"ENTRAR","token":"...","puerta":2,"lamport":17}
```

**Respuesta de ejemplo (ilustrativa):**

```json
{"tipo":"RESPUESTA","resultado":"PERMITIDA","dentro":8,"aforo":10,"lamport":19}
```

Explica cómo delimitas los mensajes (por ejemplo, una línea por mensaje o longitud prefijada) y qué respuestas se generan para aforo completo, token inválido, ausencia de personas o coordinador no disponible. Sustituye estos ejemplos si tu protocolo real es diferente.

## 8. Escenarios y evidencias

Guarda las capturas en `docs/img/`. Cada captura debe permitir verificar la consola, el usuario de Windows, el nombre del equipo y la fecha/hora del sistema, conforme al planteamiento.

| Escenario | Evidencia | Resultado que debes documentar |
|---|---|---|
| E0 — Sin red | `docs/img/E0.png` | Estado de los adaptadores desconectados y primer commit con el código de sesión 1. |
| E1 — Operación normal | `docs/img/E1.png` | Entradas por las tres puertas, salida por P2 y total coherente consultado desde las puertas. |
| E2 — Aforo y token | `docs/img/E2.png` | Rechazo de la undécima entrada y de una solicitud con token inválido; registro del rechazo. |
| E3 — Concurrencia | `docs/img/E3.png` | Cifras de la prueba en modo normal y modo inseguro. |
| E4 — Puerta caída | `docs/img/E4.png` | P3 detectada como caída y posteriormente como activa. |
| E5 — Coordinador caído | `docs/img/E5.png` | Comportamiento de una puerta al no poder contactar al coordinador y justificación. |
| E6 — Orden total | `docs/img/E6.png` | Archivo `logs/orden_total.log` y dos pares de eventos causalmente relacionados. |

No agregues capturas simuladas ni resultados que no hayas observado.

## 9. Compilación del informe LaTeX

Desde la carpeta `docs/`, ejecuta dos veces `pdflatex` para resolver referencias internas:

```powershell
cd docs
pdflatex -interaction=nonstopmode informe.tex
pdflatex -interaction=nonstopmode informe.tex
```

El archivo generado será `docs/informe.pdf`. El informe utiliza paquetes habituales de LaTeX; verifica la compilación sin conexión en el equipo donde realizarás la entrega.

## 10. Código reutilizado

Completa la tabla únicamente si reutilizaste código propio que ya estaba en un repositorio tuyo antes de la sesión. Si no reutilizaste código, conserva la frase indicada por el planteamiento.

| Archivo de este repositorio | Repositorio propio de origen (URL) | Archivo y commit de origen | Qué se cambió |
|---|---|---|---|
| [ruta o «No aplica»] | [URL o «No aplica»] | [archivo y hash o «No aplica»] | [descripción o «No aplica»] |

**Si no reutilizaste código, reemplaza la tabla por:** `No se reutilizó código`.

## 11. Limitaciones conocidas

[Describe limitaciones verificadas de tu prototipo, por ejemplo, el coordinador como punto único de fallo, ausencia de persistencia o comportamiento ante una desconexión. No incluyas limitaciones que no correspondan a tu implementación.]

## 12. Autoría y declaración

El código y los resultados documentados deben corresponder al trabajo realizado por el estudiante y cumplir las condiciones de la actividad. Declara aquí la reutilización de código propio, si existió, y verifica que todas las evidencias sean auténticas.
