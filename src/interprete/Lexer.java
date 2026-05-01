package interprete;

import excepciones.MSHException.ErrorSintaxis;
import java.util.*;

public class Lexer {

    private static final Set<String> PALABRAS_RESERVADAS =
        new HashSet<>(Arrays.asList("obi", "anaki", "padme", "imprimir"));

    private final String codigo;
    private int pos = 0;
    private int linea = 1;
    private List<Token> tokens = new ArrayList<>(); // ← nuevo campo

    public Lexer(String codigo) { this.codigo = codigo; }

    public List<Token> getTokens() { return tokens; } // ← nuevo getter

    public List<Token> tokenizar() throws ErrorSintaxis {
        while (pos < codigo.length()) {
            char c = codigo.charAt(pos);

            if (c == '\n') { linea++; pos++; continue; }
            if (Character.isWhitespace(c)) { pos++; continue; }

            /*
            if (c == '@') {
                while (pos < codigo.length() && codigo.charAt(pos) != '\n') pos++;
                continue;
            }
            */
            
            if (c == '@') {
                int lineaInicio = linea;
                pos++; // saltar el '@'
                tokens.add(new Token(Token.Tipo.COMENTARIO, "@", lineaInicio)); // token para '@'
                StringBuilder sb = new StringBuilder();
                while (pos < codigo.length() && codigo.charAt(pos) != '\n')
                    sb.append(codigo.charAt(pos++));
                String texto = sb.toString().trim();
                if (!texto.isEmpty())
                    tokens.add(new Token(Token.Tipo.TEXTO_COMENTARIO, texto, lineaInicio)); // token para el texto
                continue;
            }

            if (c == '"') { tokens.add(leerCadena()); continue; }
            if (Character.isDigit(c) || (c == '-' && pos+1 < codigo.length()
                    && Character.isDigit(codigo.charAt(pos+1)))) {
                tokens.add(leerNumero()); continue;
            }
            if (Character.isLetter(c) || c == '_') { tokens.add(leerPalabra()); continue; }

            switch (c) {
                case '$' -> tokens.add(new Token(Token.Tipo.ASIGNAR,     "$",  linea));
                case '+' -> tokens.add(new Token(Token.Tipo.SUMA,        "+",  linea));
                case '-' -> tokens.add(new Token(Token.Tipo.RESTA,       "-",  linea));
                case '*' -> tokens.add(new Token(Token.Tipo.MULT,        "*",  linea));
                case '/' -> tokens.add(new Token(Token.Tipo.DIV,         "/",  linea));
                case '%' -> tokens.add(new Token(Token.Tipo.CONCAT,      "%",  linea));
                case '(' -> tokens.add(new Token(Token.Tipo.LPAREN,      "(",  linea));
                case ')' -> tokens.add(new Token(Token.Tipo.RPAREN,      ")",  linea));
                case ';' -> tokens.add(new Token(Token.Tipo.PUNTO_COMA,  ";",  linea));
                default  -> throw new ErrorSintaxis("Carácter desconocido: '" + c + "'", linea);
            }
            pos++;
        }
        tokens.add(new Token(Token.Tipo.EOF, "", linea));
        return tokens;
    }

    private Token leerCadena() throws ErrorSintaxis {
        int lineaInicio = linea;
        StringBuilder sb = new StringBuilder("\"");
        pos++;
        while (pos < codigo.length() && codigo.charAt(pos) != '"') {
            if (codigo.charAt(pos) == '\n')
                throw new ErrorSintaxis("Cadena no cerrada", lineaInicio);
            sb.append(codigo.charAt(pos++));
        }
        if (pos >= codigo.length())
            throw new ErrorSintaxis("Cadena no cerrada", lineaInicio);
        sb.append('"');
        pos++;
        return new Token(Token.Tipo.CADENA, sb.toString(), lineaInicio);
    }

    private Token leerNumero() throws ErrorSintaxis {
        int lineaInicio = linea;
        StringBuilder sb = new StringBuilder();
        boolean esDecimal = false;

        if (codigo.charAt(pos) == '-') sb.append(codigo.charAt(pos++));

        while (pos < codigo.length() && (Character.isDigit(codigo.charAt(pos)) || codigo.charAt(pos) == '.')) {
            if (codigo.charAt(pos) == '.') esDecimal = true;
            sb.append(codigo.charAt(pos++));
        }

        if (pos < codigo.length() && Character.isLetter(codigo.charAt(pos))) {
            throw new ErrorSintaxis(
                "El formato no es el adecuado: '" + sb.toString() + codigo.charAt(pos) + "...'",
                lineaInicio
            );
        }

        return new Token(esDecimal ? Token.Tipo.NUMERO_DECIMAL : Token.Tipo.NUMERO_ENTERO,
                         sb.toString(), lineaInicio);
    }

    private Token leerPalabra() {
        int lineaInicio = linea;
        StringBuilder sb = new StringBuilder();
        while (pos < codigo.length() && (Character.isLetterOrDigit(codigo.charAt(pos)) || codigo.charAt(pos) == '_'))
            sb.append(codigo.charAt(pos++));
        String palabra = sb.toString();
        Token.Tipo tipo = switch (palabra) {
            case "obi"      -> Token.Tipo.OBI;
            case "anaki"    -> Token.Tipo.ANAKI;
            case "padme"    -> Token.Tipo.PADME;
            case "imprimir" -> Token.Tipo.IMPRIMIR;
            default         -> Token.Tipo.IDENTIFICADOR;
        };
        return new Token(tipo, palabra, lineaInicio);
    }
}