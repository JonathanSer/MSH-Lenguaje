package codigo;

import java.io.*;

public class GeneradorCodigo {

    public static void main(String[] args) throws Exception {
        String base = System.getProperty("user.dir") + "/src/";

        // 1. Generar Lexer con JFlex
        String[] jflexArgs = {
            base + "lexico/Lexer.flex"
        };
        jflex.Main.main(jflexArgs);
        System.out.println("✓ Lexer generado");

        // 2. Generar Parser con JCup
        String[] cupArgs = {
            "-destdir", base + "sintactico/",
            "-parser", "Parser",
            "-symbols", "sym",
            base + "sintactico/Sintaxis.cup"
        };
        java_cup.Main.main(cupArgs);
        System.out.println("✓ Parser generado");
    }
}