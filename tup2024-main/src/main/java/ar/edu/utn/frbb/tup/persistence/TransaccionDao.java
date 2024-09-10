package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Transaccion;

import java.util.List;

public interface TransaccionDao {
    void agregarTransaccion(Transaccion transaccion);
    List<Transaccion> obtenerTransaccionesPorCuenta(long cuentaId);
}
