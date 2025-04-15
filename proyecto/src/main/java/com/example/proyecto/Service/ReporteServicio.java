package com.example.proyecto.Service;

import com.example.proyecto.Entity.Reporte;
import com.example.proyecto.Entity.Reserva;
import com.example.proyecto.Repository.ReporteRepositorio;
import com.example.proyecto.Repository.ReservaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ReporteServicio {
    @Autowired
    private ReporteRepositorio reporteRepositorio;

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    //Crear reporte según el tipo escogido número de vueltas, tiempo máximo o n° personas
    public Reporte crearReporte(String tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        List<String> filas = new ArrayList<>();
        List<String> meses = obtenerMesesEntre(fechaInicio, fechaFin);
        List<Double> totales = new ArrayList<>();
        double total = 0.0;
        LocalDateTime fechaCreacion = LocalDateTime.now();
        List<Double> vueltas1010;
        List<Double> vueltas1515;
        List<Double> vueltas2020;
        List<Double> personas1a2;
        List<Double> personas3a5;
        List<Double> personas6a10;
        List<Double> personas11a15;

        if(meses == null){
            System.out.println("La fecha fin debe ser mayor que la fecha inicio");
            return null;
        }

        filas.add(tipo);
        if(tipo.equals("Número de personas")) {
            filas.add("1-2 personas");
            filas.add("3-5 personas");
            filas.add("6-10 personas");
            filas.add("11-15 personas");
        } else{
            filas.add("10 vueltas o máx 10 min");
            filas.add("15 vueltas o máx 15 min");
            filas.add("20 vueltas o máx 20 min");
        }
        filas.add("Total");

        if(!tipo.equals("Número de personas")) {
            vueltas1010 = calculoTotalesNum(fechaInicio, fechaFin, 10);
            vueltas1515 = calculoTotalesNum(fechaInicio, fechaFin, 15);
            vueltas2020 = calculoTotalesNum(fechaInicio, fechaFin, 20);

            for (int i = 0; i < vueltas1010.size(); i++) {
                total = vueltas1010.get(i) + vueltas1515.get(i) + vueltas2020.get(i);
                totales.add(total);
            }

            Reporte reporte = new Reporte(tipo, fechaCreacion, fechaInicio, fechaFin, meses, filas, vueltas1010, vueltas1515, vueltas2020, totales);
            return reporteRepositorio.save(reporte);
        }
        //Separar las funciones, mantener la actual para num vueltas/tiempomax, pero modificar para num personas para que verifique en un rango de personas
        personas1a2 = calculoTotalesPersonas(fechaInicio, fechaFin, 1, 2);
        personas3a5 = calculoTotalesPersonas(fechaInicio, fechaFin, 3, 5);
        personas6a10 = calculoTotalesPersonas(fechaInicio, fechaFin, 6, 10);
        personas11a15 = calculoTotalesPersonas(fechaInicio, fechaFin, 11, 15);
        for (int i = 0; i < personas1a2.size(); i++) {
            total = personas1a2.get(i) + personas3a5.get(i) + personas6a10.get(i) + personas11a15.get(i);
            totales.add(total);
        }
        System.out.println(tipo);
        System.out.println(fechaCreacion);
        System.out.println(fechaInicio);
        System.out.println(fechaFin);
        System.out.println(meses);
        System.out.println(filas);
        System.out.println(personas1a2);
        System.out.println(personas3a5);
        System.out.println(personas6a10);
        System.out.println(personas11a15);
        System.out.println(totales);
        Reporte reporte = new Reporte(tipo, fechaCreacion, fechaInicio, fechaFin, meses, filas, personas1a2, personas3a5, personas6a10, personas11a15, totales);
        return reporteRepositorio.save(reporte);
   }

    public List<String> obtenerMesesEntre(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            return null;
        }

        YearMonth inicio = YearMonth.from(fechaInicio);
        YearMonth fin = YearMonth.from(fechaFin);
        List<String> meses = new ArrayList<>();
        YearMonth actual = inicio;

        while (!actual.isAfter(fin)) {
            String nombreMes = actual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            nombreMes = nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1); // Capitaliza
            meses.add(nombreMes + " " + actual.getYear());
            actual = actual.plusMonths(1);
        }

        meses.add("Total");

        return meses;
    }

    public List<Double> calculoTotalesNum(LocalDate fechaInicio, LocalDate fechaFin, int num) {
        if (fechaInicio.isAfter(fechaFin)) {
            return null;
        }

        List<Double> totales = new ArrayList<>();

        // Obtener los meses entre las fechas
        List<YearMonth> meses = new ArrayList<>();
        YearMonth inicio = YearMonth.from(fechaInicio);
        YearMonth fin = YearMonth.from(fechaFin);

        while (!inicio.isAfter(fin)) {
            meses.add(inicio);
            inicio = inicio.plusMonths(1);
        }

        // Inicializar lista con ceros
        for (int i = 0; i < meses.size(); i++) {
            totales.add(0.0);
        }

        // Obtener reservas en el rango
        List<Reserva> reservas = reservaRepositorio.findByFechaReservaBetween(fechaInicio, fechaFin);

        for (Reserva reserva : reservas) {
            if (reserva.numVueltas == num || reserva.tiempoMax == num) {
                LocalDate fechaReserva = reserva.fechaReserva;
                YearMonth mesReserva = YearMonth.from(fechaReserva);
                int index = meses.indexOf(mesReserva);
                if (index != -1) {
                    double monto = reserva.montoTotalConIva;
                    totales.set(index, totales.get(index) + monto);
                }
            }
        }

        // Calcular total general
        double totalGeneral = totales.stream().mapToDouble(Double::doubleValue).sum();
        totales.add(totalGeneral);

        return totales;
    }

    public List<Double> calculoTotalesPersonas(LocalDate fechaInicio, LocalDate fechaFin, int num1, int num2) {
        if (fechaInicio.isAfter(fechaFin)) {
            return null;
        }

        List<Double> totales = new ArrayList<>();

        // Obtener los meses entre las fechas
        List<YearMonth> meses = new ArrayList<>();
        YearMonth inicio = YearMonth.from(fechaInicio);
        YearMonth fin = YearMonth.from(fechaFin);

        while (!inicio.isAfter(fin)) {
            meses.add(inicio);
            inicio = inicio.plusMonths(1);
        }

        // Inicializar lista con ceros
        for (int i = 0; i < meses.size(); i++) {
            totales.add(0.0);
        }

        // Obtener reservas en el rango
        List<Reserva> reservas = reservaRepositorio.findByFechaReservaBetween(fechaInicio, fechaFin);

        for (Reserva reserva : reservas) {
            if (reserva.cantidadPersonas >= num1 && reserva.cantidadPersonas <= num2) {
                LocalDate fechaReserva = reserva.fechaReserva;
                YearMonth mesReserva = YearMonth.from(fechaReserva);
                int index = meses.indexOf(mesReserva);
                if (index != -1) {
                    double monto = reserva.montoTotalConIva;
                    totales.set(index, totales.get(index) + monto);
                }
            }
        }

        // Calcular total general
        double totalGeneral = totales.stream().mapToDouble(Double::doubleValue).sum();
        totales.add(totalGeneral);

        return totales;
    }

}
