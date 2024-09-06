package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoEncontradoException;

import java.util.List;

public interface ClienteService {
    Cliente darDeAltaCliente(Cliente cliente);

    Cliente agregarCuenta(long dni, Cuenta cuenta);

    Cliente buscarClientePorDni(long dni)throws ClienteNoEncontradoException;

    List<Cliente> obtenerTodosLosClientes();

    Cliente actualizarCliente(Cliente cliente);

    void eliminarCliente(long dni);
}

