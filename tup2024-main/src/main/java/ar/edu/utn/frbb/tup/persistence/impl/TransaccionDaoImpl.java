package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.model.Transaccion;
import ar.edu.utn.frbb.tup.persistence.TransaccionDao;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Repository


public class TransaccionDaoImpl implements TransaccionDao {
    private List<Transaccion> transacciones = new ArrayList<>();

    @Override
    public void agregarTransaccion(Transaccion transaccion) {
        transacciones.add(transaccion);
    }

    @Override
    public List<Transaccion> obtenerTransaccionesPorCuenta(long cuentaId) {
        // Filtra las transacciones para obtener solo aquellas que están relacionadas con la cuenta especificada
        return transacciones.stream() // Comienza una secuencia (stream) de objetos de tipo Transaccion de la lista 'transacciones'
                .filter(t -> t.getCuentaOrigen() == cuentaId || t.getCuentaDestino() == cuentaId)// Filtra las transacciones en las que la cuenta de origen o la cuenta de destino coincidan con 'cuentaId'
                .collect(Collectors.toList());// Recolecta las transacciones filtradas en una nueva lista y la devuelve
    }
}

