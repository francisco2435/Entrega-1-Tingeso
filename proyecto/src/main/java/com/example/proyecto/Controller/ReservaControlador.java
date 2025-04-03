package com.example.proyecto.Controller;


import com.example.proyecto.Entity.Reserva;
import com.example.proyecto.Entity.Usuario;
import com.example.proyecto.Service.ReservaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/reserva")
public class ReservaControlador {
    @Autowired
    ReservaServicio reservaServicio;

    @PostMapping("/hacerReserva")
    public ResponseEntity<Reserva> hacerReserva(@RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaServicio.hacerReserva(reserva.rutCliente, reserva.nombreCliente, reserva.fechaReserva, reserva.horaInicio, reserva.tiempoMax,
        reserva.numVueltas, reserva.cantidadPersonas, reserva.rutsAmigos, reserva.nombres));
    }

    @GetMapping("/obtenerReservas")
    public ResponseEntity<List<Reserva>> obtenerReservas() {
        return ResponseEntity.ok(reservaServicio.ObtenerReservas());
    }
}
