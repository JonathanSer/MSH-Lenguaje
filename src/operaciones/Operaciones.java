package operaciones;

import excepciones.MSHException.ErrorAnaki;
import excepciones.MSHException.ErrorDivisionCero;
import excepciones.MSHException.ErrorObi;
import excepciones.MSHException.ErrorPadme;
import java.math.BigDecimal;
import tipos.Anaki;
import tipos.Obi;
import tipos.Padme;

public class Operaciones {
    // OBI (entero)
    public static Obi sumarObi(Obi a, Obi b, int linea) throws ErrorObi {
        return new Obi(a.getValor() + b.getValor(), linea);
    }
    public static Obi restarObi(Obi a, Obi b, int linea) throws ErrorObi {
        return new Obi(a.getValor() - b.getValor(), linea);
    }
    public static Obi multiplicarObi(Obi a, Obi b, int linea) throws ErrorObi {
        return new Obi(a.getValor() * b.getValor(), linea);
    }
    public static Obi dividirObi(Obi a, Obi b, int linea)
            throws ErrorDivisionCero, ErrorObi {
        if (b.getValor() == 0) throw new ErrorDivisionCero(linea);
        return new Obi(a.getValor() / b.getValor(), linea);
    }
    
    // ANAKI (decimal)
    public static Anaki sumarAnaki(Anaki a, Anaki b, int linea) throws ErrorAnaki {
        return new Anaki(a.getValor().add(b.getValor()), linea);
    }
    public static Anaki restarAnaki(Anaki a, Anaki b, int linea) throws ErrorAnaki {
        return new Anaki(a.getValor().subtract(b.getValor()), linea);
    }
    public static Anaki multiplicarAnaki(Anaki a, Anaki b, int linea) throws ErrorAnaki {
        return new Anaki(a.getValor().multiply(b.getValor()), linea);
    }
    public static Anaki dividirAnaki(Anaki a, Anaki b, int linea)
            throws ErrorDivisionCero, ErrorAnaki {
        if (b.getValor().compareTo(BigDecimal.ZERO) == 0) throw new ErrorDivisionCero(linea);
        return new Anaki(a.getValor().divide(b.getValor(), 8, java.math.RoundingMode.HALF_UP), linea);
    }

    // PADME (string — solo concatenación)
    public static Padme concatenarPadme(Padme a, Padme b, int linea) throws ErrorPadme {
        return new Padme(a.getValor() + b.getValor(), linea);
    }
}
