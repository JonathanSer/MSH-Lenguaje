[README.md](https://github.com/user-attachments/files/27250720/README.md)
# Lenguaje MSH

Un intérprete de lenguaje de programación personalizado construido completamente en Java, con interfaz gráfica Swing. MSH define sus propios tipos de datos, palabras reservadas y manejo de errores con indicación de línea.

---

## Tabla de contenidos

- [Características](#-características)
- [Tipos de datos](#-tipos-de-datos)
- [Sintaxis](#-sintaxis)
- [Operaciones](#-operaciones)
- [Errores y excepciones](#-errores-y-excepciones)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Requisitos](#-requisitos)
- [Instalación y ejecución](#-instalación-y-ejecución)
- [Ejemplos](#-ejemplos)

---

## Características

- Tipos de datos propios: `obi`, `anaki` y `padme`
- Operaciones aritméticas y concatenación de cadenas
- Intérprete línea a línea con indicación exacta del número de línea en cada error
- Interfaz gráfica con editor de código, numeración de líneas y consola de salida
- Sistema de excepciones centralizado y tipado
- Comentarios de una línea con `//`

---

## Tipos de datos

| Palabra reservada | Equivalente | Precisión | Descripción |
|---|---|---|---|
| `obi` | `int` | 10 dígitos | Número entero |
| `anaki` | `double` | `(10, 8)` | Número decimal de alta precisión |
| `padme` | `String` | Ilimitada | Cadena de texto |

---

## Sintaxis

### Declaración de variables

```msh
obi   nombre $ valor;
anaki nombre $ valor;
padme nombre $ "texto";
```

### Reasignación

```msh
nombre $ nuevo_valor;
```

### Imprimir en consola

```msh
imprimir(expresion);
imprimir(variable);
```

### Comentarios

```msh
@ Esto es un comentario de una línea
```

---

## Operaciones

| Operador | Descripción | Tipos soportados |
|---|---|---|
| `+` | Suma | `obi`, `anaki` |
| `-` | Resta | `obi`, `anaki` |
| `*` | Multiplicación | `obi`, `anaki` |
| `/` | División | `obi`, `anaki` |
| `&` | Concatenación | `padme` |

> Las operaciones son binarias: `A operador B`. No se mezclan tipos distintos.

---

## Errores y excepciones

Todos los errores incluyen el número de línea donde ocurrieron:

```
⚠ [Línea 3] Error de sintaxis: Se esperaba ';' al final
⚠ [Línea 7] División entre cero no permitida
⚠ [Línea 2] 'obi' es una palabra reservada
⚠ [Línea 5] Variable 'total' no declarada
⚠ [Línea 4] Error en tipo obi (entero): Valor fuera del rango obi(10)
```

### Excepciones disponibles

Todas heredan de `MSHException` y están definidas como clases anidadas estáticas dentro de ella:

| Excepción | Causa |
|---|---|
| `ErrorSintaxis` | Código mal formado, falta de `;`, tokens inesperados |
| `ErrorTipoObi` | Valor no entero o fuera del rango de 10 dígitos |
| `ErrorTipoAnaki` | Valor no decimal o desbordamiento en `(10,8)` |
| `ErrorTipoPadme` | Valor nulo en tipo texto |
| `ErrorOperacion` | Operador inválido para el tipo o tipos incompatibles |
| `ErrorDivisionCero` | División entre cero en `obi` o `anaki` |
| `ErrorPalabraReservada` | Uso de `obi`, `anaki`, `padme` o `mostrar` como nombre de variable |
| `ErrorVariableNoDeclarada` | Uso de una variable antes de declararla |

---

## Estructura del proyecto

```
LenguajeMSH/
│
├── src/
│   └── lenguajesw/
│       │
│       ├── excepciones/
│       │   └── MSHException.java               # Clase base + todas las excepciones anidadas
│       │
│       ├── tipos/
│       │   ├── TipoObi.java                   # Tipo entero (obi)
│       │   ├── TipoAnaki.java                 # Tipo decimal (anaki)
│       │   └── TipoPadme.java                 # Tipo texto (padme)
│       │
│       ├── operaciones/
│       │   └── Operaciones.java               # Suma, resta, multiplicación, división, concat
│       │
│       ├── interprete/
│       │   ├── Token.java                     # Unidad mínima del lenguaje
│       │   ├── Lexer.java                     # Análisis léxico (tokenización)
│       │   └── Interprete.java                # Análisis sintáctico + ejecución
│       │
│       └── ui/
│           └── Interfaz.java                  # Interfaz gráfica Swing
|           ├── Main.java                      # Punto de entrada
```

### Flujo de ejecución

```
Código fuente (texto)
        │
        ▼
     Lexer          →  convierte el texto en una lista de Tokens
        │
        ▼
   Interprete       →  lee los tokens, valida la sintaxis y ejecuta
        │
   ┌────┴────┐
   ▼         ▼
Tipos     Operaciones   →  TipoObi, TipoAnaki, TipoPadme + cálculos
        │
        ▼
   Resultado / Error con número de línea
```

---

## Requisitos

- Java 17 o superior (se usan `switch` expressions y pattern matching)
- Apache NetBeans 17+ (recomendado) o cualquier IDE compatible con Java
- No requiere dependencias externas ni Maven/Gradle

---

## Instalación y ejecución

### Con NetBeans

1. Clona el repositorio:
   ```bash
   https://github.com/JonathanSer/MSH-Lenguaje.git
   ```
2. Abre NetBeans → **File → Open Project** → selecciona la carpeta `LenguajeMSH`
3. Clic derecho sobre el proyecto → **Clean and Build**
4. Presiona **F6** o el botón **Run** para ejecutar

### Con la terminal

```bash
# Compilar
javac -d out -sourcepath src src/lenguajemsh/Main.java

# Ejecutar
java -cp out lenguajesw.Main
```

---

## Ejemplos

### Operaciones con enteros (`obi`)

```sw
obi cantidad $ 5;
obi precio   $ 20;
obi total    $ cantidad * precio;
imprimir(total);
// Salida: 100
```

### Operaciones con decimales (`anaki`)

```sw
anaki subtotal $ 20.00000000;
anaki iva      $ 3.20000000;
anaki final    $ subtotal + iva;
imprimir(final);
// Salida: 23.20000000
```

### Concatenación de texto (`padme`)

```sw
padme nombre  $ "Luke";
padme saludo  $ "Hola, " & nombre;
imprimir(saludo);
// Salida: Hola, Luke
```

### Manejo de errores

```msh
// Error: palabra reservada como nombre de variable
obi obi $ 10;
// ⚠ [Línea 2] 'obi' es una palabra reservada

// Error: división entre cero
obi cero      $ 0;
obi resultado $ 10 / cero;
// ⚠ [Línea 3] División entre cero no permitida

// Error: variable no declarada
mostrar fantasma;
// ⚠ [Línea 1] Variable 'fantasma' no declarada
```

---

## Interfaz gráfica

La interfaz cuenta con:

- **Editor de código** con numeración de líneas en el margen izquierdo
- **Botón Ejecutar** — corre el código y muestra el resultado
- **Consola de salida** — fondo oscuro con texto verde, muestra resultados y errores
- **Botón Limpiar consola** — limpia la salida anterior
- **Botón Cargar ejemplo** — inserta un programa de ejemplo para probar el lenguaje.
