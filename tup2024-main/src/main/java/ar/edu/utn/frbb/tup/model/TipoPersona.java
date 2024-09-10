package ar.edu.utn.frbb.tup.model;

public enum TipoPersona {
    FISICA("F"), JURIDICA("J");

    private String codigo;

    TipoPersona(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static TipoPersona fromCodigo(String codigo) {
        for (TipoPersona tipo : values()) {
            if (tipo.getCodigo().equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código no soportado: " + codigo);
    }
}

