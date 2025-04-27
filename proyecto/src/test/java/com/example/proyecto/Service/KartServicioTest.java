package com.example.proyecto.Service;

import com.example.proyecto.Entity.Kart;
import com.example.proyecto.Repository.KartRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class KartServicioTest {

    private KartRepositorio kartRepositorio;
    private KartServicio kartServicio;

    @BeforeEach
    void setUp() {
        kartRepositorio = Mockito.mock(KartRepositorio.class);
        kartServicio = new KartServicio(kartRepositorio); // PASO el mock en el constructor
    }

    @Test
    void whenNuevoKartWithUniqueCode_thenKartSaved() {
        // Given
        Kart kart = new Kart("K001", "ModeloX", "disponible");
        when(kartRepositorio.findByCodigo("K001")).thenReturn(null);
        when(kartRepositorio.save(any(Kart.class))).thenReturn(kart);

        // When
        Kart result = kartServicio.nuevoKart("K001", "ModeloX", "disponible");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCodigo()).isEqualTo("K001");
        verify(kartRepositorio).save(any(Kart.class));
    }

    @Test
    void whenNuevoKartWithExistingCode_thenReturnNull() {
        // Given
        Kart existingKart = new Kart("K002", "ModeloY", "ocupado");
        when(kartRepositorio.findByCodigo("K002")).thenReturn(existingKart);

        // When
        Kart result = kartServicio.nuevoKart("K002", "ModeloY", "ocupado");

        // Then
        assertThat(result).isNull();
        verify(kartRepositorio, never()).save(any(Kart.class));
    }

    @Test
    void whenObtenerKartsEstadoWithValidEstado_thenReturnList() {
        // Given
        Kart kart = new Kart("K003", "ModeloZ", "disponible");
        when(kartRepositorio.findByEstado("disponible")).thenReturn(List.of(kart));

        // When
        List<Kart> karts = kartServicio.obtenerKartsEstado("disponible");

        // Then
        assertThat(karts).isNotEmpty();
        assertThat(karts.get(0).getEstado()).isEqualTo("disponible");
    }

    @Test
    void whenObtenerKartsEstadoWithEmptyEstado_thenThrowException() {
        // When / Then
        assertThatThrownBy(() -> kartServicio.obtenerKartsEstado(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El estado no puede ser nulo o vacío.");
    }

    @Test
    void whenObtenerKartsEstadoWithNullEstado_thenThrowException() {
        // When / Then
        assertThatThrownBy(() -> kartServicio.obtenerKartsEstado(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El estado no puede ser nulo o vacío.");
    }

    @Test
    void whenCambiarEstadoKartExists_thenEstadoUpdated() {
        // Given
        Kart kart = new Kart("K004", "ModeloA", "ocupado");
        when(kartRepositorio.findByCodigo("K004")).thenReturn(kart);
        when(kartRepositorio.save(any(Kart.class))).thenReturn(kart);

        // When
        kartServicio.cambiarEstado("K004", "mantenimiento");

        // Then
        assertThat(kart.getEstado()).isEqualTo("mantenimiento");
        verify(kartRepositorio).save(kart);
    }

    @Test
    void whenCambiarEstadoKartDoesNotExist_thenThrowException() {
        // Given
        when(kartRepositorio.findByCodigo("K005")).thenReturn(null);

        // When / Then
        assertThatThrownBy(() -> kartServicio.cambiarEstado("K005", "ocupado"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Kart no encontrado con código: K005");
    }
}
