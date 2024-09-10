package ar.edu.utn.frbb.tup.model;
public enum TipoMoneda {
    PESOS, DOLARES;

    public static TipoMoneda fromString(String moneda) {
        for (TipoMoneda tm : TipoMoneda.values()) {
            if (tm.name().equalsIgnoreCase(moneda)) {
                return tm;
            }
        }
        throw new IllegalArgumentException("Moneda no soportada: " + moneda);
    }
}
