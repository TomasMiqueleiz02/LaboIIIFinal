package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.service.Impl.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CuentaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDarDeAltaCuenta()throws CuentaAlreadyExistsException {
        CuentaDto cuentaDto = new CuentaDto(1234567L, "CUENTA_CORRIENTE", "DOLARES", 1000.0,12345678);

        // Simula que el DAO guarda la cuenta con el número de cuenta específico
        Cuenta cuenta = new Cuenta(cuentaDto);

        when(cuentaDao.findCuenta(cuentaDto.getNumeroCuenta())).thenReturn(null); // Cuenta no existe
        when(cuentaDao.saveCuenta(any(Cuenta.class))).thenReturn(cuenta); // Guardado exitoso

        CuentaDto nuevoCuentaDto = cuentaService.darDeAltaCuenta(cuentaDto);

        assertNotNull(nuevoCuentaDto);
        assertEquals(cuentaDto.getNumeroCuenta(), nuevoCuentaDto.getNumeroCuenta());
        assertEquals(cuentaDto.getTipoCuenta(), nuevoCuentaDto.getTipoCuenta());
        assertEquals(cuentaDto.getMoneda(), nuevoCuentaDto.getMoneda());
        assertEquals(cuentaDto.getBalance(), nuevoCuentaDto.getBalance());

    }





    @Test
    public void testBuscarCuentaPorNumero() {
        CuentaDto cuentaDto = new CuentaDto(1234567L,"CUENTA_CORRIENTE", "DOLARES",1000.0,12345678);

        Cuenta cuenta = new Cuenta(cuentaDto);

        when(cuentaDao.findCuenta(cuentaDto.getNumeroCuenta())).thenReturn(cuenta);

        CuentaDto cuentaEncontrada = cuentaService.buscarCuentaPorNumero(cuentaDto.getNumeroCuenta());

        assertNotNull(cuentaEncontrada);
        assertEquals(cuentaDto.getNumeroCuenta(), cuentaEncontrada.getNumeroCuenta());
        assertEquals(cuentaDto.getTipoCuenta(), cuentaEncontrada.getTipoCuenta());
        assertEquals(cuentaDto.getMoneda(), cuentaEncontrada.getMoneda());
        assertEquals(cuentaDto.getBalance(), cuentaEncontrada.getBalance());
    }

    @Test
    public void testObtenerTodasLasCuentas() {
        CuentaDto cuentaDto1 = new CuentaDto(1234567L,"CUENTA_CORRIENTE", "DOLARES",1000.0,12345678);

        CuentaDto cuentaDto2 = new CuentaDto(7654321L,"CAJA_AHORRO","PESOS",500.0,12345678);

        List<Cuenta> cuentas = Arrays.asList(new Cuenta(cuentaDto1), new Cuenta(cuentaDto2));
        when(cuentaDao.findAllCuentas()).thenReturn(cuentas);

        List<CuentaDto> cuentasDto = cuentaService.obtenerTodasLasCuentas();

        assertNotNull(cuentasDto);
        assertEquals(2, cuentasDto.size());
    }

    @Test
    public void testEliminarCuenta() {
        long numeroCuenta = 1234567L;

        doNothing().when(cuentaDao).deleteCuenta(numeroCuenta);

        assertDoesNotThrow(() -> cuentaService.eliminarCuenta(numeroCuenta));
    }

}