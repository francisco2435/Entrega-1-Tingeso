package com.example.proyecto.Service;

import com.example.proyecto.Entity.Reporte;
import com.example.proyecto.Entity.Reserva;
import com.example.proyecto.Repository.ReporteRepositorio;
import com.example.proyecto.Repository.ReservaRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReporteServicioTest {

    private ReporteRepositorio reporteRepositorio;
    private ReservaRepositorio reservaRepositorio;
    private ReporteServicio reporteServicio;

    @BeforeEach
    void setUp() {
        reporteRepositorio = mock(ReporteRepositorio.class);
        reservaRepositorio = mock(ReservaRepositorio.class);
        reporteServicio = new ReporteServicio(reporteRepositorio, reservaRepositorio);
    }

    @Test
    void whenCrearReporteTipoPersonas_thenReporteConPersonas() {
        // Given
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 3, 31);

        when(reservaRepositorio.findByFechaReservaBetween(any(), any())).thenReturn(Collections.emptyList());
        when(reporteRepositorio.save(any(Reporte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Reporte reporte = reporteServicio.crearReporte("Número de personas", fechaInicio, fechaFin);

        // Then
        assertThat(reporte.getTipo()).isEqualTo("Número de personas");
        assertThat(reporte.getFilastipo()).contains("1-2 personas", "3-5 personas", "6-10 personas", "11-15 personas", "Total");
        assertThat(reporte.getColumnasMeses()).contains("Enero 2024", "Febrero 2024", "Marzo 2024", "Total");
    }

    @Test
    void whenCrearReporteTipoVueltas_thenReporteConVueltas() {
        // Given
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 1, 31);

        when(reservaRepositorio.findByFechaReservaBetween(any(), any())).thenReturn(Collections.emptyList());
        when(reporteRepositorio.save(any(Reporte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Reporte reporte = reporteServicio.crearReporte("Número de vueltas", fechaInicio, fechaFin);

        // Then
        assertThat(reporte.getTipo()).isEqualTo("Número de vueltas");
        assertThat(reporte.getFilastipo()).contains("10 vueltas o máx 10 min", "15 vueltas o máx 15 min", "20 vueltas o máx 20 min", "Total");
        assertThat(reporte.getColumnasMeses()).contains("Enero 2024", "Total");
    }

    @Test
    void whenFechaInicioAfterFechaFin_thenThrowsException() {
        // Given
        LocalDate fechaInicio = LocalDate.of(2024, 4, 1);
        LocalDate fechaFin = LocalDate.of(2024, 3, 1);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> reporteServicio.crearReporte("Número de personas", fechaInicio, fechaFin));
    }

    @Test
    void whenObtenerMesesEntre_thenCorrectMonths() {
        // Given
        LocalDate fechaInicio = LocalDate.of(2024, 1, 15);
        LocalDate fechaFin = LocalDate.of(2024, 3, 10);

        // When
        List<String> meses = reporteServicio.obtenerMesesEntre(fechaInicio, fechaFin);

        // Then
        assertThat(meses).containsExactly("Enero 2024", "Febrero 2024", "Marzo 2024", "Total");
    }

    @Test
    void whenCalculoTotalesNum_thenReturnCorrectList() {
        // Given
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 1, 31);

        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDate.of(2024, 1, 15));
        reserva.setNumVueltas(10);
        reserva.setTiempoMax(5);
        reserva.setMontoTotalConIva(1000.0);

        when(reservaRepositorio.findByFechaReservaBetween(any(), any())).thenReturn(List.of(reserva));

        // When
        List<Double> totales = reporteServicio.calculoTotalesNum(fechaInicio, fechaFin, 10);

        // Then
        assertThat(totales).hasSize(2); // 1 mes + total
        assertThat(totales.get(0)).isEqualTo(1000.0);
        assertThat(totales.get(1)).isEqualTo(1000.0);
    }

    @Test
    void whenCalculoTotalesPersonas_thenReturnCorrectList() {
        // Given
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 1, 31);

        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDate.of(2024, 1, 15));
        reserva.setCantidadPersonas(3);
        reserva.setMontoTotalConIva(2000.0);

        when(reservaRepositorio.findByFechaReservaBetween(any(), any())).thenReturn(List.of(reserva));

        // When
        List<Double> totales = reporteServicio.calculoTotalesPersonas(fechaInicio, fechaFin, 3, 5);

        // Then
        assertThat(totales).hasSize(2); // 1 mes + total
        assertThat(totales.get(0)).isEqualTo(2000.0);
        assertThat(totales.get(1)).isEqualTo(2000.0);
    }

    @Test
    void whenFechaInicioIsAfterFechaFin_thenReturnNull() {
        // Given
        LocalDate fechaInicio = LocalDate.of(2025, 5, 15); // Fecha de inicio posterior
        LocalDate fechaFin = LocalDate.of(2025, 5, 10); // Fecha de fin anterior

        // When
        List<Double> result = reporteServicio.calculoTotalesNum(fechaInicio, fechaFin, 5);

        // Then
        assertThat(result).isNull();  // Verifica que el resultado es null
    }

    @Test
    void whenFechaInicioIsAfterFechaFin2_thenReturnNull() {
        // Given
        LocalDate fechaInicio = LocalDate.of(2025, 5, 15); // Fecha de inicio posterior
        LocalDate fechaFin = LocalDate.of(2025, 5, 10); // Fecha de fin anterior
        int num1 = 3;
        int num2 = 5;

        // When
        List<Double> result = reporteServicio.calculoTotalesPersonas(fechaInicio, fechaFin, num1, num2);

        // Then
        assertThat(result).isNull();  // Verifica que el resultado es null
    }


}
