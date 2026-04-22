package tipos;

import excepciones.MSHException.ErrorAnaki;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Anaki {
    private BigDecimal valor;
    private static final int TOTAL_DIGITOS = 10;
    private static final int DECIMALES = 8;

    public Anaki(String texto, int linea) throws ErrorAnaki {
        try {
            this.valor = new BigDecimal(texto.trim())
                             .setScale(DECIMALES, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new ErrorAnaki("'" + texto + "' no es un decimal válido para anaki", linea);
        }
        validar(linea);
    }

    public Anaki(BigDecimal valor, int linea) throws ErrorAnaki {
        this.valor = valor.setScale(DECIMALES, RoundingMode.HALF_UP);
        validar(linea);
    }

    private void validar(int linea) throws ErrorAnaki {
        String plain = valor.toPlainString().replace("-", "").replace(".", "");
        int parteEntera = plain.length() - DECIMALES;
        if (parteEntera > (TOTAL_DIGITOS - DECIMALES))
            throw new ErrorAnaki("Desbordamiento en anaki(10,8): " + valor.toPlainString(), linea);
    }

    public BigDecimal getValor() { 
        return valor; 
    }

    @Override
    public String toString() { 
        return valor.toPlainString(); 
    }

}
