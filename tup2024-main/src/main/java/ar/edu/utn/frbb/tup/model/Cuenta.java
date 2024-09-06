package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.Random;

public class Cuenta {
    private long numeroCuenta;
    private long id;
    private TipoCuenta tipoCuenta;
    private TipoMoneda moneda;
    private double balance;
    LocalDateTime fechaCreacion;

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

    public Cuenta(CuentaDto cuentaDto){
        this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta());
        this.moneda = TipoMoneda.fromString(cuentaDto.getMoneda());
        this.fechaCreacion = LocalDateTime.now();
        this.balance = cuentaDto.getBalance();
        Random random = new Random();
        int lowerBound = 1000000;
        int upperBound = 9000000;
        this.numeroCuenta = lowerBound + random.nextInt(upperBound); //genera numero de 7 digitos
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void debitar(double monto) throws NoAlcanzaException {
        if (monto > balance) {
            throw new NoAlcanzaException("Saldo insuficiente");
        }
        this.balance -= monto;
    }

    public void acreditar(double monto) {
        this.balance += monto;
    }
    @Override
    public String toString() {
        return "Cuenta{" +
                "id=" + id +
                ", tipoCuenta=" + tipoCuenta +
                ", moneda=" + moneda +
                ", balance=" + balance +
                '}';
    }
}
