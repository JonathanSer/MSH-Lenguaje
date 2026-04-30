package tipos;

import excepciones.MSHException.ErrorAnaki;
import java.math.BigDecimal;

public class Anaki {
    private BigDecimal valor;
    private static final int TOTAL_DIGITOS = 10;
    private static final int DECIMALES = 8;

    public Anaki(String texto, int linea) throws ErrorAnaki {
        try {
            String limpio = texto.trim();

            if (limpio.endsWith(".") || limpio.startsWith(".")) {
                throw new ErrorAnaki("'" + texto + "' no es un decimal válido para anaki", linea);
            }

            if (!limpio.matches("-?\\d+(\\.\\d+)?")) {
                throw new ErrorAnaki("'" + texto + "' no es un decimal válido para anaki", linea);
            }

            BigDecimal temp = new BigDecimal(limpio);

            if (temp.scale() > DECIMALES) {
                throw new ErrorAnaki(
                    "anaki solo permite hasta " + DECIMALES + " decimales: " + texto,
                    linea
                );
            }

            this.valor = temp.stripTrailingZeros();

        } catch (NumberFormatException e) {
            throw new ErrorAnaki("'" + texto + "' no es un decimal válido para anaki", linea);
        }

        validar(linea);
    }

    public Anaki(BigDecimal valor, int linea) throws ErrorAnaki {
        if (valor.scale() > DECIMALES) {
            throw new ErrorAnaki(
                "anaki solo permite hasta " + DECIMALES + " decimales",
                linea
            );
        }

        this.valor = valor.stripTrailingZeros();
        validar(linea);
    }

    private void validar(int linea) throws ErrorAnaki {
        int escala = valor.scale() < 0 ? 0 : valor.scale();

        // Validar decimales (máx 8)
        if (escala > DECIMALES) {
            throw new ErrorAnaki(
                "anaki solo permite hasta " + DECIMALES + " decimales: " + valor.toPlainString(),
                linea
            );
        }

        // Validar parte entera (máx 10)
        int enteros = valor.abs().toBigInteger().toString().length();

        if (enteros > TOTAL_DIGITOS) {
            throw new ErrorAnaki(
                "anaki solo permite hasta " + TOTAL_DIGITOS + " dígitos enteros: " + valor.toPlainString(),
                linea
            );
        }
    }

    public BigDecimal getValor() { 
        return valor; 
    }

    @Override
    public String toString() { 
        return valor.toPlainString(); 
    }
}