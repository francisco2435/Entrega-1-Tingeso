package com.example.proyecto.Service;

import com.example.proyecto.Entity.Reporte;
import com.example.proyecto.Entity.Reserva;
import com.example.proyecto.Repository.ReporteRepositorio;
import com.example.proyecto.Repository.ReservaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteServicio {
    @Autowired
    ReporteRepositorio reporteRepositorio;

    @Autowired
    ReservaRepositorio reservaRepositorio;

    //Crear reporte según el tipo escogido número de vueltas, tiempo máximo o n° personas
    //public Reporte crearReporte(String tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        //Map<String, Map<YearMonth, Double>> ingresosPorTarifaYMes = new LinkedHashMap<>();
        //Map<String, Double> totalPorTarifa = new LinkedHashMap<>();
        //List<Reserva> reservas = reservaRepositorio.findByFechaReservaBetween(fechaInicio, fechaFin);


    //}

    public String obtenerCategoria(Reserva reserva) {
        int vueltas = reserva.numVueltas;
        int tiempo = reserva.getTiempoMax();

        if (vueltas <= 10 && tiempo <= 10) {
            return "10 vueltas o máx 10 min";
        } else if (vueltas <= 15 && tiempo <= 15) {
            return "15 vueltas o máx 15 min";
        } else {
            return "20 vueltas o máx 20 min";
        }
    }


}
