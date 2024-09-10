package ar.edu.utn.frbb.tup.controller.validator;


import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.exception.DatoIngresadoInvalidoException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

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
        if (!clienteDto.getNombre().matches("[a-zA-ZÀ-ÿ\\s]+")) {
            throw new DatoIngresadoInvalidoException("El nombre contiene caracteres inválidos");
        }
    }

    private void validateApellido(ClienteDto clienteDto) throws DatoIngresadoInvalidoException {
        if (clienteDto.getApellido() == null || clienteDto.getApellido().trim().isEmpty()) {
            throw new DatoIngresadoInvalidoException("El apellido es obligatorio");
        }
        if (!clienteDto.getApellido().matches("[a-zA-ZÀ-ÿ\\s]+")) {
            throw new DatoIngresadoInvalidoException("El apellido contiene caracteres inválidos");
        }
    }

    private void validateDNI(ClienteDto clienteDto) throws DatoIngresadoInvalidoException {
        if (clienteDto.getDni() <= 0) {
            throw new DatoIngresadoInvalidoException("El DNI es obligatorio y debe ser un número positivo");
        }
        String dniStr = String.valueOf(clienteDto.getDni());
        if (dniStr.length() < 7 || dniStr.length() > 8) {
            throw new DatoIngresadoInvalidoException("El DNI debe tener entre 7 y 8 dígitos");
        }
    }

    private void validateFechaNacimiento(ClienteDto clienteDto) throws DatoIngresadoInvalidoException {
        if (clienteDto.getFechaNacimiento() == null) {
            throw new DatoIngresadoInvalidoException("La fecha de nacimiento es obligatoria");
        }
        if (clienteDto.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new DatoIngresadoInvalidoException("La fecha de nacimiento no puede ser en el futuro");
        }
        if (Period.between(clienteDto.getFechaNacimiento(), LocalDate.now()).getYears() < 18) {
            throw new DatoIngresadoInvalidoException("El cliente debe ser mayor de 18 años");
        }
    }
}


