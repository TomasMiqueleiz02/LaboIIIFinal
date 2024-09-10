package ar.edu.utn.frbb.tup.service.Impl;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteDao clienteDao;

    @Override
    public ClienteDto darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {
        Cliente cliente = convertirDtoAEntidad(clienteDto);
        // Implementar lógica para dar de alta el cliente
        Cliente nuevoCliente = clienteDao.saveCliente(cliente);
        return convertirEntidadADto(nuevoCliente);
    }

    @Override
    public ClienteDto buscarClientePorDni(long dni) throws ClienteNoEncontradoException {
        Cliente cliente = clienteDao.findCliente(dni);
        if (cliente == null) {
            throw new ClienteNoEncontradoException("Cliente no encontrado");
        }
        return convertirEntidadADto(cliente);
    }

    @Override
    public List<ClienteDto> obtenerTodosLosClientes() {
        // Obtiene todos los clientes almacenados en el repositorio
        List<Cliente> clientes = clienteDao.findAll();
        // Convierte cada Cliente en un ClienteDto y recolecta los resultados en una nueva lista
        return clientes.stream()
                .map(this::convertirEntidadADto)// Convierte cada objeto Cliente en un ClienteDto usando el método 'convertirEntidadADto'
                .collect(Collectors.toList());// Recolecta los objetos ClienteDto en una nueva lista y la devuelve
    }

    @Override
    public void eliminarCliente(long dni) throws ClienteNoEncontradoException {
        Cliente cliente = clienteDao.findCliente(dni);
        if (cliente == null) {
            throw new ClienteNoEncontradoException("El cliente no se ha encontrado");
        }
        clienteDao.deleteCliente(dni);
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
