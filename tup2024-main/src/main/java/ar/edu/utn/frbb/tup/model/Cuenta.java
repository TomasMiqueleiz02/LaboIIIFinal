package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Cuenta {
    private long numeroCuenta;
    private TipoCuenta tipoCuenta;
    private TipoMoneda moneda;
    private double balance;
    private LocalDateTime fechaCreacion;
    private long dniTitular;
    private final List<Transaccion> historialTransacciones = new ArrayList<>();

    @JsonBackReference
    private Cliente titular;

    public Cuenta() {
        Random random = new Random();
        int lowerBound = 1000000;
        int upperBound = 9000000;
        this.numeroCuenta = lowerBound + random.nextInt(upperBound);
        this.fechaCreacion = LocalDateTime.now();
        this.balance = 0;

    }

    public Cuenta(CuentaDto cuentaDto) {
        this.numeroCuenta = cuentaDto.getNumeroCuenta() != 0 ? cuentaDto.getNumeroCuenta() : generarNumeroCuentaAleatorio();
        this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta());
        this.moneda = TipoMoneda.fromString(cuentaDto.getMoneda());
        this.balance = cuentaDto.getBalance();
        this.dniTitular = cuentaDto.getdniTitular(); // Asegúrate de que se esté inicializando correctamente
        this.fechaCreacion = LocalDateTime.now();
    }

    private long generarNumeroCuentaAleatorio() {
        return ThreadLocalRandom.current().nextLong(1000000, 9999999);
    }


    // Constructor con parámetros
    public Cuenta(long numeroCuenta, TipoCuenta tipoCuenta, TipoMoneda moneda, double balance,long dniTitular) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.moneda = moneda;
        this.balance = balance;
        this.fechaCreacion = LocalDateTime.now();
        this.dniTitular=dniTitular;
    }

    // Getters y setters
    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    public List<Transaccion> getHistorialTransacciones() {
        return historialTransacciones;
    }

    public void debitar(double monto) throws NoAlcanzaException {
        if (monto > balance) {
            throw new NoAlcanzaException("Saldo insuficiente");
        }
        this.balance -= monto;
    }

    public long getdniTitular() {
        return dniTitular;
    }

    public void setDniTitular(long dniTitular) {
        this.dniTitular = dniTitular;
    }

    public void acreditar(double monto) {
        this.balance += monto;
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "numeroCuenta=" + numeroCuenta +
                ", tipoCuenta=" + tipoCuenta +
                ", moneda=" + moneda +
                ", balance=" + balance +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
