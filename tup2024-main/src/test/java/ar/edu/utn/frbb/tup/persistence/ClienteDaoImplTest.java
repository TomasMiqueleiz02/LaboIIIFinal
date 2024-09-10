package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.persistence.impl.ClienteDaoImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteDaoImplTest {
    private ClienteDao clienteDao;

    @BeforeAll
    public void setUp() {
        clienteDao = new ClienteDaoImpl();
    }

    @Test
    public void testSaveCliente() {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);

        Cliente clienteGuardado = clienteDao.saveCliente(cliente);
        //verifica
        assertNotNull(clienteGuardado);
        assertEquals(12345678L, clienteGuardado.getDni());
        assertEquals("Pepe", clienteGuardado.getNombre());
        assertEquals("Rino", clienteGuardado.getApellido());
    }

    @Test
    public void testFindCliente() {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);

        clienteDao.saveCliente(cliente);
        Cliente clienteObtenido = clienteDao.findCliente(12345678L);
        //verifica
        assertNotNull(clienteObtenido);
        assertEquals(12345678L, clienteObtenido.getDni());
        assertEquals("Pepe", clienteObtenido.getNombre());
        assertEquals("Rino", clienteObtenido.getApellido());
    }

    @Test
    public void testFindAll() {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente1 = new Cliente(clienteDto);
        Cliente cliente2 = new Cliente(clienteDto);
        cliente2.setDni(43046811);

        clienteDao.saveCliente(cliente1);
        clienteDao.saveCliente(cliente2);

        List<Cliente> clientes = clienteDao.findAll();
        //verifica
        assertEquals(2, clientes.size());
        assertTrue(clientes.contains(cliente1));
        assertTrue(clientes.contains(cliente2));
    }
    @Test
    public void testUpdateCliente() {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);
        clienteDao.saveCliente(cliente);

        // Crear una versión actualizada del cliente
        clienteDto.setNombre("Pepe Updated");
        Cliente clienteActualizado = new Cliente(clienteDto);

        Cliente resultado = clienteDao.updateCliente(clienteActualizado);
        //verifica
        assertNotNull(resultado);
        assertEquals("Pepe Updated", resultado.getNombre());
    }

    @Test
    public void testDeleteCliente() {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);
        clienteDao.saveCliente(cliente);

        clienteDao.deleteCliente(cliente.getDni());
        Cliente resultado = clienteDao.findCliente(cliente.getDni());
        //verifica
        assertNull(resultado);
    }

    public ClienteDto getClienteDto(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Pepe");
        clienteDto.setApellido("Rino");
        clienteDto.setDni(12345678L);
        clienteDto.setFechaNacimiento(LocalDate.of(1997, 10, 17));
        clienteDto.setTipoPersona(TipoPersona.FISICA);
        return clienteDto;
    }
}