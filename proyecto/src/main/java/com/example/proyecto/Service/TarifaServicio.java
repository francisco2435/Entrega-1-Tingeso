package com.example.proyecto.Service;

import com.example.proyecto.Entity.Tarifa;
import com.example.proyecto.Repository.TarifaRepositorio;
import com.example.proyecto.Repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class TarifaServicio {
    @Autowired
    TarifaRepositorio tarifaRepositorio;

    @Autowired
    public TarifaServicio(TarifaRepositorio tarifaRepositorio) {
        this.tarifaRepositorio = tarifaRepositorio;
    }

    // Crear una tarifa
    public Tarifa NuevaTarifa(int numeroVueltas, int tiempoMax, Double precio, int duracionReserva, String tipo){

        if(!(Objects.equals(tipo, "normal") || Objects.equals(tipo, "dia especial") || Objects.equals(tipo, "fin de semana"))){
            throw new IllegalArgumentException("la tarifa debe ser de tipo normal, dia especial o fin de semana");
        }

        if(tiempoMax < 0){
            throw new IllegalArgumentException(" El tiempo maximo permitido debe ser positivo");
        }

        if(precio < 0){
            throw new IllegalArgumentException(" El precio debe ser positivo");
        }

        if(duracionReserva < tiempoMax){
            throw new IllegalArgumentException("La duracion total de la reserva debe ser mayor que el tiempo máximo permitido");
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
            throw new IllegalArgumentException("La tarifa no existe");
        }

        if(!(Objects.equals(nuevoTipo, "normal") || Objects.equals(nuevoTipo, "dia especial") || Objects.equals(nuevoTipo, "fin de semana"))){
            throw new IllegalArgumentException("la tarifa debe ser de tipo normal, dia especial o fin de semana");
        }

        if(nuevoTiempomax < 0){
            throw new IllegalArgumentException(" El tiempo maximo permitido debe ser positivo");
        }

        if(nuevoPrecio < 0){
            throw new IllegalArgumentException(" El precio debe ser positivo");
        }

        if(nuevaDuracion < nuevoTiempomax){
            throw new IllegalArgumentException("La duración total de la reserva debe ser mayor que el tiempo máximo permitido");
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