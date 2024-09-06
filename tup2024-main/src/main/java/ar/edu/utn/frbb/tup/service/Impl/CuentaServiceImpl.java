package ar.edu.utn.frbb.tup.service.Impl;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaDao cuentaDao;

    @Autowired
    public CuentaServiceImpl(CuentaDao cuentaDao) {
        this.cuentaDao = cuentaDao;
    }

    @Override
    public Cuenta darDeAltaCuenta(Cuenta cuenta) {
        return cuentaDao.saveCuenta(cuenta);
    }

    @Override
    public boolean tipoCuentaEstaSoportada(TipoCuenta tipoCuenta) {
        // Implementar la lógica para verificar si el tipo de cuenta está soportado
        return tipoCuenta == TipoCuenta.CAJA_AHORRO || tipoCuenta == TipoCuenta.CUENTA_CORRIENTE;
    }

    @Override
    public Cuenta findByID(long numeroCuenta) throws CuentaNoEncontradaException {
        Cuenta cuenta = cuentaDao.findCuenta(numeroCuenta);
        if (cuenta == null) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada");
        }
        return cuenta;
    }

    @Override
    public List<Cuenta> findAll() {
        return cuentaDao.findAllCuentas();
    }

    @Override
    public void eliminarCuenta(long numeroCuenta) throws CuentaNoEncontradaException {
        Cuenta cuenta = findByID(numeroCuenta);
        cuentaDao.deleteCuenta(numeroCuenta);
    }
}
