package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.service.Impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    public void testDarDeAltaCliente() throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = convertirDtoAEntidad(clienteDto);

        when(clienteDao.findCliente(clienteDto.getDni())).thenReturn(null); // Cliente no existe
        when(clienteDao.saveCliente(any(Cliente.class))).thenReturn(cliente);
        // Guardado exitoso

        ClienteDto nuevoClienteDto = clienteService.darDeAltaCliente(clienteDto);

        assertNotNull(nuevoClienteDto);
        assertEquals(clienteDto.getNombre(), nuevoClienteDto.getNombre());
    }

    @Test
    public void testBuscarClientePorDni() throws ClienteNoEncontradoException {
        long dni = 12345678L;
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = convertirDtoAEntidad(clienteDto);

        when(clienteDao.findCliente(dni)).thenReturn(cliente);

        ClienteDto clienteBuscado = clienteService.buscarClientePorDni(dni);

        assertNotNull(clienteBuscado);
        assertEquals(clienteDto.getNombre(), clienteBuscado.getNombre());
    }

    @Test
    public void testObtenerTodosLosClientes() {
        List<Cliente> clientes = Arrays.asList(new Cliente(), new Cliente());
        List<ClienteDto> clientesDto = clientes.stream()
                .map(this::convertirEntidadADto)
                .toList();

        when(clienteDao.findAll()).thenReturn(clientes);

        List<ClienteDto> resultado = clienteService.obtenerTodosLosClientes();

        assertNotNull(resultado);
        assertEquals(clientesDto.size(), resultado.size());
    }

    @Test
    public void testEliminarCliente() throws ClienteNoEncontradoException {
        long dni = 12345678L;

        when(clienteDao.findCliente(dni)).thenReturn(new Cliente()); // Cliente existe
        doNothing().when(clienteDao).deleteCliente(dni);

        clienteService.eliminarCliente(dni);

        verify(clienteDao, times(1)).deleteCliente(dni);
    }

    private ClienteDto getClienteDto() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Pepe");
        clienteDto.setApellido("Rino");
        clienteDto.setDni(12345678L);
        clienteDto.setFechaNacimiento(LocalDate.of(1997, 10, 17));
        clienteDto.setTipoPersona(TipoPersona.FISICA);
        return clienteDto;
    }

    private Cliente convertirDtoAEntidad(ClienteDto clienteDto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDto.getNombre());
        cliente.setApellido(clienteDto.getApellido());
        cliente.setDni(clienteDto.getDni());
        cliente.setFechaNacimiento(clienteDto.getFechaNacimiento());
        cliente.setTipoPersona(clienteDto.getTipoPersona());
        return cliente;
    }

    private ClienteDto convertirEntidadADto(Cliente cliente) {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre(cliente.getNombre());
        clienteDto.setApellido(cliente.getApellido());
        clienteDto.setDni(cliente.getDni());
        clienteDto.setFechaNacimiento(cliente.getFechaNacimiento());
        clienteDto.setTipoPersona(cliente.getTipoPersona());
        return clienteDto;
    }
}
