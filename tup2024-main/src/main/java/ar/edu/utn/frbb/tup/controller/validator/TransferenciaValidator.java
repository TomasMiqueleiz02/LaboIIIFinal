package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.DatoIngresadoInvalidoException;
import org.springframework.stereotype.Component;

@Component
public class TransferenciaValidator {

    public void validate(TransferenciaDto transferenciaDto) throws DatoIngresadoInvalidoException {
        validateCuentaOrigen(transferenciaDto);
        validateCuentaDestino(transferenciaDto);
        validateMonto(transferenciaDto);
        validateMoneda(transferenciaDto);
    }

    private void validateCuentaOrigen(TransferenciaDto transferenciaDto) throws DatoIngresadoInvalidoException {
        if (transferenciaDto.getCuentaOrigen() <= 0) {
            throw new DatoIngresadoInvalidoException("La cuenta de origen debe ser un número positivo");
        }
    }

    private void validateCuentaDestino(TransferenciaDto transferenciaDto) throws DatoIngresadoInvalidoException {
        if (transferenciaDto.getCuentaDestino() <= 0) {
            throw new DatoIngresadoInvalidoException("La cuenta de destino debe ser un número positivo");
        }
    }

    private void validateMonto(TransferenciaDto transferenciaDto) throws DatoIngresadoInvalidoException {
        if (transferenciaDto.getMonto() <= 0) {
            throw new DatoIngresadoInvalidoException("El monto debe ser un número positivo");
        }
    }

    private void validateMoneda(TransferenciaDto transferenciaDto) throws DatoIngresadoInvalidoException {
        try {
            TipoMoneda.valueOf(transferenciaDto.getMoneda());
        } catch (IllegalArgumentException e) {
            throw new DatoIngresadoInvalidoException("Tipo de moneda no válido");
        }
    }
}
