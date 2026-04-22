package interprete;

public class Token {
    public enum Tipo {
        OBI, ANAKI, PADME,      // palabras reservadas de tipo
        IMPRIMIR,                 // instrucción de salida
        IDENTIFICADOR,
        NUMERO_ENTERO, NUMERO_DECIMAL, CADENA,
        ASIGNAR,                 // =
        SUMA, RESTA, MULT, DIV, CONCAT,  // + - * / &
        LPAREN, RPAREN, PUNTO_COMA,
        EOF, DESCONOCIDO
    }

    public final Tipo tipo;
    public final String valor;
    public final int linea;

    public Token(Tipo tipo, String valor, int linea) {
        this.tipo  = tipo;
        this.valor = valor;
        this.linea = linea;
    }

    @Override
    public String toString() {
        return tipo + "(" + valor + ")@L" + linea;
    }
}
