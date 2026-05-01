package codigo;

import lexico.Lexer;
import sintactico.Parser;
import java.io.StringReader;

public class Main {

    public static String ejecutar(String codigoFuente) {
        StringBuilder salida = new StringBuilder();
        try {
            // Redirigir System.out para capturar imprimir(...)
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(baos);
            java.io.PrintStream old = System.out;
            System.setOut(ps);

            // Limpiar variables entre ejecuciones
            Parser.variables.clear();
            Parser.tipos.clear();

            // Ejecutar
            StringReader reader = new StringReader(codigoFuente);
            Lexer lexer = new Lexer(reader);
            Parser parser = new Parser(lexer);
            parser.parse();

            System.out.flush();
            System.setOut(old);
            salida.append(baos.toString());

        } catch (Exception e) {
            salida.append("❌ Error: ").append(e.getMessage());
        }
        return salida.toString();
    }
}
