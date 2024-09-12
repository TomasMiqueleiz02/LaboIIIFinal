package ar.edu.utn.frbb.tup.persistence.impl;

import java.util.*;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.stereotype.Service;

@Repository
public class CuentaDaoImpl implements CuentaDao {

    private final Map<Long, Cuenta> cuentas = new HashMap<>();

    @Override
    public Cuenta saveCuenta(Cuenta cuenta) {
        cuentas.put(cuenta.getNumeroCuenta(), cuenta);
        return cuenta;
    }

    @Override
    public Cuenta findCuenta(long numeroCuenta) {
        // Devuelve la cuenta correspondiente al número de cuenta, si existe
        return cuentas.get(numeroCuenta);
    }

    @Override
    public List<Cuenta> findAllCuentas() {
        // Devuelve una lista de todas las cuentas almacenadas
        return new ArrayList<>(cuentas.values());
    }
    @Override
    public Cuenta findByDni(long dni) {
        return cuentas.values().stream()
                .filter(cuenta -> cuenta.getdniTitular() == dni)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateCuenta(Cuenta cuenta) {
        // Actualiza la cuenta en el mapa si existe
        if (cuentas.containsKey(cuenta.getNumeroCuenta())) {
            cuentas.put(cuenta.getNumeroCuenta(), cuenta);
        } else {
            throw new IllegalArgumentException("La cuenta no existe");
        }
    }

    @Override
    public void deleteCuenta(long numeroCuenta) {
        // Elimina la cuenta del mapa si existe
        cuentas.remove(numeroCuenta);
    }
}
