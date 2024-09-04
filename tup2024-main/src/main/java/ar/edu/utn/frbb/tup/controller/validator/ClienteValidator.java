package ar.edu.utn.frbb.tup.service.validator;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.exception.DatoIngresadoInvalidoException;
import org.springframework.stereotype.Component;

@Component
public class ClienteValidator {

    public void validate(ClienteDto clienteDto) throws DatoIngresadoInvalidoException {
        validateNombre(clienteDto);
        validateApellido(clienteDto);
        validateDNI(clienteDto);
        validateFechaNacimiento(clienteDto);
    }

    private void validateNombre(ClienteDto clienteDto) throws DatoIngresadoInvalidoException {
        if (clienteDto.getNombre() == null || clienteDto.getNombre().trim().isEmpty()) {
            throw new DatoIngresadoInvalidoException("El nombre es obligatorio");
        }
    }

    private void validateApellido(ClienteDto clienteDto) throws DatoIngresadoInvalidoException {
        if (clienteDto.getApellido() == null || clienteDto.getApellido().trim().isEmpty()) {
            throw new DatoIngresadoInvalidoException("El apellido es obligatorio");
        }
    }

    private void validateDNI(ClienteDto clienteDto) throws DatoIngresadoInvalidoException {
        if (clienteDto.getDni() <= 0) {
            throw new DatoIngresadoInvalidoException("El DNI es obligatorio y debe ser un número positivo");
        }
    }

    private void validateFechaNacimiento(ClienteDto clienteDto) throws DatoIngresadoInvalidoException {
        if (clienteDto.getFechaNacimiento() == null) {
            throw new DatoIngresadoInvalidoException("La fecha de nacimiento es obligatoria");
        }
    }
}

