package com.example.proyecto.Service;

import com.example.proyecto.Entity.Tarifa;
import com.example.proyecto.Repository.TarifaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class TarifaServicio {
    @Autowired
    TarifaRepositorio tarifaRepositorio;

    // Crear una tarifa
    public Tarifa NuevaTarifa(int numeroVueltas, int tiempoMax, Double precio, int duracionReserva, String tipo){

        if(!(Objects.equals(tipo, "normal") || Objects.equals(tipo, "dia especial") || Objects.equals(tipo, "fin de semana"))){
            System.out.println("la tarifa debe ser de tipo normal, dia especial o fin de semana");
            return null;
        }

        if(tiempoMax < 0){
            System.out.println(" El tiempo maximo permitido debe ser positivo");
            return null;
        }

        if(precio < 0){
            System.out.println(" El precio debe ser positivo");
            return null;
        }

        if(duracionReserva < tiempoMax){
            System.out.println("La duracion total de la reserva debe ser mayor que el tiempo máximo permitido");
            return null;
        }

        Tarifa tarifa = new Tarifa(numeroVueltas, tiempoMax, precio, duracionReserva, tipo);

        return tarifaRepositorio.save(tarifa);
    }

    public List<Tarifa> ObtenerTodasLasTarifas(){
        return tarifaRepositorio.findAll();
    }

    //modificar alguna caracteristica de una tarifa
    public void modificarTarifa(Long id, int nuevasVueltas, int nuevoTiempomax, double nuevoPrecio, int nuevaDuracion, String nuevoTipo){
        Tarifa tarifa = tarifaRepositorio.findById(id).get();
        if(tarifa == null){
            System.out.println("La tarifa no existe");
            return;
        }

        if(!(Objects.equals(nuevoTipo, "normal") || Objects.equals(nuevoTipo, "dia especial") || Objects.equals(nuevoTipo, "fin de semana"))){
            System.out.println("la tarifa debe ser de tipo normal, dia especial o fin de semana");
            return;
        }

        if(nuevoTiempomax < 0){
            System.out.println(" El tiempo maximo permitido debe ser positivo");
            return;
        }

        if(nuevoPrecio < 0){
            System.out.println(" El precio debe ser positivo");
            return;
        }

        if(nuevaDuracion < nuevoTiempomax){
            System.out.println("La duración total de la reserva debe ser mayor que el tiempo máximo permitido");
            return;
        }

        tarifa.setNumeroVueltas(nuevasVueltas);
        tarifa.setTiempoMax(nuevoTiempomax);
        tarifa.setPrecio(nuevoPrecio);
        tarifa.setDuracionReserva(nuevaDuracion);
        tarifa.setTipo(nuevoTipo);

        tarifaRepositorio.save(tarifa);
        return;
    }

    public Tarifa obtenerTarifa(Long id){
        return tarifaRepositorio.findById(id).get();
    }
}