package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class BanelcoService {

    // 1 de cada 10 veces, podemos simular que la cuentaDestino existe.
    public boolean cuentaExiste(long cuentaDestino){
        Random random = new Random();
        int randomNumber = random.nextInt(10);
        return randomNumber == 0;
    }

    public void acreditar(double monto, long cuentaDestinoNumero){
        return;
    }
}

