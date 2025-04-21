package com.example.proyecto.Controller;

import com.example.proyecto.Entity.Usuario;
import com.example.proyecto.Service.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuario")
public class UsuarioControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;

    //Crear nuevo usuario
    @PostMapping("/nuevousuario")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioServicio.registrarUsuario(usuario.nombre, usuario.rut, usuario.correo, usuario.telefono, usuario.rol, usuario.contrasenia, usuario.fechaNacimiento));
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam String correo, @RequestParam String contrasenia) {
        return ResponseEntity.ok(usuarioServicio.LoginUsuario(correo, contrasenia));
    }

}
