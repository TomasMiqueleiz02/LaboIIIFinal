package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.impl.CuentaDaoImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaDaoImplTest {

    private CuentaDaoImpl cuentaDao;

    @BeforeEach
    public void setUp() {
        cuentaDao = new CuentaDaoImpl();
    }

    @Test
    public void testSaveCuenta() {
        Cuenta cuenta = new Cuenta(new CuentaDto(1234567L, "CUENTA_CORRIENTE", "DOLARES", 1000.0,12345678));
        Cuenta result = cuentaDao.saveCuenta(cuenta);
        //verifica
        assertNotNull(result);
        assertEquals(cuenta.getNumeroCuenta(), result.getNumeroCuenta());
        assertEquals(cuenta.getTipoCuenta(), result.getTipoCuenta());
        assertEquals(cuenta.getMoneda(), result.getMoneda());
        assertEquals(cuenta.getBalance(), result.getBalance());
    }

    @Test
    public void testFindCuenta() {
        Cuenta cuenta = new Cuenta(new CuentaDto(1234567L, "CUENTA_CORRIENTE", "DOLARES", 1000.0,12345678));
        cuentaDao.saveCuenta(cuenta);

        Cuenta result = cuentaDao.findCuenta(1234567L);
        //verifica
        assertNotNull(result);
        assertEquals(cuenta.getNumeroCuenta(), result.getNumeroCuenta());
        assertEquals(cuenta.getTipoCuenta(), result.getTipoCuenta());
        assertEquals(cuenta.getMoneda(), result.getMoneda());
        assertEquals(cuenta.getBalance(), result.getBalance());
    }

    @Test
    public void testFindAllCuentas() {
        Cuenta cuenta1 = new Cuenta(new CuentaDto(1234567L, "CUENTA_CORRIENTE", "DOLARES", 1000.0,87654321));
        Cuenta cuenta2 = new Cuenta(new CuentaDto(2345678L, "CAJA_AHORRO", "PESOS", 2000.0,12345678));
        cuentaDao.saveCuenta(cuenta1);
        cuentaDao.saveCuenta(cuenta2);

        List<Cuenta> cuentas = cuentaDao.findAllCuentas();
        //verifica
        assertNotNull(cuentas);
        assertEquals(2, cuentas.size());
        assertTrue(cuentas.contains(cuenta1));
        assertTrue(cuentas.contains(cuenta2));
    }

    @Test
    public void testDeleteCuenta() {
        Cuenta cuenta = new Cuenta(new CuentaDto(1234567L, "CUENTA_CORRIENTE", "DOLARES", 1000.0,12345678));
        cuentaDao.saveCuenta(cuenta);
        cuentaDao.deleteCuenta(1234567L);

        Cuenta result = cuentaDao.findCuenta(1234567L);
        //verifica
        assertNull(result);
    }

    @Test
    public void testUpdateCuenta() {
        Cuenta cuenta = new Cuenta(new CuentaDto(1234567L, "CUENTA_CORRIENTE", "DOLARES", 1000.0,12345678));
        cuentaDao.saveCuenta(cuenta);

        cuenta.setBalance(1500.0);
        cuentaDao.updateCuenta(cuenta);

        Cuenta result = cuentaDao.findCuenta(1234567L);
        //verifica
        assertNotNull(result);
        assertEquals(1500.0, result.getBalance());
    }

    @Test
    public void testUpdateCuentaNotFound() {
        Cuenta cuenta = new Cuenta(new CuentaDto(1234567L, "CUENTA_CORRIENTE", "DOLARES", 1000.0,12345678));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cuentaDao.updateCuenta(cuenta);
        });
        //verifica
        assertEquals("La cuenta no existe", exception.getMessage());
    }
}


