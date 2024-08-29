package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDarDeAltaCliente_Error() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);

        when(clienteDao.findCliente(12345678L)).thenReturn(cliente);
        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(clienteDto));

        verify(clienteDao,times(1)).findCliente(12345678L);
        verify(clienteDao,times(0)).saveCliente(any(Cliente.class));
    }

    @Test
    public void testClienteMenor18Años() {
        ClienteDto clienteMenorDeEdad = new ClienteDto();
        clienteMenorDeEdad.setFechaNacimiento(LocalDate.of(2020, 2, 7));
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testAgregarCuenta() throws TipoCuentaAlreadyExistsException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);
        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = new Cuenta(cuentaDto);

        when(clienteDao.findCliente(cliente.getDni())).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta, cliente.getDni());

        assertTrue(cliente.getCuentas().contains(cuenta));
        assertEquals(cliente, cuenta.getTitular());
    }

    @Test
    public void testBuscarClientePorDni() throws ClienteNoEncontradoException {
        ClienteDto clienteDto = getClienteDto();
        Cliente clienteEsperado = new Cliente(clienteDto);

        when(clienteDao.findCliente(clienteEsperado.getDni())).thenReturn(clienteEsperado);

        Cliente clienteObtenido = clienteService.buscarClientePorDni(clienteEsperado.getDni());

        assertNotNull(clienteObtenido);
        assertEquals(clienteEsperado, clienteObtenido);
    }

    @Test
    public void testObtenerTodosLosClientes() {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente1 = new Cliente(clienteDto);
        Cliente cliente2 = new Cliente(clienteDto);
        cliente2.setDni(43049832);

        List<Cliente> clientesEsperados = Arrays.asList(cliente1,cliente2);

        when(clienteDao.findAll()).thenReturn(clientesEsperados);

        List<Cliente> clientesObtenidos = clienteService.obtenerTodosLosClientes();

        assertEquals(clientesEsperados, clientesObtenidos);
    }


    public ClienteDto getClienteDto(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setDni(12345678L);
        clienteDto.setFechaNacimiento(LocalDate.of(1997, 10, 17));
        clienteDto.setTipoPersona(TipoPersona.PERSONA_FISICA);
        return clienteDto;
    }

    public CuentaDto getCuentaDto(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setBalance(1000);
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("Pesos");
        cuentaDto.setDniTitular(43046272);
        return cuentaDto;
    }
}

