package ar.edu.utn.frbb.tup.service.validator;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.DatoIngresadoInvalidoException;
import org.springframework.stereotype.Component;

@Component
public class CuentaValidator {

    public void validate(CuentaDto cuentaDto) throws DatoIngresadoInvalidoException {
        validateDniTitular(cuentaDto);
        validateSaldo(cuentaDto);
        validateTipoMoneda(cuentaDto);
    }

    private void validateDniTitular(CuentaDto cuentaDto) throws DatoIngresadoInvalidoException {
        if (cuentaDto.getDniTitular() <0) {
            throw new DatoIngresadoInvalidoException("El número de dni debe ser un número mayor que cero");
        }
    }

    private void validateSaldo(CuentaDto cuentaDto) throws DatoIngresadoInvalidoException {
        if (cuentaDto.getBalance() < 0) {
            throw new DatoIngresadoInvalidoException("El saldo no puede ser negativo");
        }
    }

    private void validateTipoMoneda(CuentaDto cuentaDto) throws DatoIngresadoInvalidoException {
        if (cuentaDto.getMoneda() == null || cuentaDto.getMoneda().isEmpty()) {
            throw new DatoIngresadoInvalidoException("El tipo de moneda es obligatorio");
        }
    }

}
