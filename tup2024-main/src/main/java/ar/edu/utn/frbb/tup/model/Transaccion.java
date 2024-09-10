package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.dto.TransaccionDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;

public class Transaccion {
    private long cuentaOrigen;
    private long cuentaDestino;
    private double monto;
    private String tipo;
    private String descripcionBreve;
    private String moneda;
    private LocalDateTime fecha;

    // Constructor que recibe un DTO
    public Transaccion(TransaccionDto transaccionDto) {
        this.cuentaOrigen = transaccionDto.getCuentaOrigen();
        this.cuentaDestino = transaccionDto.getCuentaDestino();
        this.monto = transaccionDto.getMonto();
        this.tipo = transaccionDto.getTipo();
        this.descripcionBreve = transaccionDto.getDescripcionBreve();
        this.moneda = transaccionDto.getMoneda();
        this.fecha = transaccionDto.getFecha();

    }

    // Getters and setters
    public long getCuentaOrigen() {
        return cuentaOrigen;
    }
    public long getCuentaDestino() {
        return cuentaDestino;
    }
    public double getMonto() {
        return monto;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getDescripcionBreve() {
        return descripcionBreve;
    }
    public String getMoneda() {
        return moneda;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }


}
