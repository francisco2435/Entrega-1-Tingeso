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
    public Usuario registrarUsuario(String nombre,String rut, String correo, String telefono, String rol, String contrasenia, LocalDate fechaNacimiento) {
        Usuario newUsuario = new Usuario(nombre, rut, correo, telefono, rol, contrasenia, fechaNacimiento);
        Usuario existente = usuarioRepositorio.findByCorreo(newUsuario.correo);
        Usuario existente2 = usuarioRepositorio.findByRut(newUsuario.rut);
        if(existente != null || existente2 != null) {
            return null;
        }
        return usuarioRepositorio.save(newUsuario);
    }

    // Login usuario
    public Usuario LoginUsuario(String correo, String contrasenia) {
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        //Comprobar que el usuario esté registrado con el correo ingresado
        if(usuario == null) {
            System.out.println("Usuario no encontrado");
            return null;
        }
        //Comprobar que la contraseña ingresada sea correcta
        if(!usuario.contrasenia.equals(contrasenia)) {
            System.out.println("Contraseñas no coinciden");
            return null;
        }
        //Retornar el usuario logueado
        return usuario;
    }

}
