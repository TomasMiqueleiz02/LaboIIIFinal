package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
public class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;
    @Mock
    private ClienteService clienteService;
    private ClienteDto clienteDto;
    @Mock
    private ClienteValidator clienteValidator;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteDto = new ClienteDto("Jhon", "Doe", 12345678L, LocalDate.of(1999,01,01), TipoPersona.FISICA);
    }

    @Test
    public void testDarDeAltaCliente_ValidData() throws Exception {
        doNothing().when(clienteValidator).validate(clienteDto);
        // Simular el comportamiento del servicio
        when(clienteService.darDeAltaCliente(clienteDto)).thenReturn(clienteDto);

        // Ejecutar la llamada al controlador
        ResponseEntity<ClienteDto> respuesta = clienteController.darDeAltaCliente(clienteDto);

        // Verificar el resultado
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(clienteDto, respuesta.getBody());
    }




    @Test
    public void testBuscarClientePorDni_ClienteEncontrado()throws ClienteNoEncontradoException {

        // Configurar el comportamiento del servicio
        when(clienteService.buscarClientePorDni(12345678L)).thenReturn(clienteDto);

        // Ejecutar la llamada al controlador
        ResponseEntity<?> respuesta = clienteController.buscarClientePorDni(12345678L);

        // Verificar el resultado
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(clienteDto, respuesta.getBody());

        // Verificar que el servicio fue llamado
        verify(clienteService, times(1)).buscarClientePorDni(12345678L);
    }



    @Test
    public void testObtenerTodosLosClientes() {
        List<ClienteDto> listaClientes = new ArrayList<>();
        listaClientes.add(clienteDto);
        // Simular el comportamiento del servicio
        when(clienteService.obtenerTodosLosClientes()).thenReturn(listaClientes);
        // Ejecutar la llamada al controlador
        ResponseEntity<List<ClienteDto>> respuesta = clienteController.obtenerTodosLosClientes();
        // Verificar el resultado
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());

    }

    @Test
    public void testEliminarCliente_Success()throws ClienteNoEncontradoException {
        // Simular el comportamiento del servicio
        doNothing().when(clienteService).eliminarCliente(12345678L);
        // Ejecutar la llamada al controlador
        ResponseEntity<?> respuesta = clienteController.eliminarCliente(12345678L);
        // Verificar el resultado
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("Cliente eliminado con éxito", respuesta.getBody());
    }


}