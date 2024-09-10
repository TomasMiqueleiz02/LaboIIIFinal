package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoEncontradoException;

import java.util.List;

public interface ClienteService {
    ClienteDto darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException, ClienteMenorDeEdadException;
    ClienteDto buscarClientePorDni(long dni) throws ClienteNoEncontradoException;
    List<ClienteDto> obtenerTodosLosClientes();
    void eliminarCliente(long dni) throws ClienteNoEncontradoException;
}
