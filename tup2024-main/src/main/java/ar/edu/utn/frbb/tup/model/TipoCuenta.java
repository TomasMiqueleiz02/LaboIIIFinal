package ar.edu.utn.frbb.tup.model;

public enum TipoCuenta {
    CUENTA_CORRIENTE, CAJA_AHORRO;

    public static TipoCuenta fromString(String tipo) {
        for (TipoCuenta tc : TipoCuenta.values()) {
            if (tc.name().equalsIgnoreCase(tipo)) {
                return tc;
            }
        }
        throw new IllegalArgumentException("Tipo de cuenta no soportado: " + tipo);
    }
}


