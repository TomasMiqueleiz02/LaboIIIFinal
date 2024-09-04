package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoEncontradaException;

import java.util.List;

public interface CuentaService {

    Cuenta darDeAltaCuenta(Cuenta cuenta);

    boolean tipoCuentaEstaSoportada(TipoCuenta tipoCuenta);

    Cuenta findByID(long numeroCuenta) throws CuentaNoEncontradaException;

    List<Cuenta> findAll();

    void eliminarCuenta(long numeroCuenta) throws CuentaNoEncontradaException;
}

