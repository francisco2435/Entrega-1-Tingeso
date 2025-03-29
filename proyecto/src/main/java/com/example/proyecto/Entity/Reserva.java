package com.example.proyecto.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter @Setter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id autoincremental
    private Long id;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    public Usuario cliente;
    public LocalDate fecha;
    public LocalTime horaInicio;
    public int tiempoMax; // en minutos
    public LocalTime horaFin;
    public int tiempoTotal; // en minutos
    public int numVueltas;
    public int cantidadPersonas;
    public List<Long> IdsAmigos;
    public double montoTotal;

    @OneToMany
    @JoinColumn(name = "kart_id")
    public List<Kart> kartsAsignados;
    public String estado;

    public Reserva() {
    }

    public Reserva(Usuario cliente, LocalDate fecha, LocalTime horaInicio, int tiempoMax, LocalTime horaFin, int tiempoTotal, int numVueltas, int cantidadPersonas, List<Long> idsAmigos, double montoTotal, List<Kart> kartsAsignados, String estado) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.tiempoMax = tiempoMax;
        this.horaFin = horaFin;
        this.tiempoTotal = tiempoTotal;
        this.numVueltas = numVueltas;
        this.cantidadPersonas = cantidadPersonas;
        IdsAmigos = idsAmigos;
        this.montoTotal = montoTotal;
        this.kartsAsignados = kartsAsignados;
        this.estado = estado;
    }
}
