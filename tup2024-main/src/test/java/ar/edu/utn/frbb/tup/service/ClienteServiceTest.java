package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.service.Impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setDni(12345678L);
        // Inicializa otros campos si es necesario
    }


    @Test
    void testDarDeAltaCliente() {
        // Configura el mock para retornar el cliente
        when(clienteDao.saveCliente(any(Cliente.class))).thenReturn(cliente);

        // Llama al método del servicio
        Cliente resultado = clienteService.darDeAltaCliente(cliente);

        // Verifica el resultado
        assertNotNull(resultado); // Asegúrate de que el resultado no sea null
        assertEquals("Juan", resultado.getNombre()); // Verifica que el nombre es correcto
        verify(clienteDao, times(1)).saveCliente(any(Cliente.class)); // Verifica que el DAO fue llamado correctamente
    }


    @Test
    void testAgregarCuenta() {
        when(clienteDao.findCliente(12345678L)).thenReturn(cliente);
        clienteService.agregarCuenta(12345678L, cuenta);
        assertEquals(1, cliente.getCuentas().size());
        verify(clienteDao, times(1)).updateCliente(cliente);
    }

    @Test
    void testBuscarClientePorDni() throws ClienteNoEncontradoException {
        when(clienteDao.findCliente(12345678L)).thenReturn(cliente);
        Cliente resultado = clienteService.buscarClientePorDni(12345678L);
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(clienteDao, times(1)).findCliente(12345678L);
    }

    @Test
    void testObtenerTodosLosClientes() {
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteDao.findAll()).thenReturn(clientes);
        List<Cliente> resultado = clienteService.obtenerTodosLosClientes();
        assertEquals(1, resultado.size());
        verify(clienteDao, times(1)).findAll();
    }

    @Test
    void testActualizarCliente() {
        when(clienteDao.updateCliente(cliente)).thenReturn(cliente);
        Cliente resultado = clienteService.actualizarCliente(cliente);
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(clienteDao, times(1)).updateCliente(cliente);
    }

    @Test
    void testEliminarCliente() {
        clienteService.eliminarCliente(12345678L);
        verify(clienteDao, times(1)).deleteCliente(12345678L);
    }
}
