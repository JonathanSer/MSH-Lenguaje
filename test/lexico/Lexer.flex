package lexico;
import java_cup.runtime.*;

%%

%class Lexer
%unicode
%cup
%line
%column

%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

/* Espacios en blanco */
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

/* Literales */
EnteroLiteral  = 0 | [1-9][0-9]*
DoubleLiteral  = {EnteroLiteral} \. [0-9]+
StringLiteral  = \" [^\"\n]* \"
Identificador  = [a-zA-Z_][a-zA-Z0-9_]*

%%

/* Palabras reservadas - tipos */
"abi"       { return symbol(sym.T_ABI); }
"anaki"     { return symbol(sym.T_ANAKI); }
"padme"     { return symbol(sym.T_PADME); }

/* Palabras reservadas - control */
"imprimir"  { return symbol(sym.T_IMPRIMIR); }

/* Operadores aritméticos */
"+"         { return symbol(sym.MAS); }
"-"         { return symbol(sym.MENOS); }
"*"         { return symbol(sym.POR); }
"/"         { return symbol(sym.DIV); }
"++"        { return symbol(sym.CONCAT); }

/* Operadores de asignación y puntuación */
"$"         { return symbol(sym.IGUAL); }
";"         { return symbol(sym.PCOMA); }
"("         { return symbol(sym.PARIZQ); }
")"         { return symbol(sym.PARDER); }

"@"[^\n]*   { /* comentario */ }

/* Literales */
{EnteroLiteral}  { return symbol(sym.ENTERO, Integer.parseInt(yytext())); }
{DoubleLiteral}  { return symbol(sym.DOUBLE, Double.parseDouble(yytext())); }
{StringLiteral}  { return symbol(sym.CADENA, yytext().substring(1, yytext().length()-1)); }
{Identificador}  { return symbol(sym.ID, yytext()); }

/* Ignorar espacios */
{WhiteSpace}     { /* ignorar */ }

/* Error */
[^]  { throw new Error("Caracter ilegal: " + yytext() + 
        " en línea " + (yyline+1) + ", columna " + (yycolumn+1)); }