package com.example.proyecto.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Entity
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id autoincremental
    private Long id;
    private String tipo;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public List<String> ColumnasMeses;
    private List<String> Filastipo;

    private List<Double> vueltas1010;
    private List<Double> vueltas1515;
    private List<Double> vueltas2020;

    private List<Double> personas1a2;
    private List<Double> personas3a5;
    private List<Double> personas6a10;
    private List<Double> personas11a15;

    private List<Double> totalesFilas;



    public Reporte() {
    }

    public Reporte(String tipo, LocalDateTime fechaCreacion, LocalDate fechaInicio, LocalDate fechaFin, List<String> columnasMeses, List<String> filastipo, List<Double> vueltas1010, List<Double> vueltas1515, List<Double> vueltas2020, List<Double> totalesFilas) {
        this.tipo = tipo;
        this.fechaCreacion = fechaCreacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.ColumnasMeses = columnasMeses;
        this.Filastipo = filastipo;
        this.vueltas1010 = vueltas1010;
        this.vueltas1515 = vueltas1515;
        this.vueltas2020 = vueltas2020;
        this.totalesFilas = totalesFilas;
    }

    public Reporte(String tipo, LocalDateTime fechaCreacion, LocalDate fechaInicio, LocalDate fechaFin, List<String> columnasMeses, List<String> filastipo, List<Double> personas1a2, List<Double> personas3a5, List<Double> personas6a10, List<Double> personas11a15, List<Double> totalesFilas) {
        this.tipo = tipo;
        this.fechaCreacion = fechaCreacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.ColumnasMeses = columnasMeses;
        this.Filastipo = filastipo;
        this.personas1a2 = personas1a2;
        this.personas3a5 = personas3a5;
        this.personas6a10 = personas6a10;
        this.personas11a15 = personas11a15;
        this.totalesFilas = totalesFilas;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public List<String> getColumnasMeses() {
        return ColumnasMeses;
    }

    public List<String> getFilastipo() {
        return Filastipo;
    }

    public List<Double> getVueltas1010() {
        return vueltas1010;
    }

    public List<Double> getVueltas1515() {
        return vueltas1515;
    }

    public List<Double> getVueltas2020() {
        return vueltas2020;
    }

    public List<Double> getPersonas1a2() {
        return personas1a2;
    }

    public List<Double> getPersonas3a5() {
        return personas3a5;
    }

    public List<Double> getPersonas6a10() {
        return personas6a10;
    }

    public List<Double> getPersonas11a15() {
        return personas11a15;
    }

    public List<Double> getTotalesFilas() {
        return totalesFilas;
    }
}
