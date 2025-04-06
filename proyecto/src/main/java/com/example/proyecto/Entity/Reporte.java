package com.example.proyecto.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id autoincremental
    public Long id;
    public String tipo;
    public LocalDateTime fechaCreacion;
    public YearMonth MesInicio;
    public YearMonth MesFin;
    public Map<String, Map<YearMonth, Double>> ingresosPorTarifaYMes = new LinkedHashMap<>();
    public Map<String, Double> totalPorTarifa = new LinkedHashMap<>();
    public double totalGeneral = 0.0;
}
