package ar.edu.utn.frbb.tup.controller.dto;

import ar.edu.utn.frbb.tup.model.TipoPersona;

public class ClienteDto extends PersonaDto {
    private TipoPersona tipoPersona;


    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

}
