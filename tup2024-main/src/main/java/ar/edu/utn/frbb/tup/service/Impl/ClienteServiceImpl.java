package ar.edu.utn.frbb.tup.service.Impl;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteDao clienteDao;

    @Autowired
    public ClienteServiceImpl(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    @Override
    public Cliente darDeAltaCliente(Cliente cliente) {
        // Verifica que el cliente no sea null
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no puede ser null");
        }
        return clienteDao.saveCliente(cliente);
    }

    @Override
    public Cliente agregarCuenta(long dni, Cuenta cuenta) {
        Cliente cliente = clienteDao.findCliente(dni);
        if (cliente != null) {
            cliente.agregarCuenta(cuenta);
            clienteDao.updateCliente(cliente);
        }
        return cliente;
    }

    @Override
    public Cliente buscarClientePorDni(long dni) throws ClienteNoEncontradoException {
        // Verifica que el DNI no sea null y existe en la base de datos
        Cliente cliente = clienteDao.findCliente(dni);
        if (cliente == null) {
            throw new ClienteNoEncontradoException("Cliente no encontrado");
        }
        return cliente;
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteDao.findAll();
    }

    @Override
    public Cliente actualizarCliente(Cliente cliente) {
        // Verifica que el cliente no sea null
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no puede ser null");
        }
        return clienteDao.updateCliente(cliente);
    }

    @Override
    public void eliminarCliente(long dni) {
        clienteDao.deleteCliente(dni);
    }
}

