package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.DatoIngresadoInvalidoException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteValidator clienteValidator;

    @Autowired
    public ClienteController(ClienteService clienteService, ClienteValidator clienteValidator) {
        this.clienteService = clienteService;
        this.clienteValidator = clienteValidator;
    }

    @PostMapping("/alta")
    public ResponseEntity<ClienteDto> darDeAltaCliente(@RequestBody ClienteDto clienteDto) throws DatoIngresadoInvalidoException, ClienteAlreadyExistsException, ClienteMenorDeEdadException {
        // Si la validación falla, lanzará DatoIngresadoInvalidoException
        clienteValidator.validate(clienteDto);

        // Si todo va bien, se crea el cliente
        ClienteDto nuevoCliente = clienteService.darDeAltaCliente(clienteDto);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<?> buscarClientePorDni(@PathVariable long dni) {
        try {
            ClienteDto clienteDto = clienteService.buscarClientePorDni(dni);
            return new ResponseEntity<>(clienteDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/obtener")
    public ResponseEntity<List<ClienteDto>> obtenerTodosLosClientes() {
        try {
            List<ClienteDto> clientesDto = clienteService.obtenerTodosLosClientes();
            return new ResponseEntity<>(clientesDto, HttpStatus.OK);
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

