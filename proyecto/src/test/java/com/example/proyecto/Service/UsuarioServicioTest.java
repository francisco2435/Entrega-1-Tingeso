package com.example.proyecto.Service;

import com.example.proyecto.Entity.Usuario;
import com.example.proyecto.Repository.KartRepositorio;
import com.example.proyecto.Repository.UsuarioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UsuarioServicioTest {

    private UsuarioRepositorio usuarioRepositorio;
    private UsuarioServicio usuarioServicio;

    @BeforeEach
    void setUp() {
        usuarioRepositorio = Mockito.mock(UsuarioRepositorio.class);
        usuarioServicio = new UsuarioServicio(usuarioRepositorio);
    }

    @Test
    void whenRegisterUser_thenUserIsSaved() {
        // Given
        String nombre = "Carlos";
        String rut = "13.777.548-2";
        String correo = "carlos@example.com";
        String telefono = "123456789";
        String rol = "admin";
        String contrasenia = "password";
        LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);

        when(usuarioRepositorio.findByCorreo(correo)).thenReturn(null);
        when(usuarioRepositorio.findByRut(rut)).thenReturn(null);

        Usuario usuarioMock = new Usuario(nombre, rut, correo, telefono, rol, contrasenia, fechaNacimiento);
        when(usuarioRepositorio.save(Mockito.any(Usuario.class))).thenReturn(usuarioMock);

        // When
        Usuario usuarioRegistrado = usuarioServicio.registrarUsuario(nombre, rut, correo, telefono, rol, contrasenia, fechaNacimiento);

        // Then
        assertThat(usuarioRegistrado).isNotNull();
        assertThat(usuarioRegistrado.getCorreo()).isEqualTo(correo);
        verify(usuarioRepositorio, times(1)).save(Mockito.any(Usuario.class));
    }

    @Test
    void whenRegisterUserWithExistingCorreo_thenThrowException() {
        // Given
        String correo = "existing@example.com";
        when(usuarioRepositorio.findByCorreo(correo)).thenReturn(new Usuario());

        // Then
        assertThatThrownBy(() -> usuarioServicio.registrarUsuario("Nombre", "11.111.111-1", correo, "123456789", "user", "pass", LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un usuario");
    }

    @Test
    void whenRegisterUserWithExistingRut_thenThrowException() {
        // Given
        String rut = "13.777.548-2";
        when(usuarioRepositorio.findByCorreo(anyString())).thenReturn(null);
        when(usuarioRepositorio.findByRut(rut)).thenReturn(new Usuario());

        // Then
        assertThatThrownBy(() -> usuarioServicio.registrarUsuario("Nombre", rut, "nuevo@example.com", "123456789", "user", "pass", LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un usuario");
    }

    @Test
    void whenLoginWithValidCredentials_thenReturnUser() {
        // Given
        String correo = "carlos@example.com";
        String contrasenia = "password";
        Usuario usuario = new Usuario("Carlos", "13.777.548-2", correo, "123456789", "admin", contrasenia, LocalDate.of(1990, 1, 1));
        when(usuarioRepositorio.findByCorreo(correo)).thenReturn(usuario);

        // When
        Usuario usuarioLogueado = usuarioServicio.LoginUsuario(correo, contrasenia);

        // Then
        assertThat(usuarioLogueado).isNotNull();
        assertThat(usuarioLogueado.getCorreo()).isEqualTo(correo);
    }

    @Test
    void whenLoginWithWrongEmail_thenThrowException() {
        // Given
        String correo = "notfound@example.com";
        when(usuarioRepositorio.findByCorreo(correo)).thenReturn(null);

        // Then
        assertThatThrownBy(() -> usuarioServicio.LoginUsuario(correo, "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void whenLoginWithWrongPassword_thenThrowException() {
        // Given
        String correo = "carlos@example.com";
        Usuario usuario = new Usuario("Carlos", "13.777.548-2", correo, "123456789", "admin", "correctpassword", LocalDate.of(1990, 1, 1));
        when(usuarioRepositorio.findByCorreo(correo)).thenReturn(usuario);

        // Then
        assertThatThrownBy(() -> usuarioServicio.LoginUsuario(correo, "wrongpassword"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Contraseñas no coinciden");
    }
}
