package excepciones;

public class MSHException extends Exception {

    public enum Categoria { LEXICO, SINTACTICO, SEMANTICO, LOGICO }

    private final Categoria categoria;

    public MSHException(String mensaje, Categoria categoria) {
        super(mensaje);
        this.categoria = categoria;
    }

    public Categoria getCategoria() { return categoria; }

    // ── Excepciones tipadas ──────────────────────────────────────────────

    public static class ErrorSintaxis extends MSHException {
        public ErrorSintaxis(String msg, int linea) {
            super("[Línea " + linea + "] " + msg, Categoria.SINTACTICO);
        }
    }

    public static class ErrorObi extends MSHException {
        public ErrorObi(String msg, int linea) {
            super("[Línea " + linea + "] " + msg, Categoria.SEMANTICO);
        }
    }

    public static class ErrorAnaki extends MSHException {
        public ErrorAnaki(String msg, int linea) {
            super("[Línea " + linea + "] " + msg, Categoria.SEMANTICO);
        }
    }

    public static class ErrorPadme extends MSHException {
        public ErrorPadme(String msg, int linea) {
            super("[Línea " + linea + "] " + msg, Categoria.SEMANTICO);
        }
    }

    public static class ErrorOperacion extends MSHException {
        public ErrorOperacion(String msg, int linea) {
            super("[Línea " + linea + "] " + msg, Categoria.SEMANTICO);
        }
    }

    public static class ErrorVariableNoDeclarada extends MSHException {
        public ErrorVariableNoDeclarada(String nombre, int linea) {
            super("[Línea " + linea + "] Variable '" + nombre + "' no declarada",
                  Categoria.SEMANTICO);
        }
    }

    public static class ErrorDivisionCero extends MSHException {
        public ErrorDivisionCero(int linea) {
            super("[Línea " + linea + "] División entre cero no permitida",
                  Categoria.LOGICO);
        }
    }

    public static class ErrorLexico extends MSHException {
        public ErrorLexico(String msg, int linea) {
            super("[Línea " + linea + "] " + msg, Categoria.LEXICO);
        }
    }
}