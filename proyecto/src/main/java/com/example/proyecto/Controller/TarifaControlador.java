package com.example.proyecto.Controller;

import com.example.proyecto.Entity.Tarifa;
import com.example.proyecto.Service.TarifaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/tarifa")
public class TarifaControlador {
    @Autowired
    private TarifaServicio tarifaServicio;

    //Crear Tarifa
    @PostMapping("/nuevaTarifa")
    public ResponseEntity<Tarifa> nuevaTarifa(@RequestBody Tarifa tarifa) {
        return ResponseEntity.ok(tarifaServicio.NuevaTarifa(tarifa.getNumeroVueltas(), tarifa.getTiempoMax(), tarifa.getPrecio(), tarifa.getDuracionReserva(), tarifa.getTipo()));
    }

    @GetMapping("/obtenerTarifas")
    public ResponseEntity<List<Tarifa>> obtenerTarifas() {
        return ResponseEntity.ok(tarifaServicio.ObtenerTodasLasTarifas());
    }

    @PutMapping("/modificarTarifa")
    public void modificarTarifa(@RequestBody Tarifa tarifa) {
        tarifaServicio.modificarTarifa(tarifa.getId(), tarifa.getNumeroVueltas(), tarifa.getTiempoMax(), tarifa.getPrecio(), tarifa.getDuracionReserva(), tarifa.getTipo());
    }

    @GetMapping("/obtenerTarifa")
    public ResponseEntity<Tarifa> obtenerTarifa(@RequestParam Long id) {
        return ResponseEntity.ok(tarifaServicio.obtenerTarifa(id));
    }
}