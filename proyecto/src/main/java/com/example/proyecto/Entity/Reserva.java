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
    private String rutCliente;
    private String nombreCliente;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int tiempoTotal; // en minutos
    private List<String> rutsAmigos;

    // Atributos para hacer comprobante
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id autoincremental
    private Long id;

    private LocalDate fechaReserva;
    private String TipoTarifa;
    private LocalTime horaReserva;
    private int numVueltas;
    private int tiempoMax; // en minutos
    private int cantidadPersonas;

    private List<String> nombres;
    // tarifa aplicada es igual para todos
    private List<String> nombreDescuentoTamanoGrupo;
    private List<Double> valorDescuentoTamanoGrupo;
    private List<String> nombreDescuentoEspeciales; //por ser cliente frecuente o promociones especiales
    private List<Double> valorDescuentoEspeciales;
    private double montoTotal;
    private double valorIva;
    private double montoTotalConIva;

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

    public String getRutCliente() {
        return rutCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public int getTiempoTotal() {
        return tiempoTotal;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public List<String> getRutsAmigos() {
        return rutsAmigos;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public String getTipoTarifa() {
        return TipoTarifa;
    }

    public LocalTime getHoraReserva() {
        return horaReserva;
    }

    public int getTiempoMax() {
        return tiempoMax;
    }

    public int getNumVueltas() {
        return numVueltas;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public List<String> getNombres() {
        return nombres;
    }

    public List<String> getNombreDescuentoTamanoGrupo() {
        return nombreDescuentoTamanoGrupo;
    }

    public List<Double> getValorDescuentoTamanoGrupo() {
        return valorDescuentoTamanoGrupo;
    }

    public List<String> getNombreDescuentoEspeciales() {
        return nombreDescuentoEspeciales;
    }

    public List<Double> getValorDescuentoEspeciales() {
        return valorDescuentoEspeciales;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public double getValorIva() {
        return valorIva;
    }

    public double getMontoTotalConIva() {
        return montoTotalConIva;
    }
}
