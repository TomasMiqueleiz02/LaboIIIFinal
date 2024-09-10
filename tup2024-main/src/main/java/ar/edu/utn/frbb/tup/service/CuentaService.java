package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;

import java.util.List;

public interface CuentaService {
    CuentaDto darDeAltaCuenta(CuentaDto cuentaDto)throws CuentaAlreadyExistsException;
    CuentaDto buscarCuentaPorNumero(long numeroCuenta);
    List<CuentaDto> obtenerTodasLasCuentas();
    void eliminarCuenta(long numeroCuenta);

}


