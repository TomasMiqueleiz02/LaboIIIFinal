package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransaccionDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;
    private final TransferenciaService transferenciaService;
    private final CuentaValidator cuentaValidator;

    @Autowired
    public CuentaController(CuentaService cuentaService,TransferenciaService transferenciaService, CuentaValidator cuentaValidator) {
        this.cuentaService = cuentaService;
        this.transferenciaService=transferenciaService;
        this.cuentaValidator = cuentaValidator;
    }

    @PostMapping("/alta")
    public ResponseEntity<CuentaDto> darDeAltaCuenta(@RequestBody CuentaDto cuentaDto)throws CuentaAlreadyExistsException {
            cuentaValidator.validate(cuentaDto);
            CuentaDto nuevaCuentaDto = cuentaService.darDeAltaCuenta(cuentaDto);
            return new ResponseEntity<>(nuevaCuentaDto, HttpStatus.CREATED);

        }
    @GetMapping("/{cuentaId}/transacciones")
    public ResponseEntity<Map<String, Object>> obtenerHistorialTransacciones(@PathVariable long cuentaId) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<TransaccionDto> transacciones = transferenciaService.obtenerHistorialTransacciones(cuentaId);

            response.put("numeroCuenta", cuentaId);
            response.put("transacciones", transacciones);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CuentaNoEncontradaException e) {
            response.put("error", "Cuenta no encontrada");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("error", "Error al obtener el historial de transacciones");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<?> buscarCuentaPorNumero(@PathVariable long numeroCuenta) {
        try {
            CuentaDto cuentaDto = cuentaService.buscarCuentaPorNumero(numeroCuenta);
            if (cuentaDto != null) {
                return new ResponseEntity<>(cuentaDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al buscar la cuenta", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/obtener")
    public ResponseEntity<List<CuentaDto>> obtenerTodasLasCuentas() {
        try {
            List<CuentaDto> cuentasDto = cuentaService.obtenerTodasLasCuentas();
            return new ResponseEntity<>(cuentasDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable long numeroCuenta) {
        try {
            cuentaService.eliminarCuenta(numeroCuenta);
            return new ResponseEntity<>("Cuenta eliminada con éxito", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la cuenta", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
