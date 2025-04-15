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
@Data
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id autoincremental
    public Long id;
    public String tipo;
    public LocalDateTime fechaCreacion;
    public LocalDate fechaInicio;
    public LocalDate fechaFin;

    public List<String> ColumnasMeses;
    public List<String> Filastipo;

    public List<Double> vueltas1010;
    public List<Double> vueltas1515;
    public List<Double> vueltas2020;

    public List<Double> personas1a2;
    public List<Double> personas3a5;
    public List<Double> personas6a10;
    public List<Double> personas11a15;

    public List<Double> totalesFilas;

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
}
