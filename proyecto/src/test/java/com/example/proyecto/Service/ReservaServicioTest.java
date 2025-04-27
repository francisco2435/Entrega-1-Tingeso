package com.example.proyecto.Service;

import com.example.proyecto.Entity.Kart;
import com.example.proyecto.Entity.Reserva;
import com.example.proyecto.Entity.Tarifa;
import com.example.proyecto.Entity.Usuario;
import com.example.proyecto.Repository.ReservaRepositorio;
import com.example.proyecto.Repository.KartRepositorio;
import com.example.proyecto.Repository.TarifaRepositorio;
import com.example.proyecto.Repository.UsuarioRepositorio;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaServicioTest {
    @Mock
    private ReservaRepositorio reservaRepositorio = Mockito.mock(ReservaRepositorio.class);

    @Mock
    private KartRepositorio kartRepositorio = Mockito.mock(KartRepositorio.class);

    @Mock
    private TarifaRepositorio tarifaRepositorio = Mockito.mock(TarifaRepositorio.class);

    @Mock
    private JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);

    @InjectMocks
    private ReservaServicio reservaServicio; // ReservaServicio es inyectado con los mocks

    @Mock
    private UsuarioRepositorio usuarioRepositorio = Mockito.mock(UsuarioRepositorio.class);

    @BeforeEach
    void setUp() {
        reservaRepositorio = Mockito.mock(ReservaRepositorio.class);
        kartRepositorio = Mockito.mock(KartRepositorio.class);
        tarifaRepositorio = Mockito.mock(TarifaRepositorio.class);
        mailSender = Mockito.mock(JavaMailSender.class);
        usuarioRepositorio = Mockito.mock(UsuarioRepositorio.class);
        reservaServicio = Mockito.spy(new ReservaServicio(reservaRepositorio, kartRepositorio, tarifaRepositorio, mailSender, usuarioRepositorio));

    }

    @Test
    void whenComprobarTopeHorarioWithNoOverlap_thenReturnTrue() {
        // Given
        LocalDate fecha = LocalDate.now();
        when(reservaRepositorio.findByFechaReserva(fecha)).thenReturn(Collections.emptyList());

        // When
        boolean result = reservaServicio.comprobarTopeHorario(fecha, LocalTime.of(15, 0), LocalTime.of(16, 0), 2);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void whenComprobarTopeHorarioWithOverlap_thenReturnFalse() {
        // Given
        LocalDate fecha = LocalDate.now();
        Reserva reserva = new Reserva();
        reserva.setHoraInicio(LocalTime.of(14, 30));
        reserva.setHoraFin(LocalTime.of(15, 30));
        when(reservaRepositorio.findByFechaReserva(fecha)).thenReturn(List.of(reserva));

        // When
        boolean result = reservaServicio.comprobarTopeHorario(fecha, LocalTime.of(15, 0), LocalTime.of(16, 0), 2);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void whenComprobarHorarioTrabajoOnWeekday_thenReturnTrue() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 4, 22); // martes
        LocalTime inicio = LocalTime.of(14, 0);
        LocalTime fin = LocalTime.of(21, 0);

        // When
        boolean result = reservaServicio.comprobarHorarioTrabajo(fecha, inicio, fin);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void whenComprobarHorarioTrabajoOnWeekend_thenReturnTrue() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 4, 26); // sábado
        LocalTime inicio = LocalTime.of(10, 0);
        LocalTime fin = LocalTime.of(22, 0);

        // When
        boolean result = reservaServicio.comprobarHorarioTrabajo(fecha, inicio, fin);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void whenObtenerCantidadKartsDisponibles_thenReturnCorrectNumber() {
        // Given
        LocalDate fecha = LocalDate.now();
        Reserva reserva = new Reserva();
        reserva.setHoraInicio(LocalTime.of(15, 0));
        reserva.setHoraFin(LocalTime.of(16, 0));
        reserva.setCantidadPersonas(4);

        // ATENCIÓN aquí, ahora usamos new Kart()
        when(kartRepositorio.findByEstado("disponible")).thenReturn(Collections.nCopies(10, new Kart()));
        when(reservaRepositorio.findByFechaReserva(fecha)).thenReturn(List.of(reserva));

        // When
        int disponibles = reservaServicio.obtenerCantidadKartsDisponibles(fecha, LocalTime.of(15, 30), LocalTime.of(16, 30));

        // Then
        assertThat(disponibles).isEqualTo(6);
    }

    @Test
    void whenObtenerReservas_thenReturnAll() {
        // Given
        Reserva reserva = new Reserva();
        when(reservaRepositorio.findAll()).thenReturn(List.of(reserva));

        // When
        List<Reserva> reservas = reservaServicio.ObtenerReservas();

        // Then
        assertThat(reservas).isNotEmpty();
    }

    @Test
    void whenObtenerTarifaWithVueltas_thenReturnCorrectTarifa() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 4, 22); // martes
        Tarifa tarifa = new Tarifa();
        when(tarifaRepositorio.findByTipoAndNumeroVueltas(anyString(), anyInt())).thenReturn(tarifa);

        // When
        Tarifa result = reservaServicio.obtenerTarifa(fecha, 0, 5);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void whenObtenerTarifaWithTiempo_thenReturnCorrectTarifa() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 4, 26); // sábado
        Tarifa tarifa = new Tarifa();
        when(tarifaRepositorio.findByTipoAndTiempoMax(anyString(), anyInt())).thenReturn(tarifa);

        // When
        Tarifa result = reservaServicio.obtenerTarifa(fecha, 30, 0);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void whenObtenerTarifaWithInvalidParams_thenThrowException() {
        // When / Then
        assertThatThrownBy(() -> reservaServicio.obtenerTarifa(LocalDate.now(), 0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Debe especificar numVueltas o tiempoMax");
    }

    @Test
    void whenDiasEspecialesOnWeekend_thenReturn1() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 4, 26); // sábado

        // When
        int tipo = reservaServicio.diasEspeciales(fecha);

        // Then
        assertThat(tipo).isEqualTo(1);
    }

    @Test
    void whenDiasEspecialesOnHoliday_thenReturn2() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 9, 18); // 18 de septiembre

        // When
        int tipo = reservaServicio.diasEspeciales(fecha);

        // Then
        assertThat(tipo).isEqualTo(2);
    }

    @Test
    void whenDiasEspecialesOnWeekday_thenReturn0() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 4, 22); // martes

        // When
        int tipo = reservaServicio.diasEspeciales(fecha);

        // Then
        assertThat(tipo).isEqualTo(0);
    }

    @Test
    void whenGenerarComprobanteReserva_thenReturnDocument() throws IOException {
        // Given
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setNombreCliente("Juan");
        reserva.setFechaReserva(LocalDate.now());
        reserva.setHoraReserva(LocalTime.of(15, 0));
        reserva.setNumVueltas(5);
        reserva.setTiempoMax(0);
        reserva.setCantidadPersonas(3);
        reserva.setTipoTarifa("normal");
        reserva.setNombres(List.of("Juan", "Pedro", "Ana"));
        reserva.setNombreDescuentoTamanoGrupo(Collections.emptyList());
        reserva.setValorDescuentoTamanoGrupo(Collections.emptyList());
        reserva.setNombreDescuentoEspeciales(Collections.emptyList());
        reserva.setValorDescuentoEspeciales(Collections.emptyList());
        reserva.setMontoTotal(10000.0);
        reserva.setValorIva(1900.0);

        // When
        PDDocument document = reservaServicio.generarComprobanteReserva(reserva);

        // Then
        assertThat(document).isNotNull();
        document.close();
    }


    @Test
    void whenComprobarTopeHorario_thenReturnFalseWhenOverlap() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 4, 15);
        LocalTime horaInicio = LocalTime.of(10, 0);
        LocalTime horaFin = LocalTime.of(11, 0);
        List<Reserva> reservas = List.of(
                new Reserva("12345678-9", "Juan", LocalTime.of(10, 30), LocalTime.of(11, 30), 60, List.of("12345678-9"), fecha, LocalTime.of(11, 0), 5, 30, 2, List.of("Juan", "Pedro"), List.of("Grupo Grande"), List.of(10.0), List.of("Descuento Especial"), List.of(5.0), 200.0, 38.0, 238.0, "normal")
        );
        when(reservaRepositorio.findByFechaReserva(fecha)).thenReturn(reservas);

        // When
        boolean result = reservaServicio.comprobarTopeHorario(fecha, horaInicio, horaFin, 2);

        // Then
        assertThat(result).isFalse();  // Hay solapamiento de horarios
        verify(reservaRepositorio, times(1)).findByFechaReserva(fecha);
    }

    @Test
    void whenCalcularFrecuencia_thenReturnCorrectFrequency() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 4, 15);  // Un día en el mes de abril
        String rutIntegrante = "12345678-9";

        List<Reserva> reservasDelMes = List.of(
                new Reserva("12345678-9", "Juan", LocalTime.of(14, 0), LocalTime.of(15, 0), 60, List.of("12345678-9"), fecha, LocalTime.of(13, 0), 5, 30, 2, List.of("Juan", "Pedro"), List.of("Grupo Grande"), List.of(10.0), List.of("Descuento Especial"), List.of(5.0), 200.0, 38.0, 238.0, "normal"),
                new Reserva("12345678-9", "Juan", LocalTime.of(16, 0), LocalTime.of(17, 0), 60, List.of("12345678-9"), fecha, LocalTime.of(15, 0), 5, 30, 2, List.of("Juan", "Pedro"), List.of("Grupo Grande"), List.of(10.0), List.of("Descuento Especial"), List.of(5.0), 200.0, 38.0, 238.0, "normal")
        );

        when(reservaRepositorio.findByFechaReservaBetween(any(), any())).thenReturn(reservasDelMes);

        // When
        int frecuencia = reservaServicio.calcularFrecuencia(fecha, rutIntegrante);

        // Then
        assertThat(frecuencia).isEqualTo(2); // El rutIntegrante aparece 2 veces en las reservas
        verify(reservaRepositorio, times(1)).findByFechaReservaBetween(any(), any());
    }


    @Test
    void whenCalcularFrecuenciaWithNullRut_thenThrowException() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 4, 15);

        // When / Then
        assertThatThrownBy(() -> reservaServicio.calcularFrecuencia(fecha, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El rut no puede ser vacío");
    }

    @Test
    void whenCalcularFrecuenciaWithEmptyRut_thenThrowException() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 4, 15);

        // When / Then
        assertThatThrownBy(() -> reservaServicio.calcularFrecuencia(fecha, ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El rut no puede ser vacío");
    }

    @Test
    void whenNoDescuentoApplicable_thenReturnZero() {
        // Given
        String rutIntegrante = "12345678-9";
        int cantidadIntegrantes = 2; // No se aplica ningún descuento
        LocalDate fecha = LocalDate.of(2025, 4, 15);
        int descuentosCumpleanosAplicados = 0;

        // When
        double descuento = reservaServicio.calcularDescuento(rutIntegrante, cantidadIntegrantes, fecha, descuentosCumpleanosAplicados);

        // Then
        assertThat(descuento).isEqualTo(0.0);
    }

    @Test
    void whenDescuentoPorCantidadPersonas_thenReturnCorrectDiscount() {
        // Given
        String rutIntegrante = "12345678-9";
        int cantidadIntegrantes = 4; // Se aplica descuento por 3-5 integrantes
        LocalDate fecha = LocalDate.of(2025, 4, 15);
        int descuentosCumpleanosAplicados = 0;

        // When
        double descuento = reservaServicio.calcularDescuento(rutIntegrante, cantidadIntegrantes, fecha, descuentosCumpleanosAplicados);

        // Then
        assertThat(descuento).isEqualTo(1.1); // Descuento por 3-5 integrantes
    }


    @Test
    void whenDescuentoCumpleanos_thenReturnBirthdayDiscount() {
        // Given
        String rutIntegrante = "12345678-9";
        int cantidadIntegrantes = 4; // Se aplica descuento por cumpleaños (cumple 3-5)
        LocalDate fecha = LocalDate.of(2025, 4, 15);
        int descuentosCumpleanosAplicados = 0;

        // Mock para fecha de nacimiento
        Usuario usuario = new Usuario();
        usuario.setFechaNacimiento(LocalDate.of(2025, 4, 15)); // Es su cumpleaños
        when(usuarioRepositorio.findByRut(rutIntegrante)).thenReturn(usuario);

        // When
        double descuento = reservaServicio.calcularDescuento(rutIntegrante, cantidadIntegrantes, fecha, descuentosCumpleanosAplicados);

        // Then
        assertThat(descuento).isEqualTo(3.5); // Descuento de cumpleaños
    }

    @Test
    void whenDescuentoCumpleanosExceedsLimit_thenNoBirthdayDiscount() {
        // Given
        String rutIntegrante = "12345678-9";
        int cantidadIntegrantes = 4;
        LocalDate fecha = LocalDate.of(2025, 4, 15);
        int descuentosCumpleanosAplicados = 3; // Ya se ha alcanzado el límite de descuentos

        // Mock para fecha de nacimiento
        Usuario usuario = new Usuario();
        usuario.setFechaNacimiento(LocalDate.of(2025, 4, 15)); // Es su cumpleaños
        when(usuarioRepositorio.findByRut(rutIntegrante)).thenReturn(usuario);

        // When
        double descuento = reservaServicio.calcularDescuento(rutIntegrante, cantidadIntegrantes, fecha, descuentosCumpleanosAplicados);

        // Then
        assertThat(descuento).isEqualTo(1.1); // Solo se aplica el descuento por cantidad de personas
    }

    @Test
    void whenUserNotFound_thenOnlyGroupDiscountApplied() {
        // Given
        String rutIntegrante = "12345678-9";
        int cantidadIntegrantes = 4; // Se aplica descuento por cantidad
        LocalDate fecha = LocalDate.of(2025, 4, 15);
        int descuentosCumpleanosAplicados = 0;

        // Usuario no registrado o sin fecha de nacimiento
        when(usuarioRepositorio.findByRut(rutIntegrante)).thenReturn(null);

        // When
        double descuento = reservaServicio.calcularDescuento(rutIntegrante, cantidadIntegrantes, fecha, descuentosCumpleanosAplicados);

        // Then
        assertThat(descuento).isEqualTo(1.1); // Descuento solo por cantidad de personas
    }

    @Test
    void whenObtenerReservas_thenReturnList() {
        // Given
        List<Reserva> reservasMock = new ArrayList<>();
        reservasMock.add(new Reserva());
        reservasMock.add(new Reserva());
        when(reservaRepositorio.findAll()).thenReturn(reservasMock);

        // When
        List<Reserva> reservas = reservaServicio.ObtenerReservas();

        // Then
        assertNotNull(reservas);  // Verifica que no sea null
        assertEquals(2, reservas.size());  // Verifica que la lista contenga 2 elementos
    }

    @Test
    void whenNumVueltasAndTiempoMaxAreZero_thenThrowException() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 5, 1);  // Un día normal
        int numVueltas = 0;
        int tiempoMax = 0;

        // When / Then
        assertThatThrownBy(() -> reservaServicio.obtenerTarifa(fecha, tiempoMax, numVueltas))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Debe especificar numVueltas o tiempoMax.");
    }

    @Test
    void whenNoReservations_thenReturnEmptyList() {
        // Given
        when(reservaRepositorio.findAll()).thenReturn(Collections.emptyList());  // El repositorio devuelve una lista vacía

        // When
        List<Reserva> reservas = reservaServicio.ObtenerReservas();

        // Then
        assertNotNull(reservas);  // Aseguramos que no sea null
        assertTrue(reservas.isEmpty());  // Verificamos que la lista esté vacía
    }

    @Test
    void whenMultipleReservations_thenReturnListOfReservations() {
        // Given
        Reserva reserva1 = new Reserva(); // Suponiendo que tienes un constructor adecuado para Reserva
        Reserva reserva2 = new Reserva();
        when(reservaRepositorio.findAll()).thenReturn(Arrays.asList(reserva1, reserva2));  // El repositorio devuelve dos reservas

        // When
        List<Reserva> reservas = reservaServicio.ObtenerReservas();

        // Then
        assertNotNull(reservas);  // Verificamos que no sea null
        assertEquals(2, reservas.size());  // Verificamos que el tamaño de la lista sea 2
    }

    @Test
    void whenReservationsAreNull_thenReturnEmptyList() {
        // Given
        when(reservaRepositorio.findAll()).thenReturn(null);  // El repositorio devuelve null

        // When
        List<Reserva> reservas = reservaServicio.ObtenerReservas();

        // Then
        assertNotNull(reservas);  // Aseguramos que no sea null
        assertTrue(reservas.isEmpty());  // Verificamos que la lista esté vacía
    }

    @Test
    void whenFechaIsNull_thenThrowIllegalArgumentException() {
        // Given
        String rutCliente = "12345678";
        String nombreCliente = "Juan Pérez";
        LocalDate fecha = null; // Fecha nula
        LocalTime horaInicio = LocalTime.of(10, 0);
        int tiempoMax = 30;
        int numVueltas = 3;
        int cantidadPersonas = 5;
        List<String> rutsAmigos = Arrays.asList("87654321", "12345679");
        List<String> nombres = Arrays.asList("Amigo 1", "Amigo 2");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            reservaServicio.hacerReserva(rutCliente, nombreCliente, fecha, horaInicio, tiempoMax, numVueltas, cantidadPersonas, rutsAmigos, nombres);
        }, "La fecha no puede ser nula");
    }

    @Test
    void whenRutsAmigosSizeDoesNotMatchNombresSize_thenThrowIllegalArgumentException() {
        // Given
        String rutCliente = "12345678";
        String nombreCliente = "Juan Pérez";
        LocalDate fecha = LocalDate.of(2025, 5, 2);
        LocalTime horaInicio = LocalTime.of(10, 0);
        int tiempoMax = 30;
        int numVueltas = 3;
        int cantidadPersonas = 3;
        List<String> rutsAmigos = Arrays.asList("87654321", "12345679"); // Solo 2 ruts
        List<String> nombres = Arrays.asList("Amigo 1", "Amigo 2", "Amigo 3"); // 3 nombres

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            reservaServicio.hacerReserva(rutCliente, nombreCliente, fecha, horaInicio, tiempoMax, numVueltas, cantidadPersonas, rutsAmigos, nombres);
        }, "La cantidad de ruts ingresados no es igual al de los nombres ingresados");
    }


    @Test
    void whenRutsAmigosSizeDoesNotMatchCantidadPersonas_thenThrowIllegalArgumentException() {
        // Given
        String rutCliente = "12345678";
        String nombreCliente = "Juan Pérez";
        LocalDate fecha = LocalDate.of(2025, 5, 2);
        LocalTime horaInicio = LocalTime.of(10, 0);
        int tiempoMax = 30;
        int numVueltas = 3;
        int cantidadPersonas = 5;  // Número de personas es 5
        List<String> rutsAmigos = Arrays.asList("87654321", "12345679"); // Solo 2 ruts
        List<String> nombres = Arrays.asList("Amigo 1", "Amigo 2");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            reservaServicio.hacerReserva(rutCliente, nombreCliente, fecha, horaInicio, tiempoMax, numVueltas, cantidadPersonas, rutsAmigos, nombres);
        }, "El numero de ruts ingresados no coinciden con el numero de personas ingresadas");
    }

    @Test
    void whenCantidadPersonasIsGreaterThan15_thenThrowIllegalArgumentException() {
        // Given
        String rutCliente = "12345678";
        String nombreCliente = "Juan Pérez";
        LocalDate fecha = LocalDate.of(2025, 5, 2);
        LocalTime horaInicio = LocalTime.of(10, 0);
        int tiempoMax = 30;
        int numVueltas = 3;
        int cantidadPersonas = 16;  // Mayor a 15 personas
        List<String> rutsAmigos = Arrays.asList("87654321", "12345679", "23456780", "34567891", "45678902", "56789013", "67890124", "78901235", "89012346", "90123457");
        List<String> nombres = Arrays.asList("Amigo 1", "Amigo 2", "Amigo 3", "Amigo 4", "Amigo 5", "Amigo 6", "Amigo 7", "Amigo 8", "Amigo 9", "Amigo 10", "Amigo 11", "Amigo 12", "Amigo 13", "Amigo 14", "Amigo 15", "Amigo 16");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            reservaServicio.hacerReserva(rutCliente, nombreCliente, fecha, horaInicio, tiempoMax, numVueltas, cantidadPersonas, rutsAmigos, nombres);
        }, "El numero de personas ingresadas es mayor que 15");
    }

    @Test
    void hacerReserva_CuandoDatosValidos_RetornaReservaGuardada() {
        // Arrange
        String rutCliente = "12345678-9";
        String nombreCliente = "Juan Pérez";
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(10, 0);
        int tiempoMax = 30;
        int numVueltas = 10;
        int cantidadPersonas = 2;

        List<String> rutsAmigos = new ArrayList<>();
        rutsAmigos.add("11111111-1");

        List<String> nombres = new ArrayList<>();
        nombres.add("Amigo Uno");

        Tarifa tarifaMock = new Tarifa();
        tarifaMock.setDuracionReserva(60);
        tarifaMock.setPrecio(10000.0);
        tarifaMock.setTipo("normal");

        Usuario usuarioMock = new Usuario();
        usuarioMock.setRut(rutCliente);

        Reserva reservaEsperada = new Reserva();

        // Mockeamos comportamiento
        when(kartRepositorio.count()).thenReturn(10L);
        when(reservaRepositorio.save(any(Reserva.class))).thenReturn(reservaEsperada);
        when(usuarioRepositorio.findByRut(anyString())).thenReturn(usuarioMock);
        when(tarifaRepositorio.findById(anyLong())).thenReturn(java.util.Optional.of(tarifaMock));

        // Simulamos métodos internos
        doReturn(10).when(reservaServicio).obtenerCantidadKartsDisponibles(any(), any(), any());
        doReturn(tarifaMock).when(reservaServicio).obtenerTarifa(any(), anyInt(), anyInt());
        doReturn(true).when(reservaServicio).comprobarHorarioTrabajo(any(), any(), any());
        doReturn(true).when(reservaServicio).comprobarTopeHorario(any(), any(), any(), anyInt());
        doReturn(0.0).when(reservaServicio).calcularDescuento(anyString(), anyInt(), any(), anyInt());
        doNothing().when(reservaServicio).enviarComprobanteReserva(any(), any());

        // Act
        Reserva resultado = reservaServicio.hacerReserva(
                rutCliente, nombreCliente, fecha, horaInicio, tiempoMax,
                numVueltas, cantidadPersonas, new ArrayList<>(rutsAmigos), new ArrayList<>(nombres)
        );

        // Assert
        assertThat(resultado).isNotNull();
        verify(reservaRepositorio).save(any(Reserva.class));
    }


    @Test
    void hacerReserva_CuandoFechaEsNula_LanzaExcepcion() {
        // Arrange
        LocalDate fecha = null;
        LocalTime horaInicio = LocalTime.of(10, 0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                reservaServicio.hacerReserva("11111111-1", "Cliente Uno", fecha, horaInicio, 60, 10, 2, new ArrayList<>(), new ArrayList<>())
        );
    }

    @Test
    void hacerReserva_CuandoRutsYNombreNoCoinciden_LanzaExcepcion() {
        // Arrange
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(10, 0);

        List<String> rutsAmigos = new ArrayList<>(Arrays.asList("22222222-2"));
        List<String> nombres = new ArrayList<>();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                reservaServicio.hacerReserva("11111111-1", "Cliente Uno", fecha, horaInicio, 60, 10, 2, rutsAmigos, nombres)
        );
    }

    @Test
    void hacerReserva_CuandoNoHaySuficientesKarts_LanzaExcepcion() {
        // Arrange
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(10, 0);
        List<String> rutsAmigos = new ArrayList<>(Arrays.asList("22222222-2"));
        List<String> nombres = new ArrayList<>(Arrays.asList("Amigo Uno"));

        when(reservaServicio.obtenerCantidadKartsDisponibles(any(), any(), any())).thenReturn(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                reservaServicio.hacerReserva("11111111-1", "Cliente Uno", fecha, horaInicio, 60, 10, 2, rutsAmigos, nombres)
        );
    }

    @Test
    void hacerReserva_CuandoNumeroDeRutsNoCoincideConCantidadPersonas_LanzaExcepcion() {
        // Arrange
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(10, 0);
        List<String> rutsAmigos = new ArrayList<>(Arrays.asList("22222222-2"));
        List<String> nombres = new ArrayList<>(Arrays.asList("Amigo Uno"));

        when(reservaServicio.obtenerCantidadKartsDisponibles(any(), any(), any())).thenReturn(10);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                reservaServicio.hacerReserva("11111111-1", "Cliente Uno", fecha, horaInicio, 60, 10, 3, rutsAmigos, nombres)
        );
    }

    @Test
    void hacerReserva_CuandoCantidadPersonasMayorA15_LanzaExcepcion() {
        // Arrange
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(10, 0);
        List<String> rutsAmigos = new ArrayList<>();
        List<String> nombres = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            rutsAmigos.add("rut" + i);
            nombres.add("nombre" + i);
        }

        when(reservaServicio.obtenerCantidadKartsDisponibles(any(), any(), any())).thenReturn(20);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                reservaServicio.hacerReserva("11111111-1", "Cliente Uno", fecha, horaInicio, 60, 10, 16, rutsAmigos, nombres)
        );
    }

    @Test
    void whenHacerReservaWithValidData_thenReturnReserva() {
        // Given
        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime horaInicio = LocalTime.of(10, 0);
        int tiempoMax = 60;
        int numVueltas = 5;
        int cantidadPersonas = 2;
        String rutCliente = "12345678-9";
        String nombreCliente = "Juan Perez";
        List<String> rutsAmigos = new ArrayList<>(List.of("98765432-1"));
        List<String> nombres = new ArrayList<>(List.of("Maria Lopez"));

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        Kart kart = new Kart();
        kart.setEstado("disponible");

        Tarifa tarifa = new Tarifa();
        tarifa.setDuracionReserva(60);
        tarifa.setPrecio(10000.0);

        Usuario usuario = new Usuario();
        usuario.setRut("98765432-1");
        usuario.setCorreo("test@example.com");  // <-- Asegurar que el usuario tiene correo

        // Mock obtenerCantidadKartsDisponibles
        doReturn(5).when(reservaServicio).obtenerCantidadKartsDisponibles(any(), any(), any());
        // Mock obtenerTarifa
        doReturn(tarifa).when(reservaServicio).obtenerTarifa(any(), anyInt(), anyInt());
        // Mock comprobarHorarioTrabajo
        doReturn(true).when(reservaServicio).comprobarHorarioTrabajo(any(), any(), any());
        // Mock comprobarTopeHorario
        doReturn(true).when(reservaServicio).comprobarTopeHorario(any(), any(), any(), anyInt());
        // Mock calcularDescuento
        doReturn(0.0).when(reservaServicio).calcularDescuento(any(), anyInt(), any(), anyInt());
        // Mock usuarioRepositorio
        when(usuarioRepositorio.findByRut(any())).thenReturn(usuario);
        // Mock reservaRepositorio para evitar que retorne null
        when(reservaRepositorio.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Reserva reserva = null;
        try {
            reserva = reservaServicio.hacerReserva(
                    rutCliente, nombreCliente, fecha, horaInicio,
                    tiempoMax, numVueltas, cantidadPersonas,
                    rutsAmigos, nombres
            );
        } catch (Exception e) {
            e.printStackTrace();
            fail("Se lanzó una excepción inesperada: " + e.getMessage());
        }

        // Then
        assertThat(reserva).isNotNull();
    }

    @Test
    void whenFechaIsNull_thenThrowException() {
        // Given
        LocalDate fecha = null;
        LocalTime horaInicio = LocalTime.of(10, 0);
        LocalTime horaFin = horaInicio.plusMinutes(60);
        int cantidadPersonas = 2;
        List<String> rutsAmigos = List.of("98765432-1");
        List<String> nombres = List.of("Maria Lopez");

        // When - Then
        assertThrows(IllegalArgumentException.class, () -> {
            reservaServicio.hacerReserva(
                    "12345678-9", "Juan Perez",
                    fecha, horaInicio, 60, 5, cantidadPersonas,
                    rutsAmigos, nombres
            );
        });
    }

    @Test
    void whenUsuarioNoRegistrado_thenApplyDescuentoTamanoGrupo() {
        // Given
        double parteEntera = 1.0;
        double descuento = 0.1; // 10% de descuento
        Tarifa tarifa = new Tarifa();
        tarifa.setPrecio(10000.0); // Suponemos que la tarifa es 10,000
        List<String> nombreDescuentoTamanoGrupo = new ArrayList<>();
        List<Double> valorDescuentoTamanoGrupo = new ArrayList<>();
        List<String> nombreDescuentoEspeciales = new ArrayList<>();
        List<Double> valorDescuentoEspeciales = new ArrayList<>();
        double montoTotal = 0.0;

        Usuario usuarioEncontrado = null; // El usuario no está registrado

        // When
        if (usuarioEncontrado == null) {
            if (parteEntera == 1.0) {
                nombreDescuentoTamanoGrupo.add("Por número de personas");
                valorDescuentoTamanoGrupo.add(descuento);
                nombreDescuentoEspeciales.add("No Aplica");
                valorDescuentoEspeciales.add(0.0);
                montoTotal += Math.round((tarifa.getPrecio() - tarifa.getPrecio() * descuento) * 100.0) / 100.0;
            } else {
                nombreDescuentoTamanoGrupo.add("No Aplica");
                valorDescuentoTamanoGrupo.add(0.0);
                nombreDescuentoEspeciales.add("No Aplica");
                valorDescuentoEspeciales.add(0.0);
                montoTotal += Math.round((tarifa.getPrecio()) * 100.0) / 100.0;
            }
        }

        // Then
        assertThat(montoTotal).isEqualTo(9000.0); // Se aplica un descuento de 10% sobre 10,000
        assertThat(nombreDescuentoTamanoGrupo).containsExactly("Por número de personas");
        assertThat(valorDescuentoTamanoGrupo).containsExactly(0.1);
        assertThat(nombreDescuentoEspeciales).containsExactly("No Aplica");
        assertThat(valorDescuentoEspeciales).containsExactly(0.0);
    }



    @Test
    void testCalcularDescuento_Cumpleanos() {
        String rutIntegrante = "12345678-9";
        int cantidadIntegrantes = 5;  // Número de personas entre 3 y 5
        LocalDate fecha = LocalDate.of(2025, 4, 25);  // Fecha de ejemplo (cumpleaños del usuario)
        int descuentosCumpleanosAplicados = 0;

        // Simulamos que el usuario existe y es su cumpleaños
        Usuario usuarioMock = new Usuario();
        usuarioMock.setFechaNacimiento(LocalDate.of(1990, 4, 25));  // Fecha de nacimiento
        when(usuarioRepositorio.findByRut(rutIntegrante)).thenReturn(usuarioMock);

        // Llamamos al método
        double descuento = reservaServicio.calcularDescuento(rutIntegrante, cantidadIntegrantes, fecha, descuentosCumpleanosAplicados);

        // Verificamos que el descuento de cumpleaños (3.5) se ha aplicado correctamente
        assertEquals(3.5, descuento);
    }

    @Test
    void testObtenerTarifa_ExcepcionParametrosInvalidos() {
        LocalDate fecha = LocalDate.of(2025, 4, 25);  // Fecha de ejemplo
        int tiempoMax = 0;
        int numVueltas = 0;

        // Llamamos al método y verificamos que se lanza la excepción correspondiente
        assertThrows(IllegalArgumentException.class, () -> {
            reservaServicio.obtenerTarifa(fecha, tiempoMax, numVueltas);
        });
    }

    @Test
    void testObtenerTarifa_TarifaNoEncontrada() {
        LocalDate fecha = LocalDate.of(2025, 4, 25);  // Fecha de ejemplo
        int tiempoMax = 120;
        int numVueltas = 10;

        // Simulamos que el repositorio no devuelve una tarifa (se configura como null)
        when(tarifaRepositorio.findByTipoAndNumeroVueltas("normal", numVueltas)).thenReturn(null);
        when(tarifaRepositorio.findByTipoAndNumeroVueltas("fin de semana", numVueltas)).thenReturn(null);
        when(tarifaRepositorio.findByTipoAndNumeroVueltas("dia especial", numVueltas)).thenReturn(null);
        when(tarifaRepositorio.findByTipoAndTiempoMax("normal", tiempoMax)).thenReturn(null);
        when(tarifaRepositorio.findByTipoAndTiempoMax("fin de semana", tiempoMax)).thenReturn(null);
        when(tarifaRepositorio.findByTipoAndTiempoMax("dia especial", tiempoMax)).thenReturn(null);

        // Llamamos al método y verificamos que se lanza la excepción correspondiente
        assertThrows(IllegalArgumentException.class, () -> {
            reservaServicio.obtenerTarifa(fecha, tiempoMax, numVueltas);
        });
    }

    @Test
    void testObtenerTarifa_NumVueltas() {
        LocalDate fecha = LocalDate.of(2025, 4, 25);  // Fecha de ejemplo
        int tiempoMax = 0;
        int numVueltas = 10;

        // Simulamos que el repositorio devuelve una tarifa válida para el tipo de día "normal"
        Tarifa tarifaMock = new Tarifa();
        when(tarifaRepositorio.findByTipoAndNumeroVueltas("normal", numVueltas)).thenReturn(tarifaMock);

        // Llamamos al método
        Tarifa tarifa = reservaServicio.obtenerTarifa(fecha, tiempoMax, numVueltas);

        // Verificamos que se devuelve la tarifa correcta
        assertNotNull(tarifa);
        assertSame(tarifaMock, tarifa);
    }

    @Test
    void testObtenerTarifa_TiempoMax() {
        LocalDate fecha = LocalDate.of(2025, 4, 25);  // Fecha de ejemplo
        int tiempoMax = 120;  // Tiempo máximo
        int numVueltas = 0;

        // Simulamos que el repositorio devuelve una tarifa válida para el tipo de día "normal"
        Tarifa tarifaMock = new Tarifa();
        when(tarifaRepositorio.findByTipoAndTiempoMax("normal", tiempoMax)).thenReturn(tarifaMock);

        // Llamamos al método
        Tarifa tarifa = reservaServicio.obtenerTarifa(fecha, tiempoMax, numVueltas);

        // Verificamos que se devuelve la tarifa correcta
        assertNotNull(tarifa);
        assertSame(tarifaMock, tarifa);
    }

    @Test
    void testObtenerTarifa_TipoDiaFinDeSemana() {
        LocalDate fecha = LocalDate.of(2025, 4, 25);  // Fecha de ejemplo (un fin de semana)
        int tiempoMax = 120;
        int numVueltas = 10;

        // Simulamos que el tipo de día es "fin de semana"
        when(reservaServicio.diasEspeciales(fecha)).thenReturn(1);  // 1 indica fin de semana

        // Simulamos que el repositorio devuelve una tarifa válida para "fin de semana"
        Tarifa tarifaMock = new Tarifa();
        when(tarifaRepositorio.findByTipoAndNumeroVueltas("fin de semana", numVueltas)).thenReturn(tarifaMock);

        // Llamamos al método
        Tarifa tarifa = reservaServicio.obtenerTarifa(fecha, tiempoMax, numVueltas);

        // Verificamos que se devuelve la tarifa correcta para "fin de semana"
        assertNotNull(tarifa);
        assertSame(tarifaMock, tarifa);
    }

    @Test
    void testObtenerTarifa_TipoDiaEspecial() {
        LocalDate fecha = LocalDate.of(2025, 4, 25);  // Fecha de ejemplo (un día especial)
        int tiempoMax = 120;
        int numVueltas = 10;

        // Simulamos que el tipo de día es "día especial"
        when(reservaServicio.diasEspeciales(fecha)).thenReturn(2);  // 2 indica día especial

        // Simulamos que el repositorio devuelve una tarifa válida para "día especial"
        Tarifa tarifaMock = new Tarifa();
        when(tarifaRepositorio.findByTipoAndNumeroVueltas("dia especial", numVueltas)).thenReturn(tarifaMock);

        // Llamamos al método
        Tarifa tarifa = reservaServicio.obtenerTarifa(fecha, tiempoMax, numVueltas);

        // Verificamos que se devuelve la tarifa correcta para "día especial"
        assertNotNull(tarifa);
        assertSame(tarifaMock, tarifa);
    }


    @Test
    void testEnviarComprobanteReserva_MessagingException() throws IOException, MessagingException {
        // Guardar el System.err original
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        try {
            // Crear una reserva y un usuario de ejemplo
            Reserva reserva = new Reserva();
            Usuario usuario = new Usuario();
            usuario.setCorreo("test@correo.com");

            // Simulamos que generar el comprobante PDF no falla
            PDDocument comprobanteMock = mock(PDDocument.class);
            doReturn(comprobanteMock).when(reservaServicio).generarComprobanteReserva(reserva);

            // Simulamos que se lanza una excepción al enviar el correo
            doThrow(new MessagingException("Error al enviar el correo")).when(reservaServicio).enviarPdfPorCorreo(usuario.getCorreo(), comprobanteMock);

            // Llamamos al método
            reservaServicio.enviarComprobanteReserva(reserva, usuario);

            // Podrías agregar asserts sobre errContent si quieres validar el error capturado
            // Ejemplo:
            // assertTrue(errContent.toString().contains("Error al enviar el correo"));

        } finally {
            // Restaurar System.err
            System.setErr(originalErr);
        }
    }

    @Test
    void testEnviarComprobanteReserva_IOException() throws IOException, MessagingException {
        // Guardar el System.err original
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        try {
            // Crear una reserva y un usuario de ejemplo
            Reserva reserva = new Reserva();
            Usuario usuario = new Usuario();
            usuario.setCorreo("test@correo.com");

            // Simulamos que se lanza una excepción al generar el comprobante PDF
            doThrow(new IOException("Error al generar el archivo PDF")).when(reservaServicio).generarComprobanteReserva(reserva);

            // Llamamos al método
            reservaServicio.enviarComprobanteReserva(reserva, usuario);

            // Si quieres, aquí también podrías validar que el error fue capturado, por ejemplo:
            // assertTrue(errContent.toString().contains("Error al generar el archivo PDF"));

        } finally {
            // Restaurar System.err
            System.setErr(originalErr);
        }
    }



}
