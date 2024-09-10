package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.DatoIngresadoInvalidoException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CuentaValidator {

    private final CuentaDao cuentaDao;
    @Autowired
    public CuentaValidator(CuentaDao cuentaDao) {
        this.cuentaDao = cuentaDao;
    }
    public void validate(CuentaDto cuentaDto) throws DatoIngresadoInvalidoException {
        validateDniTitular(cuentaDto);
        validateSaldo(cuentaDto);
        validateTipoMoneda(cuentaDto);
        validateNumeroCuenta(cuentaDto);
        validateTipoCuenta(cuentaDto);

    }

    private void validateDniTitular(CuentaDto cuentaDto) throws DatoIngresadoInvalidoException {
        if (cuentaDto.getdniTitular() <= 0) {
            throw new DatoIngresadoInvalidoException("El número de DNI debe ser un número mayor que cero");
        }
        Cuenta cuentaExistente = cuentaDao.findByDni(cuentaDto.getdniTitular());
        if (cuentaExistente != null) {
            throw new DatoIngresadoInvalidoException("Ya existe una cuenta con el mismo DNI");
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
        try {
            TipoMoneda.valueOf(cuentaDto.getMoneda());
        } catch (IllegalArgumentException e) {
            throw new DatoIngresadoInvalidoException("El tipo de moneda no es válido");
        }
    }

    private void validateNumeroCuenta(CuentaDto cuentaDto) throws DatoIngresadoInvalidoException {
        if (cuentaDto.getNumeroCuenta() == null || cuentaDto.getNumeroCuenta().toString().trim().isEmpty()) {
            throw new DatoIngresadoInvalidoException("El número de cuenta es obligatorio");
        }
        // Aquí podrías agregar lógica para verificar el formato del número de cuenta
    }

    private void validateTipoCuenta(CuentaDto cuentaDto) throws DatoIngresadoInvalidoException {
        // Suponiendo que tienes un tipo de cuenta en cuentaDto
        if (cuentaDto.getTipoCuenta() == null || cuentaDto.getTipoCuenta().isEmpty()) {
            throw new DatoIngresadoInvalidoException("El tipo de cuenta es obligatorio");
        }
        // Aquí podrías agregar lógica para verificar que el tipo de cuenta sea válido
    }


}
