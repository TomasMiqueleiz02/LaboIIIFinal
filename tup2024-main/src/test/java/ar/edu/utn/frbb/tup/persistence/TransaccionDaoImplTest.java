package ar.edu.utn.frbb.tup.persistence;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frbb.tup.controller.dto.TransaccionDto;
import ar.edu.utn.frbb.tup.model.Transaccion;
import ar.edu.utn.frbb.tup.persistence.impl.TransaccionDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class TransaccionDaoImplTest {

    private TransaccionDaoImpl transaccionDao;

    @BeforeEach
    void setUp() {
        transaccionDao = new TransaccionDaoImpl();
    }

    @Test
    public void testAgregarTransaccion() {
        Transaccion transaccion = new Transaccion(new TransaccionDto(
                1L, 2L, 100.0, "TRANSFERENCIA", "Pago", "Pesos", LocalDateTime.now()
        ));
        transaccionDao.agregarTransaccion(transaccion);

        //verifica
        List<Transaccion> transacciones = transaccionDao.obtenerTransaccionesPorCuenta(1L);
        assertNotNull(transacciones);
        assertEquals(1, transacciones.size());
        assertEquals(transaccion, transacciones.get(0));
    }

    @Test
    public void testObtenerTransaccionesPorCuenta() {
        Transaccion transaccion1 = new Transaccion(new TransaccionDto(
                1L, 2L, 100.0, "TRANSFERENCIA", "Pago", "Pesos", LocalDateTime.now()
        ));
        Transaccion transaccion2 = new Transaccion(new TransaccionDto(
                1L, 3L, 200.0, "TRANSFERENCIA", "Pago", "Pesos", LocalDateTime.now().minusDays(1)
        ));
        Transaccion transaccion3 = new Transaccion(new TransaccionDto(
                4L, 5L, 300.0, "TRANSFERENCIA", "Pago", "Pesos", LocalDateTime.now().minusDays(2)
        ));

        transaccionDao.agregarTransaccion(transaccion1);
        transaccionDao.agregarTransaccion(transaccion2);
        transaccionDao.agregarTransaccion(transaccion3);

        List<Transaccion> result = transaccionDao.obtenerTransaccionesPorCuenta(1L);

        //verifica
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(transaccion1));
        assertTrue(result.contains(transaccion2));
        assertFalse(result.contains(transaccion3));
    }
}

