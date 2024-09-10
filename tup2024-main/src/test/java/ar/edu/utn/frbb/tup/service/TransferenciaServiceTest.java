package ar.edu.utn.frbb.tup.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
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
import ar.edu.utn.frbb.tup.service.Impl.CuentaServiceImpl;
import ar.edu.utn.frbb.tup.service.Impl.TransferenciaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TransferenciaServiceTest {

    @Mock
    CuentaDao cuentaDao;
    @Mock
    TransaccionDao transaccionDao;

    @Mock
    TransferenciaDao transferenciaDao;

    @Mock
    BanelcoService banelco;

    @InjectMocks
    TransferenciaServiceImpl transferenciaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testRealizarTransferencia_Exito() throws Exception {
        TransferenciaDto transferenciaDto = getTransferenciaDto();
        CuentaDto cuentaDtoOrigen =  getCuentaDtoOrigen();
        Cuenta cuentaOrigen = new Cuenta(cuentaDtoOrigen);
        CuentaDto cuentaDtoDestino = getCuentaDtoDestino();
        Cuenta cuentaDestino = new Cuenta(cuentaDtoDestino);

        // configura el comportamiento
        when(cuentaDao.findCuenta(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.findCuenta(2L)).thenReturn(cuentaDestino);
        doNothing().when(transaccionDao).agregarTransaccion(any(Transaccion.class));
        when(banelco.cuentaExiste(anyLong())).thenReturn(false);

        Transferencia transferencia = transferenciaService.realizarTransferencia(transferenciaDto);

        assertNotNull(transferencia);
        verify(cuentaDao).updateCuenta(cuentaOrigen);
        verify(cuentaDao).updateCuenta(cuentaDestino);
        verify(transaccionDao).agregarTransaccion(any(Transaccion.class));
    }

    @Test
    public void testRealizarTransferencia_ErrorTipoMoneda() throws Exception {
        TransferenciaDto transferenciaDto = getTransferenciaDto();

        CuentaDto cuentaDtoOrigen = getCuentaDtoOrigen();
        Cuenta cuentaOrigen = new Cuenta(cuentaDtoOrigen);
        // configura el comportamiento
        when(cuentaDao.findCuenta(1L)).thenReturn(cuentaOrigen);
        //verifica
        assertThrows(Exception.class, () -> transferenciaService.realizarTransferencia(transferenciaDto));
    }

    @Test
    public void testRealizarTransferencia_ErrorCuentaOrigenNoEncontrada() {
        TransferenciaDto transferenciaDto = getTransferenciaDto();
        // configura el comportamiento
        when(cuentaDao.findCuenta(1L)).thenReturn(null);
        //se espera que el método arroje una excepción indicando que la cuenta de origen no fue encontrada.
        Exception exception = assertThrows(Exception.class, () -> {
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
        //verifica
        assertEquals("Cuenta origen no encontrada", exception.getMessage());
    }

    @Test
    public void testRealizarTransferencia_ErrorCuentaDestinoNoEncontrada() throws Exception {
        TransferenciaDto transferenciaDto = getTransferenciaDto();

        CuentaDto cuentaDtoOrigen = getCuentaDtoOrigen();
        Cuenta cuentaOrigen = new Cuenta(cuentaDtoOrigen);
        // configura el comportamiento
        when(cuentaDao.findCuenta(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.findCuenta(2L)).thenReturn(null);
        when(banelco.cuentaExiste(2L)).thenReturn(false);
        //se espera que el método arroje una excepción
        assertThrows(Exception.class, () -> transferenciaService.realizarTransferencia(transferenciaDto));

        verify(cuentaDao, never()).updateCuenta(any());
    }

    @Test
    public void testProcesarTransferenciaInterna() throws NoAlcanzaException {
        // Datos de prueba
        Cuenta cuentaOrigen = new Cuenta(getCuentaDtoOrigen());
        Cuenta cuentaDestino = new Cuenta(getCuentaDtoDestino());
        Transferencia transferencia = new Transferencia();

        // Mockear métodos necesarios
        doNothing().when(transferenciaDao).guardarTransferencia(any(Transferencia.class));

        // Ejecutar el método a probar
        transferenciaService.procesarTransferenciaInterna(cuentaOrigen, cuentaDestino, 99, transferencia);

        // verifica
        verify(transferenciaDao, times(1)).guardarTransferencia(transferencia);
        assertEquals(901, cuentaOrigen.getBalance(), 0.01);
        assertEquals(1099, cuentaDestino.getBalance(), 0.01);
    }


    @Test
    public void testProcesarTransferenciaExterna() throws NoAlcanzaException {
        // Datos de prueba
        Cuenta cuentaOrigen = new Cuenta(getCuentaDtoOrigen());
        long cuentaDestinoNumero = 12345678;
        Transferencia transferencia = new Transferencia();


        // mockear métodos necesarios
        doNothing().when(transferenciaDao).guardarTransferencia(any(Transferencia.class));
        doNothing().when(banelco).acreditar(anyDouble(), anyLong());

        // ejecutar el método a probar
        transferenciaService.procesarTransferenciaExterna(cuentaOrigen, cuentaDestinoNumero, 99, transferencia);

        // verifica

        verify(banelco, times(1)).acreditar(anyDouble(), anyLong());
        verify(transferenciaDao, times(1)).guardarTransferencia(transferencia);
        assertNotNull(banelco, "banelco should not be null");
        assertEquals(901, cuentaOrigen.getBalance(), 0.01);
    }


    @Test
    public void testFind() {
        // datos de prueba
        Transferencia t1 = new Transferencia();
        Transferencia t2 = new Transferencia();
        List<Transferencia> transferencias = Arrays.asList(t1, t2);
        long id = 1L;

        // configura el comportamiento
        when(transferenciaDao.findTransfersByID(id)).thenReturn(transferencias);

        // llamar al método que se va a probar
        List<Transferencia> result = transferenciaService.find(id);

        // verifica
        assertEquals(2, result.size());
        verify(transferenciaDao, times(1)).findTransfersByID(id);
    }

    @Test
    public void testFindAll() {
        // Datos de prueba
        Transferencia t1 = new Transferencia();
        Transferencia t2 = new Transferencia();
        List<Transferencia> transferencias = Arrays.asList(t1, t2);

        // configura el comportamiento
        when(transferenciaDao.findAllTransfers()).thenReturn(transferencias);

        // llamar al método que se va a probar
        List<Transferencia> result = transferenciaService.findAll();

        // Verificar los resultados
        assertEquals(2, result.size());
        verify(transferenciaDao, times(1)).findAllTransfers();
    }

    @Test
    public void testObtenerHistorialTransacciones_Exito() throws CuentaNoEncontradaException {
        long cuentaId = 1L;
        Cuenta cuenta = new Cuenta(getCuentaDto());
        Transaccion transaccion1 = new Transaccion(getTransaccionDto());

        List<Transaccion> transacciones = new ArrayList<>();
        transacciones.add(transaccion1);
        // configura el comportamiento
        when(cuentaDao.findCuenta(cuentaId)).thenReturn(cuenta);
        when(transaccionDao.obtenerTransaccionesPorCuenta(cuentaId)).thenReturn(transacciones);
        // llamar al método que se va a probar
        List<TransaccionDto> result = transferenciaService.obtenerHistorialTransacciones(cuentaId);


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaccion1.getMonto(), result.get(0).getMonto());
    }

    public CuentaDto getCuentaDtoOrigen() {
        CuentaDto cuentaDto = new CuentaDto(44357768,"CUENTA_CORRIENTE","Pesos",1000,12345678);

        return cuentaDto;
    }

    public CuentaDto getCuentaDtoDestino() {
        CuentaDto cuentaDto = new CuentaDto(43046272,"CUENTA_CORRIENTE","Pesos",1000,12345678);

        return cuentaDto;
    }

    public TransferenciaDto getTransferenciaDto() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMonto(99);
        transferenciaDto.setMoneda("Pesos");
        return transferenciaDto;
    }


    public TransaccionDto getTransaccionDto() {
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setCuentaDestino(2l);
        transaccionDto.setCuentaOrigen(1l);
        transaccionDto.setMoneda("Pesos");
        transaccionDto.setFecha(LocalDateTime.now());
        transaccionDto.setMonto(100);
        transaccionDto.setDescripcionBreve("PAGO");
        transaccionDto.setTipo("TRANSFERENCIA");
        return transaccionDto;

    }
    public CuentaDto getCuentaDto(){
        CuentaDto cuentaDto=new CuentaDto(43046272,"CUENTA_CORRIENTE","Pesos",1000,12345678);
        return cuentaDto;


    }
}

