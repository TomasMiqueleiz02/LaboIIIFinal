package ar.edu.utn.frbb.tup.service.Impl;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;

import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
    public class CuentaServiceImpl implements CuentaService {

        private final CuentaDao cuentaDao;

        @Autowired
        public CuentaServiceImpl(CuentaDao cuentaDao) {
            this.cuentaDao = cuentaDao;
        }

        @Override
        public CuentaDto darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException {
            // Verifica si la cuenta ya existe
            Cuenta cuentaExistente = cuentaDao.findCuenta(cuentaDto.getNumeroCuenta());
            if (cuentaExistente != null) {
                throw new IllegalArgumentException("La cuenta ya existe");
            }
            cuentaExistente = cuentaDao.findByDni(cuentaDto.getdniTitular());
            if (cuentaExistente != null) {
                throw new CuentaAlreadyExistsException("Ya existe una cuenta para el cliente con el mismo DNI.");
            }

            // Convierte el DTO a entidad
            Cuenta cuenta = new Cuenta(cuentaDto);

            // Guarda la cuenta
            Cuenta cuentaGuardada = cuentaDao.saveCuenta(cuenta);

            // Convierte la entidad de nuevo a DTO
            return convertirEntidadADto(cuentaGuardada);
        }

        @Override
        public CuentaDto buscarCuentaPorNumero(long numeroCuenta) {
            Cuenta cuenta = cuentaDao.findCuenta(numeroCuenta);
            if (cuenta != null) {
                return convertirEntidadADto(cuenta);
            } else {
                return null; // O lanza una excepción si prefieres
            }
        }

        @Override
        public List<CuentaDto> obtenerTodasLasCuentas() {
            List<Cuenta> cuentas = cuentaDao.findAllCuentas();
            return cuentas.stream()
                    .map(this::convertirEntidadADto)
                    .collect(Collectors.toList());
        }

        @Override
        public void eliminarCuenta(long numeroCuenta) {
            cuentaDao.deleteCuenta(numeroCuenta);
        }


        private Cuenta convertirDtoAEntidad(CuentaDto dto) {
            return new Cuenta(dto.getNumeroCuenta(), TipoCuenta.fromString(dto.getTipoCuenta()), TipoMoneda.fromString(dto.getMoneda()), dto.getBalance(), dto.getdniTitular());
        }


        private CuentaDto convertirEntidadADto(Cuenta cuenta) {
            return new CuentaDto(cuenta.getNumeroCuenta(), cuenta.getTipoCuenta().toString(), cuenta.getMoneda().toString(), cuenta.getBalance(), cuenta.getdniTitular());
        }
    }
