package tipos;

import excepciones.MSHException.ErrorPadme;

public class Padme {
    private String valor;

    public Padme(String valor, int linea) throws ErrorPadme {
        if (valor == null)
            throw new ErrorPadme("El valor padme no puede ser nulo", linea);
        // Quitar comillas si vienen del lexer
        this.valor = valor.startsWith("\"") && valor.endsWith("\"")
                     ? valor.substring(1, valor.length() - 1)
                     : valor;
    }

    public String getValor() { return valor; }

    @Override
    public String toString() { return valor; }
}
