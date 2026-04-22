package tipos;

import excepciones.MSHException.ErrorObi;

public class Obi {
    private long valor;
    private static final long MAX = 9_999_999_999L;
    private static final long MIN = -9_999_999_999L;

    public Obi(String texto, int linea) throws ErrorObi {
        try {
            this.valor = Long.parseLong(texto.trim());
        } catch (NumberFormatException e) {
            throw new ErrorObi("'" + texto + "' no es un entero válido para obi", linea);
        }
        if (this.valor > MAX || this.valor < MIN)
            throw new ErrorObi("Valor fuera del rango obi(10): " + texto, linea);
    }

    public Obi(long valor, int linea) throws ErrorObi {
        if (valor > MAX || valor < MIN)
            throw new ErrorObi("Valor fuera del rango obi(10): " + valor, linea);
        this.valor = valor;
    }

    public long getValor() { 
        return valor; 
    }

    @Override
    public String toString() { 
        return String.valueOf(valor); 
    }
    
}
