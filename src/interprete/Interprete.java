package interprete;

import excepciones.*;
import excepciones.MSHException.ErrorAnaki;
import excepciones.MSHException.ErrorObi;
import excepciones.MSHException.ErrorOperacion;
import excepciones.MSHException.ErrorPadme;
import excepciones.MSHException.ErrorSintaxis;
import excepciones.MSHException.ErrorVariableNoDeclarada;
import operaciones.Operaciones;
import tipos.*;
import java.util.*;

public class Interprete {

    private final Map<String, Object> variables    = new HashMap<>();
    private final Map<String, String> tiposVar     = new HashMap<>();
    private List<Token> ultimosTokens              = new ArrayList<>();
    private int         tokenHastaError            = -1;

    public List<Token> getTokens()          { return ultimosTokens; }
    public int         getTokenHastaError() { return tokenHastaError; }

    public String ejecutar(String codigo) {
        StringBuilder salida = new StringBuilder();
        tokenHastaError = -1;
        try {
            Lexer lexer = new Lexer(codigo);
            List<Token> tokens = lexer.tokenizar();
            ultimosTokens = tokens;
            int i = 0;
            while (tokens.get(i).tipo != Token.Tipo.EOF) {
                i = procesarInstruccion(tokens, i, salida);
                tokenHastaError = i - 1;
            }
        } catch (MSHException e) {
            salida.append("\n⚠ ").append(e.getMessage());
        }
        return salida.toString();
    }

    private int procesarInstruccion(List<Token> tokens, int i, StringBuilder out) throws MSHException {
        Token t = tokens.get(i);

        // ───── DECLARACIÓN ─────
        if (t.tipo == Token.Tipo.OBI || t.tipo == Token.Tipo.ANAKI || t.tipo == Token.Tipo.PADME) {
            String tipoPalabra = t.valor;

            Token nombre = tokens.get(i + 1);
            if (nombre.tipo != Token.Tipo.IDENTIFICADOR)
                throw new ErrorSintaxis("Se esperaba un nombre de variable", t.linea);

            if (tokens.get(i + 2).tipo != Token.Tipo.ASIGNAR)
                throw new ErrorSintaxis("Se esperaba '$' tras el nombre", t.linea);

            if (tokens.get(i + 3).tipo == Token.Tipo.PUNTO_COMA)
                throw new ErrorSintaxis("Se esperaba un valor después de '$'", t.linea);

            Object valor = evaluarExpresion(tokens, i + 3, t.linea, tipoPalabra);

            validarTipo(tipoPalabra, valor, t.linea);

            int fin = saltarExpresion(tokens, i + 3);
            if (tokens.get(fin).tipo != Token.Tipo.PUNTO_COMA)
                throw new ErrorSintaxis("Se esperaba ';' al final", t.linea);

            declararVariable(nombre.valor, valor, t.linea);

            variables.put(nombre.valor, valor);
            tiposVar.put(nombre.valor, tipoPalabra);
            return fin + 1;
        }

        // ───── IMPRIMIR ─────
        if (t.tipo == Token.Tipo.IMPRIMIR) {

            if (tokens.get(i + 1).tipo != Token.Tipo.LPAREN)
                throw new ErrorSintaxis("Se esperaba '(' después de imprimir", t.linea);

            Object valor = evaluarExpresion(tokens, i + 2, t.linea, null);

            int fin = i + 2;
            while (fin < tokens.size() && tokens.get(fin).tipo != Token.Tipo.RPAREN) {
                fin++;
            }

            if (fin >= tokens.size())
                throw new ErrorSintaxis("Se esperaba ')'", t.linea);

            if (tokens.get(fin + 1).tipo != Token.Tipo.PUNTO_COMA)
                throw new ErrorSintaxis("Se esperaba ';' al final", t.linea);

            out.append(valor).append("\n");
            return fin + 2;
        }

        // ───── REASIGNACIÓN ─────
        if (t.tipo == Token.Tipo.IDENTIFICADOR && tokens.get(i + 1).tipo == Token.Tipo.ASIGNAR) {

            if (!variables.containsKey(t.valor))
                throw new ErrorVariableNoDeclarada(t.valor, t.linea);

            String tipo = tiposVar.get(t.valor);
            Object valor = evaluarExpresion(tokens, i + 2, t.linea, tipo);

            validarTipo(tipo, valor, t.linea);

            int fin = saltarExpresion(tokens, i + 2);
            if (tokens.get(fin).tipo != Token.Tipo.PUNTO_COMA)
                throw new ErrorSintaxis("Se esperaba ';'", t.linea);

            variables.put(t.valor, valor);
            return fin + 1;
        }

        // ───── COMENTARIO ─────
        if (t.tipo == Token.Tipo.COMENTARIO) {
            int siguiente = i + 1;
            // si el siguiente token es el texto del comentario, saltarlo también
            if (siguiente < tokens.size() && tokens.get(siguiente).tipo == Token.Tipo.TEXTO_COMENTARIO)
                return siguiente + 1;
            return siguiente;
        }
        
        throw new ErrorSintaxis("Instrucción no reconocida: '" + t.valor + "'", t.linea);
    }

    public void declararVariable(String nombre, Object valor, int linea) throws MSHException {
        Object existente = variables.putIfAbsent(nombre, valor);
        if (existente != null)
            throw new ErrorSintaxis("La variable '" + nombre + "' ya fue declarada", linea);
    }

    private Object evaluarExpresion(List<Token> tokens, int i, int linea, String tipo) throws MSHException {
        Object resultado = evaluarAtomo(tokens, i, linea, tipo);
        int pos = i + 1;

        while (pos < tokens.size()) {
            Token op = tokens.get(pos);

            if (op.tipo != Token.Tipo.SUMA  &&
                op.tipo != Token.Tipo.RESTA  &&
                op.tipo != Token.Tipo.MULT   &&
                op.tipo != Token.Tipo.DIV    &&
                op.tipo != Token.Tipo.CONCAT) {
                break;
            }

            if (pos + 1 >= tokens.size() ||
                tokens.get(pos + 1).tipo == Token.Tipo.PUNTO_COMA ||
                tokens.get(pos + 1).tipo == Token.Tipo.RPAREN) {
                throw new ErrorSintaxis(
                    "Se esperaba un valor después del operador '" + op.valor + "'", linea);
            }

            Object der = evaluarAtomo(tokens, pos + 1, linea, tipo);
            resultado = aplicarOp(resultado, op, der, linea);
            pos += 2;
        }

        return resultado;
    }

    private Object evaluarAtomo(List<Token> tokens, int i, int linea, String tipo) throws MSHException {
        Token t = tokens.get(i);

        return switch (t.tipo) {
            case NUMERO_ENTERO  -> new Obi(t.valor, linea);
            case NUMERO_DECIMAL -> new Anaki(t.valor, linea);
            case CADENA         -> new Padme(t.valor, linea);

            case IDENTIFICADOR -> {
                if (!variables.containsKey(t.valor)) {
                    if (tipo != null) {
                        switch (tipo) {
                            case "obi"   -> throw new ErrorObi(
                                    "Se esperaba un entero pero se encontró '" + t.valor + "'", linea);
                            case "anaki" -> throw new ErrorAnaki(
                                    "Se esperaba un decimal pero se encontró '" + t.valor + "'", linea);
                            case "padme" -> throw new ErrorPadme(
                                    "Se esperaba texto pero se encontró '" + t.valor + "'", linea);
                        }
                    }
                    throw new ErrorVariableNoDeclarada(t.valor, linea);
                }
                yield variables.get(t.valor);
            }

            default -> throw new ErrorSintaxis("Expresión inesperada: '" + t.valor + "'", linea);
        };
    }

    private Object aplicarOp(Object izq, Token op, Object der, int linea) throws MSHException {

        if (izq instanceof Obi a && der instanceof Obi b) {
            return switch (op.tipo) {
                case SUMA  -> Operaciones.sumarObi(a, b, linea);
                case RESTA -> Operaciones.restarObi(a, b, linea);
                case MULT  -> Operaciones.multiplicarObi(a, b, linea);
                case DIV   -> Operaciones.dividirObi(a, b, linea);
                default    -> throw new ErrorOperacion(
                        "Operación '" + op.valor + "' no válida para obi", linea);
            };
        }

        if (izq instanceof Anaki a && der instanceof Anaki b) {
            return switch (op.tipo) {
                case SUMA  -> Operaciones.sumarAnaki(a, b, linea);
                case RESTA -> Operaciones.restarAnaki(a, b, linea);
                case MULT  -> Operaciones.multiplicarAnaki(a, b, linea);
                case DIV   -> Operaciones.dividirAnaki(a, b, linea);
                default    -> throw new ErrorOperacion(
                        "Operación '" + op.valor + "' no válida para anaki", linea);
            };
        }

        if (izq instanceof Padme a && der instanceof Padme b) {
            if (op.tipo == Token.Tipo.CONCAT)
                return Operaciones.concatenarPadme(a, b, linea);
            throw new ErrorOperacion("padme solo admite '%'", linea);
        }

        throw new ErrorOperacion("Tipos incompatibles en la operación", linea);
    }

    private void validarTipo(String tipo, Object valor, int linea) throws MSHException {
        switch (tipo) {
            case "obi" -> {
                if (!(valor instanceof Obi))
                    throw new ErrorObi("obi solo acepta enteros", linea);
            }
            case "anaki" -> {
                if (!(valor instanceof Anaki))
                    throw new ErrorAnaki("anaki solo acepta decimales", linea);
            }
            case "padme" -> {
                if (!(valor instanceof Padme))
                    throw new ErrorPadme("padme solo acepta strings", linea);
            }
        }
    }

    private int saltarExpresion(List<Token> tokens, int i) {
        while (i < tokens.size()
                && tokens.get(i).tipo != Token.Tipo.PUNTO_COMA
                && tokens.get(i).tipo != Token.Tipo.EOF) {
            i++;
        }
        return i;
    }
}