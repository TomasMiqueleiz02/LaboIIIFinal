package ar.edu.utn.frbb.tup.service.Impl;

import ar.edu.utn.frbb.tup.controller.dto.TransaccionDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenYdestinoException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.TransaccionDao;
import ar.edu.utn.frbb.tup.persistence.TransferenciaDao;
import ar.edu.utn.frbb.tup.service.BanelcoService;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferenciaServiceImpl implements TransferenciaService {

    private final TransferenciaDao transferenciaDao;
    private final TransaccionDao transaccionDao;
    private final CuentaDao cuentaDao;
    private TipoMoneda tipoMoneda;
    @Autowired
    private Banco banco;
    @Autowired
    private final BanelcoService banelco;

    public TransferenciaServiceImpl(TransferenciaDao transferenciaDao, CuentaDao cuentaDao,BanelcoService banelco,TransaccionDao transaccionDao) {
        this.transferenciaDao = transferenciaDao;
        this.cuentaDao = cuentaDao;
        this.banelco=banelco;
        this.transaccionDao = transaccionDao;
    }

    @Override
    public Transferencia realizarTransferencia(TransferenciaDto transferenciaDto) throws CuentaNoEncontradaException,
            NoAlcanzaException, TipoMonedaException, CuentaOrigenYdestinoException {

        // Crear la transferencia a partir del DTO
        Transferencia transferencia = new Transferencia(transferenciaDto);
        // Obtener los datos de la transferencia
        long numeroCuentaOrigen = transferencia.getCuentaOrigen();
        long numeroCuentaDestino = transferencia.getCuentaDestino();
        double monto = transferencia.getMonto();
        String moneda = String.valueOf(transferenciaDto.getMoneda());

        // Buscar las cuentas en el DAO
        Cuenta cuentaOrigen = cuentaDao.findCuenta(numeroCuentaOrigen);
        Cuenta cuentaDestino = cuentaDao.findCuenta(numeroCuentaDestino);

        // Validar existencia de la cuenta origen
        if (cuentaOrigen == null) {
            throw new CuentaNoEncontradaException("Cuenta origen no encontrada");
        }

        // Validar que la cuenta origen tenga saldo suficiente
        if (cuentaOrigen.getBalance() < monto) {
            throw new NoAlcanzaException("Saldo insuficiente en la cuenta origen");
        }

        // Validar que la moneda coincida
        if (cuentaOrigen.getMoneda() != TipoMoneda.fromString(moneda)) {
            throw new TipoMonedaException("El tipo de moneda ingresado no coincide con el tipo de moneda de la cuenta origen");
        }

        // Si la cuenta destino no existe
        if (cuentaDestino == null) {
            // Verificar si es una cuenta externa
            if (banelco.cuentaExiste(numeroCuentaDestino)) {
                procesarTransferenciaExterna(cuentaOrigen, numeroCuentaDestino, monto, transferencia);
            } else {
                throw new CuentaNoEncontradaException("Cuenta destino no encontrada");
            }
        } else {
            // Validar que la cuenta de origen y destino no sean la misma
            if (cuentaOrigen == cuentaDestino) {
                throw new CuentaOrigenYdestinoException("La cuenta de origen y la cuenta de destino son iguales.");
            }

            // Validar que las monedas coincidan entre las cuentas
            if (cuentaOrigen.getMoneda() != cuentaDestino.getMoneda()) {
                throw new TipoMonedaException("El tipo de moneda de las cuentas bancarias no coincide");
            }

            // Procesar la transferencia interna
            procesarTransferenciaInterna(cuentaOrigen, cuentaDestino, monto, transferencia);
        }
        // Crear TransaccionDto a partir del TransferenciaDto

// Crear TransaccionDto a partir del TransferenciaDto
        TransaccionDto transaccionDto = new TransaccionDto(
                transferenciaDto.getCuentaOrigen(),
                transferenciaDto.getCuentaDestino(),
                transferenciaDto.getMonto(),
                transferenciaDto.getTipo(),
                transferenciaDto.getDescripcionBreve(),
                transferenciaDto.getMoneda(),
                LocalDateTime.now()
        );

        // Crear la transacción usando el DTO
        Transaccion transaccion = new Transaccion(transaccionDto);
        // Guardar la transacción en el TransaccionDao
        transaccionDao.agregarTransaccion(transaccion);

        // Actualizar las cuentas en el DAO
        cuentaDao.updateCuenta(cuentaOrigen);
        if (cuentaDestino != null) {
            cuentaDao.updateCuenta(cuentaDestino);
        }

        // Devolver la transferencia realizada
        return transferencia;
    }


    public void procesarTransferenciaInterna(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto, Transferencia transferencia) throws NoAlcanzaException {
        double comision = calcularComision(cuentaOrigen, monto);
        double montoFinal = monto - comision;

        cuentaOrigen.debitar(montoFinal);
        cuentaDestino.acreditar(montoFinal);
        transferenciaDao.guardarTransferencia(transferencia);
    }

    public void procesarTransferenciaExterna(Cuenta cuentaOrigen, long cuentaDestinoNumero, double monto, Transferencia transferencia) throws NoAlcanzaException {
        double comision = calcularComision(cuentaOrigen, monto);
        double montoFinal = monto - comision;

        cuentaOrigen.debitar(montoFinal);
        banelco.acreditar(montoFinal, cuentaDestinoNumero);
        transferenciaDao.guardarTransferencia(transferencia);
    }

    public double calcularComision(Cuenta cuenta, double monto)  {
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


    @Override
    public List<TransaccionDto> obtenerHistorialTransacciones(long idCuenta) throws CuentaNoEncontradaException {
        Cuenta cuenta = cuentaDao.findCuenta(idCuenta);
        if (cuenta == null) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada");
        }

        // Obtener las transacciones desde el TransaccionDao
        List<Transaccion> transacciones = transaccionDao.obtenerTransaccionesPorCuenta(idCuenta);

        // Convertir las transacciones a DTOs
        return transacciones.stream()
                .map(transaccion -> new TransaccionDto(
                        transaccion.getCuentaOrigen(),
                        transaccion.getCuentaDestino(),
                        transaccion.getMonto(),
                        transaccion.getTipo(),
                        transaccion.getDescripcionBreve(),
                        transaccion.getMoneda(),
                        transaccion.getFecha()
                ))
                .collect(Collectors.toList());
    }







    public List<Transferencia> find(long id) {
        return transferenciaDao.findTransfersByID(id);
    }

    public List<Transferencia> findAll() {
        return transferenciaDao.findAllTransfers();
    }


}
