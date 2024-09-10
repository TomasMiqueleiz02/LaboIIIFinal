package ar.edu.utn.frbb.tup.controller.dto;

import ar.edu.utn.frbb.tup.model.TipoMoneda;

import java.time.LocalDateTime;
import java.util.List;

public class TransaccionDto {

    private long cuentaOrigen;
    private long cuentaDestino;
    private double monto;
    private String tipo; // Ej: "Débito" o "Crédito"
    private String descripcionBreve;
    private String moneda; // Ej: "PESOS" o "DÓLARES"
    private LocalDateTime fecha; // Fecha y hora de la transacción

    // Constructor vacío
    public TransaccionDto() {}

    // Constructor con parámetros
    public TransaccionDto(long cuentaOrigen, long cuentaDestino, double monto, String tipo,
                          String descripcionBreve, String moneda, LocalDateTime fecha) {
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
        this.tipo = tipo;
        this.descripcionBreve = descripcionBreve;
        this.moneda = moneda;
        this.fecha = fecha;
    }

    // Constructor basado en TransferenciaDto




    public long getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public long getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(long cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
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

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

}


