package com.example.proyecto.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Entity
@Getter @Setter
@Table(name="usuario")
public class Usuario {
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id autoincremental
    private Long id;
    public String nombre;
    @Getter
    public String correo;
    public String telefono;
    public String rol;
    public String contrasenia;
    public LocalDate fechaNacimiento;

    //Constructores
    public Usuario() {
    }

    public Usuario(String nombre, String correo, String telefono, String rol, String contrasenia, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.rol = rol;
        this.contrasenia = contrasenia;
        this.fechaNacimiento = fechaNacimiento;
    }
}
