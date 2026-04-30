package excepciones;

public class MSHException extends Exception {
    private final int linea;

    public MSHException(String mensaje, int linea) {
        super("[Línea " + linea + "] " + mensaje);
        this.linea = linea;
    }

    public int getLinea() { return linea; }

    public static class ErrorSintaxis extends MSHException {
        public ErrorSintaxis(String detalle, int linea) {
            super("Error: " + detalle, linea);
        }
    }

    public static class ErrorObi extends MSHException {
        public ErrorObi(String detalle, int linea) {
            super("Error en tipo obi (entero): " + detalle, linea);
        }
    }

    public static class ErrorAnaki extends MSHException {
        public ErrorAnaki(String detalle, int linea) {
            super("Error en tipo anaki (decimal): " + detalle, linea);
        }
    }

    public static class ErrorPadme extends MSHException {
        public ErrorPadme(String detalle, int linea) {
            super("Error en tipo padme (texto): " + detalle, linea);
        }
    }

    public static class ErrorOperacion extends MSHException {
        public ErrorOperacion(String detalle, int linea) {
            super("Error de operación: " + detalle, linea);
        }
    }

    public static class ErrorDivisionCero extends MSHException {
        public ErrorDivisionCero(int linea) {
            super("División entre cero no permitida", linea);
        }
    }

    public static class ErrorPalabraReservada extends MSHException {
        public ErrorPalabraReservada(String palabra, int linea) {
            super("'" + palabra + "' es una palabra reservada", linea);
        }
    }

    public static class ErrorVariableNoDeclarada extends MSHException {
        public ErrorVariableNoDeclarada(String nombre, int linea) {
            super("Variable '" + nombre + "' no declarada", linea);
        }
    }
}