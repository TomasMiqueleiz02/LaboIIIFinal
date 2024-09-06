package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.service.Impl.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuentaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cuenta = new Cuenta(); // Usa el constructor sin parámetros
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(1000.0);
    }

    @Test
    void testDarDeAltaCuenta() {
        when(cuentaDao.saveCuenta(cuenta)).thenReturn(cuenta);

        Cuenta nuevaCuenta = cuentaService.darDeAltaCuenta(cuenta);

        assertNotNull(nuevaCuenta);
        assertEquals(cuenta.getTipoCuenta(), nuevaCuenta.getTipoCuenta());
        assertEquals(cuenta.getMoneda(), nuevaCuenta.getMoneda());
        assertEquals(cuenta.getBalance(), nuevaCuenta.getBalance());
        verify(cuentaDao, times(1)).saveCuenta(cuenta);
    }
    @Test
    void testDarDeAltaCuentaFalla() {
        when(cuentaDao.saveCuenta(cuenta)).thenReturn(null); // Simula fallo en el DAO

        Cuenta nuevaCuenta = cuentaService.darDeAltaCuenta(cuenta);

        assertNull(nuevaCuenta); // Verifica que el resultado es null
    }


    @Test
    void testTipoCuentaEstaSoportada() {
        assertTrue(cuentaService.tipoCuentaEstaSoportada(TipoCuenta.CAJA_AHORRO));
        assertFalse(cuentaService.tipoCuentaEstaSoportada(null));
    }
    @Test
    void testTipoCuentaNoSoportada() {
        TipoCuenta tipoNoSoportado = null; // O usa un valor explícito si es posible
        assertFalse(cuentaService.tipoCuentaEstaSoportada(tipoNoSoportado));
    }



    @Test
    void testFindByID() throws CuentaNoEncontradaException {
        when(cuentaDao.findCuenta(cuenta.getNumeroCuenta())).thenReturn(cuenta);

        Cuenta foundCuenta = cuentaService.findByID(cuenta.getNumeroCuenta());

        assertNotNull(foundCuenta);
        assertEquals(cuenta.getNumeroCuenta(), foundCuenta.getNumeroCuenta());
        verify(cuentaDao, times(1)).findCuenta(cuenta.getNumeroCuenta());
    }
    @Test
    void testFindByIDFalla() throws CuentaNoEncontradaException {
        when(cuentaDao.findCuenta(cuenta.getNumeroCuenta())).thenReturn(null); // Simula fallo en el DAO

        assertThrows(CuentaNoEncontradaException.class, () -> cuentaService.findByID(cuenta.getNumeroCuenta()));
    }


    @Test
    void testFindByIDCuentaNoEncontrada() {
        when(cuentaDao.findCuenta(anyLong())).thenReturn(null);

        assertThrows(CuentaNoEncontradaException.class, () -> cuentaService.findByID(12345L));
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta);
        when(cuentaDao.findAllCuentas()).thenReturn(cuentas);

        List<Cuenta> allCuentas = cuentaService.findAll();

        assertEquals(1, allCuentas.size());
        verify(cuentaDao, times(1)).findAllCuentas();
    }
    @Test
    void testFindAllFalla() {
        when(cuentaDao.findAllCuentas()).thenReturn(new ArrayList<>()); // Simula lista vacía

        List<Cuenta> allCuentas = cuentaService.findAll();

        assertTrue(allCuentas.isEmpty()); // Verifica que la lista está vacía
    }

    @Test
    void testEliminarCuenta() throws CuentaNoEncontradaException {
        when(cuentaDao.findCuenta(cuenta.getNumeroCuenta())).thenReturn(cuenta);

        cuentaService.eliminarCuenta(cuenta.getNumeroCuenta());

        verify(cuentaDao, times(1)).deleteCuenta(cuenta.getNumeroCuenta());
    }
    @Test
    void testEliminarCuentaFalla() throws CuentaNoEncontradaException {
        when(cuentaDao.findCuenta(cuenta.getNumeroCuenta())).thenReturn(null); // Simula que la cuenta no existe

        assertThrows(CuentaNoEncontradaException.class, () -> cuentaService.eliminarCuenta(cuenta.getNumeroCuenta()));
    }


}
