package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping ("/alta")
    public ResponseEntity<?> darDeAltaCliente(@RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.darDeAltaCliente(cliente);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al dar de alta el cliente", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{dni}")
    public ResponseEntity<?> buscarClientePorDni(@PathVariable long dni) {
        try {
            Cliente cliente = clienteService.buscarClientePorDni(dni);
            if (cliente != null) {
                return new ResponseEntity<>(cliente, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al buscar el cliente", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/obtener")
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes() {
        try {
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            return new ResponseEntity<>(clientes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<?> eliminarCliente(@PathVariable long dni) {
        try {
            clienteService.eliminarCliente(dni);
            return new ResponseEntity<>("Cliente eliminado con éxito", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el cliente", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
