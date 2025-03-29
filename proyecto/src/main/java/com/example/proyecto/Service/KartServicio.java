package com.example.proyecto.Service;

import com.example.proyecto.Entity.Kart;
import com.example.proyecto.Repository.KartRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KartServicio {
    @Autowired
    private KartRepositorio kartRepositorio;

    //Nuevo kart
    public Kart nuevoKart(String codigo, String modelo, String estado) {
        Kart newKart = new Kart(codigo, modelo, estado);
        Kart existente = kartRepositorio.findByCodigo(newKart.codigo);
        if (existente != null) {
            return null;
        }
        return kartRepositorio.save(newKart);
    }

    // Obtener karts a partir de su estado (disponible, mantenimiento y ocupado)
    public List<Kart> obtenerKartsEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            System.out.println("\"El estado no puede ser nulo o vacío.");
        }
        return kartRepositorio.findByEstado(estado);
    }

    // Cambiar el estado de un kart
    public void cambiarEstado(String codigo, String newEstado) {
        Kart kart = kartRepositorio.findByCodigo(codigo);

        if (kart == null) {
            System.out.println("Kart no encontrado con código: " + codigo);
            return;
        }

        kart.setEstado(newEstado);
        kartRepositorio.save(kart);
        return;
    }

}
