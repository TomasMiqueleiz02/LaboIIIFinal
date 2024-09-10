package ar.edu.utn.frbb.tup.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransaccionDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class CuentaControllerTest {

    @Mock
    private CuentaService cuentaService;
    @Mock
    private TransferenciaService transferenciaService;
    @Mock
    CuentaValidator cuentaValidator;
    @InjectMocks
    private CuentaController cuentaController;

    @Test
    public void testDarDeAltaCuenta_ValidData() throws Exception {
        // crear un objeto CuentaDto
        CuentaDto cuentaDto = new CuentaDto(0, "CAJA_AHORRO", "PESOS", 1000.0, 87654321L);
        CuentaDto nuevaCuentaDto = new CuentaDto(1, "CAJA_AHORRO", "PESOS", 1000.0, 87654321L);
        doNothing().when(cuentaValidator).validate(cuentaDto);
        // simular el comportamiento del servicio

        when(cuentaService.darDeAltaCuenta(cuentaDto)).thenReturn(nuevaCuentaDto);

        // ejecuta la llamada al controlador
        ResponseEntity<?> respuesta = cuentaController.darDeAltaCuenta(cuentaDto);

        // verificar el resultado
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(nuevaCuentaDto, respuesta.getBody());
    }

    @Test
    public void testObtenerHistorialTransacciones() throws Exception {
        long cuentaId = 12345678L;// define un ID de cuenta para usar en la prueba
        // configura el servicio para que devuelva la lista de transacciones
        List<TransaccionDto> transacciones = Arrays.asList(new TransaccionDto(), new TransaccionDto());

        when(transferenciaService.obtenerHistorialTransacciones(cuentaId)).thenReturn(transacciones);
        // Llama al método del controlador para obtener el historial de transacciones
        ResponseEntity<Map<String, Object>> respuesta = cuentaController.obtenerHistorialTransacciones(cuentaId);
        //obtiene la respuesta
        Map<String, Object> responseBody = respuesta.getBody();
        //verifica los resultados
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(cuentaId, responseBody.get("numeroCuenta"));
        assertEquals(transacciones, responseBody.get("transacciones"));
    }

    @Test
    public void testBuscarCuentaPorNumero() throws Exception {
        long numeroCuenta = 12345678L;// define un numero de cuenta para usar en la prueba
        CuentaDto cuentaDto = new CuentaDto(12345678, "CAJA_AHORRO", "PESOS", 1000.0, 87654321L);
        //simula el service para que devuelva la cuenta cuando se llame al numeroCuenta
        when(cuentaService.buscarCuentaPorNumero(numeroCuenta)).thenReturn(cuentaDto);

        ResponseEntity<?> respuesta = cuentaController.buscarCuentaPorNumero(numeroCuenta);
        //verifica los resultados
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(cuentaDto, respuesta.getBody());
    }

    @Test
    public void testObtenerTodasLasCuentas() throws Exception {
        List<CuentaDto> cuentasDto = Arrays.asList(new CuentaDto(0, "CAJA_AHORRO", "PESOS", 1000.0, 87654321L), new CuentaDto(1, "CAJA_AHORRO", "PESOS", 1000.0, 87654321L));
        //simula el service para que devuelva la lista de cuentasDto
        when(cuentaService.obtenerTodasLasCuentas()).thenReturn(cuentasDto);
        // llama al método del controlador para obtener todas las cuentas
        ResponseEntity<List<CuentaDto>> respuesta = cuentaController.obtenerTodasLasCuentas();
        //verifica los resultados
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(cuentasDto, respuesta.getBody());
    }

    @Test
    public void testEliminarCuenta() throws Exception {
        long numeroCuenta = 12345678L;
        // Configura servicio para que no haga nada cuando se elimine una cuenta con el numeroCuenta
        doNothing().when(cuentaService).eliminarCuenta(numeroCuenta);
        // llama al método del controlador para eliminar la cuenta
        ResponseEntity<?> respuesta = cuentaController.eliminarCuenta(numeroCuenta);
        //verifica los resultados
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Cuenta eliminada con éxito", respuesta.getBody());
    }
}

