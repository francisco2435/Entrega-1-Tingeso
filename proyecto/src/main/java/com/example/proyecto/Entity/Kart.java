package com.example.proyecto.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="kart")
public class Kart {
    //Atributos
    @Id //Identificador
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String codigo;
    public String modelo;

    @Setter
    public String estado;

    //Constructores
    public Kart() {
    }

    public Kart(String codigo, String modelo, String estado) {
        this.codigo = codigo;
        this.modelo = modelo;
        this.estado = estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
