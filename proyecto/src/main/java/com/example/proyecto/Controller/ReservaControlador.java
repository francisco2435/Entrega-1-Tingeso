package com.example.proyecto.Controller;


import com.example.proyecto.Entity.Reserva;
import com.example.proyecto.Service.ReservaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/reserva")
public class ReservaControlador {
    @Autowired
    ReservaServicio reservaServicio;

    @PostMapping("/hacerReserva")
    public ResponseEntity<Reserva> hacerReserva(@RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaServicio.hacerReserva(reserva.getRutCliente(), reserva.getNombreCliente(), reserva.getFechaReserva(), reserva.getHoraInicio(), reserva.getTiempoMax(),
        reserva.getNumVueltas(), reserva.getCantidadPersonas(), reserva.getRutsAmigos(), reserva.getNombres()));
    }

    @GetMapping("/obtenerReservas")
    public ResponseEntity<List<Reserva>> obtenerReservas() {
        return ResponseEntity.ok(reservaServicio.ObtenerReservas());
    }
}
