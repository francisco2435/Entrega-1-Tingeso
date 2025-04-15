package com.example.proyecto.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
public class Reserva {

    //Atributos para hacer reserva
    public String rutCliente;
    public String nombreCliente;
    public LocalTime horaInicio;
    public LocalTime horaFin;
    public int tiempoTotal; // en minutos
    public List<String> rutsAmigos;

    // Atributos para hacer comprobante
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id autoincremental
    public Long id;

    public LocalDate fechaReserva;
    public String TipoTarifa;
    public LocalTime horaReserva;
    public int numVueltas;
    public int tiempoMax; // en minutos
    public int cantidadPersonas;
    // nombre de quien hizo la reserva
    public List<String> nombres;
    // tarifa aplicada es igual para todos
    public List<String> nombreDescuentoTamanoGrupo;
    public List<Double> valorDescuentoTamanoGrupo;
    public List<String> nombreDescuentoEspeciales; //por ser cliente frecuente o promociones especiales
    public List<Double> valorDescuentoEspeciales;
    public double montoTotal;
    public double valorIva;
    public double montoTotalConIva;

    public Reserva() {
    }

    public Reserva(String rutCliente, String nombreCliente, LocalTime horaInicio, LocalTime horaFin,
                   int tiempoTotal, List<String> rutsAmigos, LocalDate fechaReserva, LocalTime horaReserva, int numVueltas,
                   int tiempoMax, int cantidadPersonas, List<String> nombres, List<String> nombreDescuentoTamanoGrupo,
                   List<Double> valorDescuentoTamanoGrupo, List<String> nombreDescuentoEspeciales,
                   List<Double> valorDescuentoEspeciales, double montoTotal, double valorIva, double montoTotalConIva, String TipoTarifa) {
        this.rutCliente = rutCliente;
        this.nombreCliente = nombreCliente;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tiempoTotal = tiempoTotal;
        this.rutsAmigos = rutsAmigos;
        this.fechaReserva = fechaReserva;
        this.horaReserva = horaReserva;
        this.numVueltas = numVueltas;
        this.tiempoMax = tiempoMax;
        this.cantidadPersonas = cantidadPersonas;
        this.nombres = nombres;
        this.nombreDescuentoTamanoGrupo = nombreDescuentoTamanoGrupo;
        this.valorDescuentoTamanoGrupo = valorDescuentoTamanoGrupo;
        this.nombreDescuentoEspeciales = nombreDescuentoEspeciales;
        this.valorDescuentoEspeciales = valorDescuentoEspeciales;
        this.montoTotal = montoTotal;
        this.valorIva = valorIva;
        this.montoTotalConIva = montoTotalConIva;
        this.TipoTarifa = TipoTarifa;
    }

}
