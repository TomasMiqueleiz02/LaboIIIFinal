package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;

import java.time.LocalDateTime;

public class Transferencia {

    private long cuentaOrigen;
    private long cuentaDestino;
    private double monto;
    private String tipo; // CREDITO, DEBITO, TRANSFERENCIA_ENTRANTE, etc.
    private String descripcionBreve;
    private TipoMoneda moneda;
    private LocalDateTime fecha;

    public Transferencia() {
        this.fecha = LocalDateTime.now();
    }

    public Transferencia(TransferenciaDto transferenciaDto) {
        this.cuentaOrigen = transferenciaDto.getCuentaOrigen();
        this.cuentaDestino = transferenciaDto.getCuentaDestino();
        this.monto = transferenciaDto.getMonto();
        this.tipo=transferenciaDto.getTipo();
        this.descripcionBreve=transferenciaDto.getDescripcionBreve();
        this.moneda = TipoMoneda.fromString(transferenciaDto.getMoneda());
        this.fecha = LocalDateTime.now();
    }

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
}