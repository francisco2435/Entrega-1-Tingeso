package com.example.proyecto.Service;


import com.example.proyecto.Entity.Usuario;
import com.example.proyecto.Repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UsuarioServicio {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    //Nuevo Usuario
    public Usuario registrarUsuario(String nombre, String rut, String correo, String telefono, String rol, String contrasenia, LocalDate fechaNacimiento) {
        if (usuarioRepositorio.findByCorreo(correo) != null || usuarioRepositorio.findByRut(rut) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo o RUT.");
        }

        Usuario nuevoUsuario = new Usuario(nombre, rut, correo, telefono, rol, contrasenia, fechaNacimiento);
        return usuarioRepositorio.save(nuevoUsuario);
    }

    // Login usuario
    public Usuario LoginUsuario(String correo, String contrasenia) {
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        //Comprobar que el usuario esté registrado con el correo ingresado
        if(usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        //Comprobar que la contraseña ingresada sea correcta
        if(!usuario.contrasenia.equals(contrasenia)) {
            throw new IllegalArgumentException("Contraseñas no coinciden");
        }
        //Retornar el usuario logueado
        return usuario;
    }

}
