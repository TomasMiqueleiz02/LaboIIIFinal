package ar.edu.utn.frbb.tup.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TransferenciaControllerTest {

    @InjectMocks
    private TransferenciaController transferenciaController;

    @Mock
    private TransferenciaService transferenciaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRealizarTransferencia_Success() throws Exception, CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, TipoMonedaException {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        Transferencia transferencia = new Transferencia(); // Configura la transferencia si es necesario
        // configura el servicio para que devuelva el objeto transferencia cuando se llame con transferenciaDto
        when(transferenciaService.realizarTransferencia(transferenciaDto)).thenReturn(transferencia);
        // llama al método del controlador para realizar la transferencia
        ResponseEntity<?> respuesta = transferenciaController.realizarTransferencia(transferenciaDto);
        //verifica
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(transferencia, respuesta.getBody());
    }

    @Test
    public void testRealizarTransferencia_CuentaNoEncontradaException() throws Exception,CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, TipoMonedaException {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        // configura servicio para lanzar una excepción de CuentaNoEncontradaException
        when(transferenciaService.realizarTransferencia(transferenciaDto))
                .thenThrow(new CuentaNoEncontradaException("Cuenta no encontrada"));
        // llama al método para realizar la transferencia
        ResponseEntity<?> respuesta = transferenciaController.realizarTransferencia(transferenciaDto);
        //verifica
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Cuenta no encontrada", respuesta.getBody());
    }

    @Test
    public void testRealizarTransferencia_NoAlcanzaException() throws Exception,CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, TipoMonedaException {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        // configura el servicio para lanzar una excepción de NoAlcanzaException
        when(transferenciaService.realizarTransferencia(transferenciaDto))
                .thenThrow(new NoAlcanzaException("Saldo insuficiente"));
        // llama al método para realizar la transferencia
        ResponseEntity<?> respuesta = transferenciaController.realizarTransferencia(transferenciaDto);
        //verifica
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Saldo insuficiente", respuesta.getBody());
    }

    @Test
    public void testRealizarTransferencia_TipoMonedaException() throws Exception,CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, TipoMonedaException {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        // configura el servicio para lanzar una excepción de TipoMonedaException
        when(transferenciaService.realizarTransferencia(transferenciaDto))
                .thenThrow(new TipoMonedaException("Tipo de moneda no coincide"));
        // llama al método para realizar la transferencia
        ResponseEntity<?> respuesta = transferenciaController.realizarTransferencia(transferenciaDto);
        //verifica
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("Tipo de moneda no coincide", respuesta.getBody());
    }

    @Test
    public void testRealizarTransferencia_CuentaOrigenYdestinoException() throws Exception,CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, TipoMonedaException {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        // configura el servicio para lanzar una excepción de CuentaOrigenYdestinoException
        when(transferenciaService.realizarTransferencia(transferenciaDto))
                .thenThrow(new CuentaOrigenYdestinoException("La cuenta de origen y destino son iguales"));
        // llama al método para realizar la transferencia
        ResponseEntity<?> respuesta = transferenciaController.realizarTransferencia(transferenciaDto);
        //verifica
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("La cuenta de origen y destino son iguales", respuesta.getBody());
    }

    @Test
    public void testRealizarTransferencia_Exception() throws Exception,CantidadNegativaException, NoAlcanzaException, CuentaNoEncontradaException, TipoMonedaException {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        // configura el servicio para lanzar una excepción de RuntimeException
        when(transferenciaService.realizarTransferencia(transferenciaDto))
                .thenThrow(new RuntimeException("Error interno"));
        // llama al método para realizar la transferencia
        ResponseEntity<?> respuesta = transferenciaController.realizarTransferencia(transferenciaDto);
        //verifica
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertEquals("Error interno del servidor", respuesta.getBody());
    }
}

