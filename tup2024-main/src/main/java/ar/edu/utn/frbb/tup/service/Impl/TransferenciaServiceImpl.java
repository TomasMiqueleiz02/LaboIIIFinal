package ar.edu.utn.frbb.tup.service.Impl;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.Banco;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenYdestinoException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.TransferenciaDao;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferenciaServiceImpl implements TransferenciaService {

    private final TransferenciaDao transferenciaDao;
    private final CuentaDao cuentaDao;
    @Autowired
    private Banco banco;
    @Autowired
    private BanelcoService banelco;

    public TransferenciaServiceImpl(TransferenciaDao transferenciaDao, CuentaDao cuentaDao) {
        this.transferenciaDao = transferenciaDao;
        this.cuentaDao = cuentaDao;
    }

    @Override
    public Transferencia realizarTransferencia(TransferenciaDto transferenciaDto) throws CuentaNoEncontradaException,
            NoAlcanzaException, TipoMonedaException, CuentaOrigenYdestinoException {

        Transferencia transferencia = new Transferencia(transferenciaDto);

        long numeroCuentaOrigen = transferencia.getCuentaOrigen();
        long numeroCuentaDestino = transferencia.getCuentaDestino();
        double monto = transferencia.getMonto();
        TipoMoneda moneda = TipoMoneda.valueOf(transferenciaDto.getMoneda());

        Cuenta cuentaOrigen = cuentaDao.findCuenta(numeroCuentaOrigen);
        Cuenta cuentaDestino = cuentaDao.findCuenta(numeroCuentaDestino);

        validarCuentaOrigen(cuentaOrigen, monto, moneda);

        if (cuentaDestino == null) {
            manejarCuentaDestinoNoEncontrada(cuentaOrigen, numeroCuentaDestino, monto, transferencia);
        } else {
            validarTransferenciaInterna(cuentaOrigen, cuentaDestino,moneda);
            procesarTransferenciaInterna(cuentaOrigen, cuentaDestino, monto, transferencia);
        }

        cuentaDao.updateCuenta(cuentaOrigen);
        if (cuentaDestino != null) {
            cuentaDao.updateCuenta(cuentaDestino);
        }

        return transferencia;
    }

    private void validarCuentaOrigen(Cuenta cuentaOrigen, double monto, TipoMoneda moneda) throws CuentaNoEncontradaException, NoAlcanzaException, TipoMonedaException {
        if (cuentaOrigen == null) {
            throw new CuentaNoEncontradaException("Cuenta origen no encontrada");
        }

        if (cuentaOrigen.getBalance() < monto) {
            throw new NoAlcanzaException("Saldo insuficiente en la cuenta origen");
        }

        if (cuentaOrigen.getMoneda() != moneda) {
            throw new TipoMonedaException("El tipo de moneda de la cuenta origen no coincide");
        }
    }

    private void manejarCuentaDestinoNoEncontrada(Cuenta cuentaOrigen, long cuentaDestinoNumero, double monto, Transferencia transferencia) throws NoAlcanzaException, CuentaNoEncontradaException {
        if (banelco.cuentaExiste(cuentaDestinoNumero)) {
            procesarTransferenciaExterna(cuentaOrigen, cuentaDestinoNumero, monto, transferencia);
        } else {
            throw new CuentaNoEncontradaException("Cuenta destino no encontrada");
        }
    }

    private void validarTransferenciaInterna(Cuenta cuentaOrigen, Cuenta cuentaDestino, TipoMoneda moneda) throws CuentaOrigenYdestinoException, TipoMonedaException {
        if (cuentaOrigen.equals(cuentaDestino)) {
            throw new CuentaOrigenYdestinoException("La cuenta de origen y la cuenta de destino son iguales.");
        }

        if (cuentaDestino.getMoneda() != moneda) {
            throw new TipoMonedaException("El tipo de moneda de las cuentas no coincide");
        }
    }

    private void procesarTransferenciaInterna(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto, Transferencia transferencia) throws NoAlcanzaException {
        double comision = calcularComision(cuentaOrigen, monto);
        double montoFinal = monto - comision;

        cuentaOrigen.debitar(montoFinal);
        cuentaDestino.acreditar(montoFinal);
        transferenciaDao.guardarTransferencia(transferencia);
    }

    private void procesarTransferenciaExterna(Cuenta cuentaOrigen, long cuentaDestinoNumero, double monto, Transferencia transferencia)throws NoAlcanzaException {
        double comision = calcularComision(cuentaOrigen, monto);
        double montoFinal = monto - comision;

        cuentaOrigen.debitar(montoFinal);
        banelco.acreditar(montoFinal, cuentaDestinoNumero);
        transferenciaDao.guardarTransferencia(transferencia);
    }

    private double calcularComision(Cuenta cuenta, double monto) {
        double comision = 0;
        if (cuenta.getMoneda() == TipoMoneda.PESOS && monto >= 1000000) {
            comision = monto * 0.02;
            banco.guardarComisionEnPesos(comision);
        } else if (cuenta.getMoneda() == TipoMoneda.DOLARES && monto >= 5000) {
            comision = monto * 0.005;
            banco.guardarComisionEnDolares(comision);
        }
        return comision;
    }

    public List<Transferencia> find(long id) {
        return transferenciaDao.findTransfersByID(id);
    }

    public List<Transferencia> findAll() {
        return transferenciaDao.findAllTransfers();
    }
}
