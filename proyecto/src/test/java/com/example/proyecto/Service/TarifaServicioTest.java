package com.example.proyecto.Service;

import com.example.proyecto.Entity.Tarifa;
import com.example.proyecto.Repository.TarifaRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TarifaServicioTest {

    private TarifaRepositorio tarifaRepositorio;
    private TarifaServicio tarifaServicio;

    @BeforeEach
    void setUp() {
        tarifaRepositorio = Mockito.mock(TarifaRepositorio.class);
        tarifaServicio = new TarifaServicio(tarifaRepositorio);
    }

    @Test
    void whenNuevaTarifaWithValidData_thenSaveTarifa() {
        // Given
        Tarifa tarifa = new Tarifa(5, 60, 10000.0, 90, "normal");
        when(tarifaRepositorio.save(any(Tarifa.class))).thenReturn(tarifa);

        // When
        Tarifa result = tarifaServicio.NuevaTarifa(5, 60, 10000.0, 90, "normal");

        // Then
        assertThat(result.getNumeroVueltas()).isEqualTo(5);
        assertThat(result.getTiempoMax()).isEqualTo(60);
        assertThat(result.getPrecio()).isEqualTo(10000.0);
        assertThat(result.getDuracionReserva()).isEqualTo(90);
        assertThat(result.getTipo()).isEqualTo("normal");
    }

    @Test
    void whenNuevaTarifaWithInvalidTipo_thenThrowsException() {
        // Then
        assertThatThrownBy(() ->
                tarifaServicio.NuevaTarifa(5, 60, 10000.0, 90, "vip")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("la tarifa debe ser de tipo normal, dia especial o fin de semana");
    }

    @Test
    void whenNuevaTarifaWithNegativeTiempoMax_thenThrowsException() {
        // Then
        assertThatThrownBy(() ->
                tarifaServicio.NuevaTarifa(5, -10, 10000.0, 90, "normal")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El tiempo maximo permitido debe ser positivo");
    }

    @Test
    void whenNuevaTarifaWithNegativePrecio_thenThrowsException() {
        // Then
        assertThatThrownBy(() ->
                tarifaServicio.NuevaTarifa(5, 60, -10000.0, 90, "normal")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El precio debe ser positivo");
    }

    @Test
    void whenNuevaTarifaWithDuracionReservaLessThanTiempoMax_thenThrowsException() {
        // Then
        assertThatThrownBy(() ->
                tarifaServicio.NuevaTarifa(5, 90, 10000.0, 60, "normal")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La duracion total de la reserva debe ser mayor que el tiempo máximo permitido");
    }

    @Test
    void whenModificarTarifaWithValidData_thenUpdateTarifa() {
        // Given
        Tarifa tarifa = new Tarifa(5, 60, 10000.0, 90, "normal");
        when(tarifaRepositorio.findById(1L)).thenReturn(Optional.of(tarifa));

        // When
        tarifaServicio.modificarTarifa(1L, 6, 70, 12000.0, 100, "fin de semana");

        // Then
        assertThat(tarifa.getNumeroVueltas()).isEqualTo(6);
        assertThat(tarifa.getTiempoMax()).isEqualTo(70);
        assertThat(tarifa.getPrecio()).isEqualTo(12000.0);
        assertThat(tarifa.getDuracionReserva()).isEqualTo(100);
        assertThat(tarifa.getTipo()).isEqualTo("fin de semana");
    }

    @Test
    void whenModificarTarifaWithInvalidTipo_thenThrowsException() {
        // Given
        Tarifa tarifa = new Tarifa(5, 60, 10000.0, 90, "normal");
        when(tarifaRepositorio.findById(1L)).thenReturn(Optional.of(tarifa));

        // Then
        assertThatThrownBy(() ->
                tarifaServicio.modificarTarifa(1L, 6, 70, 12000.0, 100, "vip")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("la tarifa debe ser de tipo normal, dia especial o fin de semana");
    }

    @Test
    void whenObtenerTarifaById_thenReturnTarifa() {
        // Given
        Tarifa tarifa = new Tarifa(5, 60, 10000.0, 90, "normal");
        when(tarifaRepositorio.findById(1L)).thenReturn(Optional.of(tarifa));

        // When
        Tarifa result = tarifaServicio.obtenerTarifa(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTipo()).isEqualTo("normal");
    }

    @Test
    void whenModificarTarifaWithNonExistingTarifa_thenThrowsException() {
        // Given: No existe la tarifa con id 99L
        when(tarifaRepositorio.findById(99L)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() ->
                tarifaServicio.modificarTarifa(99L, 5, 60, 10000.0, 90, "normal")
        ).isInstanceOf(NoSuchElementException.class);  // O IllegalArgumentException si cambias el código
    }

    // Test para crear una tarifa con tipo inválido
    @Test
    void testNuevaTarifaTipoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            tarifaServicio.NuevaTarifa(5, 60, 100.0, 120, "tipoInvalido");
        });
    }

    // Test para obtener todas las tarifas
    @Test
    void testObtenerTodasLasTarifas() {
        Tarifa tarifa1 = new Tarifa(5, 60, 100.0, 120, "normal");
        Tarifa tarifa2 = new Tarifa(10, 120, 200.0, 240, "fin de semana");

        when(tarifaRepositorio.findAll()).thenReturn(List.of(tarifa1, tarifa2));

        List<Tarifa> tarifas = tarifaServicio.ObtenerTodasLasTarifas();

        assertEquals(2, tarifas.size());
        verify(tarifaRepositorio, times(1)).findAll();
    }

    // Test para modificar una tarifa con parámetros válidos
    @Test
    void testModificarTarifa() {
        Long id = 1L;
        int nuevasVueltas = 6;
        int nuevoTiempomax = 90;
        double nuevoPrecio = 120.0;
        int nuevaDuracion = 150;
        String nuevoTipo = "dia especial";

        Tarifa tarifaExistente = new Tarifa(5, 60, 100.0, 120, "normal");

        // Mock de la respuesta del repositorio
        when(tarifaRepositorio.findById(id)).thenReturn(Optional.of(tarifaExistente));
        when(tarifaRepositorio.save(any(Tarifa.class))).thenReturn(tarifaExistente);

        tarifaServicio.modificarTarifa(id, nuevasVueltas, nuevoTiempomax, nuevoPrecio, nuevaDuracion, nuevoTipo);

        verify(tarifaRepositorio, times(1)).save(any(Tarifa.class));
        assertEquals(nuevasVueltas, tarifaExistente.getNumeroVueltas());
        assertEquals(nuevoTiempomax, tarifaExistente.getTiempoMax());
        assertEquals(nuevoPrecio, tarifaExistente.getPrecio());
        assertEquals(nuevaDuracion, tarifaExistente.getDuracionReserva());
        assertEquals(nuevoTipo, tarifaExistente.getTipo());
    }



    // Test para obtener tarifa por id
    @Test
    void testObtenerTarifa() {
        Long id = 1L;
        Tarifa tarifaEsperada = new Tarifa(5, 60, 100.0, 120, "normal");

        when(tarifaRepositorio.findById(id)).thenReturn(Optional.of(tarifaEsperada));

        Tarifa tarifa = tarifaServicio.obtenerTarifa(id);

        assertEquals(tarifaEsperada, tarifa);
        verify(tarifaRepositorio, times(1)).findById(id);
    }

    @Test
    void testModificarTarifa_TipoInvalido() {
        Long id = 1L;
        Tarifa tarifaExistente = new Tarifa(5, 60, 100.0, 120, "normal");

        // Mock de la respuesta del repositorio
        when(tarifaRepositorio.findById(id)).thenReturn(Optional.of(tarifaExistente));

        // Se espera que se lance una excepción cuando el tipo de tarifa no sea válido
        assertThrows(IllegalArgumentException.class, () -> {
            tarifaServicio.modificarTarifa(id, 10, 60, 150.0, 120, "tipoInvalido");
        });
    }
    @Test
    void testModificarTarifa_TiempoMaximoNegativo() {
        Long id = 1L;
        Tarifa tarifaExistente = new Tarifa(5, 60, 100.0, 120, "normal");

        // Mock de la respuesta del repositorio
        when(tarifaRepositorio.findById(id)).thenReturn(Optional.of(tarifaExistente));

        // Se espera que se lance una excepción cuando el tiempo máximo sea negativo
        assertThrows(IllegalArgumentException.class, () -> {
            tarifaServicio.modificarTarifa(id, 10, -1, 150.0, 120, "normal");
        });
    }
    @Test
    void testModificarTarifa_PrecioNegativo() {
        Long id = 1L;
        Tarifa tarifaExistente = new Tarifa(5, 60, 100.0, 120, "normal");

        // Mock de la respuesta del repositorio
        when(tarifaRepositorio.findById(id)).thenReturn(Optional.of(tarifaExistente));

        // Se espera que se lance una excepción cuando el precio sea negativo
        assertThrows(IllegalArgumentException.class, () -> {
            tarifaServicio.modificarTarifa(id, 10, 60, -50.0, 120, "normal");
        });
    }

    @Test
    void testModificarTarifa_DuracionReservaMenorQueTiempoMaximo() {
        Long id = 1L;
        Tarifa tarifaExistente = new Tarifa(5, 60, 100.0, 120, "normal");

        // Mock de la respuesta del repositorio
        when(tarifaRepositorio.findById(id)).thenReturn(Optional.of(tarifaExistente));

        // Se espera que se lance una excepción cuando la duración de la reserva sea menor que el tiempo máximo
        assertThrows(IllegalArgumentException.class, () -> {
            tarifaServicio.modificarTarifa(id, 10, 60, 150.0, 50, "normal");
        });
    }

    @Test
    void testModificarTarifa_Exitosa() {
        Long id = 1L;
        int nuevasVueltas = 10;
        int nuevoTiempomax = 80;
        double nuevoPrecio = 150.0;
        int nuevaDuracion = 120;
        String nuevoTipo = "dia especial";

        Tarifa tarifaExistente = new Tarifa(5, 60, 100.0, 120, "normal");

        // Mock de la respuesta del repositorio
        when(tarifaRepositorio.findById(id)).thenReturn(Optional.of(tarifaExistente));
        when(tarifaRepositorio.save(any(Tarifa.class))).thenReturn(tarifaExistente);

        tarifaServicio.modificarTarifa(id, nuevasVueltas, nuevoTiempomax, nuevoPrecio, nuevaDuracion, nuevoTipo);

        // Verificamos que los valores de la tarifa se hayan actualizado correctamente
        assertEquals(nuevasVueltas, tarifaExistente.getNumeroVueltas());
        assertEquals(nuevoTiempomax, tarifaExistente.getTiempoMax());
        assertEquals(nuevoPrecio, tarifaExistente.getPrecio());
        assertEquals(nuevaDuracion, tarifaExistente.getDuracionReserva());
        assertEquals(nuevoTipo, tarifaExistente.getTipo());

        verify(tarifaRepositorio, times(1)).save(any(Tarifa.class));
    }

    @Test
    void whenTarifaNotFound_thenThrowIllegalArgumentException() {
        // Given
        Long id = 1L; // ID de tarifa que no existe
        int nuevasVueltas = 5;
        int nuevoTiempomax = 30;
        double nuevoPrecio = 1000.0;
        int nuevaDuracion = 60;
        String nuevoTipo = "normal";

        // Configurar el mock del repositorio para retornar un Optional vacío
        when(tarifaRepositorio.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> tarifaServicio.modificarTarifa(id, nuevasVueltas, nuevoTiempomax, nuevoPrecio, nuevaDuracion, nuevoTipo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La tarifa no existe");  // Verifica que la excepción tenga el mensaje esperado
    }
}
