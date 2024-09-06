package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private long dni;
    private String nombre;
    private String apellido;
    private TipoPersona tipoPersona;
    private LocalDateTime fechaAlta;

    @JsonManagedReference
    private List<Cuenta> cuentas;

    public Cliente(long dni, String nombre, String apellido) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoPersona = TipoPersona.PERSONA_FISICA; // Valor predeterminado de cualquier persona
        this.fechaAlta = LocalDateTime.now();
        this.cuentas = new ArrayList<>();
    }

    public Cliente(ClienteDto clienteDto) {
        this.dni = clienteDto.getDni();
        this.nombre = clienteDto.getNombre();
        this.apellido = clienteDto.getApellido();
        this.tipoPersona = TipoPersona.fromString(clienteDto.getTipoPersona());
        this.fechaAlta = LocalDateTime.now();
        this.cuentas = new ArrayList<>();
    }

    public long getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void agregarCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
        cuenta.setTitular(this); // Para mantener la relación inversa
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "dni=" + dni +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", tipoPersona=" + tipoPersona +
                ", fechaAlta=" + fechaAlta +
                ", cuentas=" + cuentas +
                '}';
    }
}
