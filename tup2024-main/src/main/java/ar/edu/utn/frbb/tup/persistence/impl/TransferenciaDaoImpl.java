package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.persistence.TransferenciaDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository

public class TransferenciaDaoImpl implements TransferenciaDao {

    private final Map<Long, List<Transferencia>> repositorioTransferencias = new HashMap<>();

    @Override
    public void guardarTransferencia(Transferencia transferencia) {
        repositorioTransferencias
                .computeIfAbsent(transferencia.getCuentaOrigen(), k -> new ArrayList<>())
                .add(transferencia);
    }

    @Override
    public List<Transferencia> findTransfersByID(long id) {
        return repositorioTransferencias.getOrDefault(id, new ArrayList<>());
    }

    @Override
    public List<Transferencia> findAllTransfers() {
        List<Transferencia> todasLasTransferencias = new ArrayList<>();
        for (List<Transferencia> transferencias : repositorioTransferencias.values()) {
            todasLasTransferencias.addAll(transferencias);
        }
        return todasLasTransferencias;
    }



    // Método para limpiar el repositorio
    public void clearRepository() {
        repositorioTransferencias.clear();
    }
}


