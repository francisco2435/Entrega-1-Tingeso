package com.example.proyecto.Controller;

import com.example.proyecto.Entity.Kart;
import com.example.proyecto.Service.KartServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/kart")
public class KartControlador {
    @Autowired
    private KartServicio kartServicio;

    //Crear nuevo kart
    @PostMapping("/nuevoKart")
    public ResponseEntity<Kart> nuevoKart(@RequestBody Kart kart) {
        return ResponseEntity.ok(kartServicio.nuevoKart(kart.getCodigo(), kart.getModelo(), kart.getEstado()));
    }

    // Obtener karts a partir de su estado (disponible, mantenimiento y ocupado)
    @GetMapping("/getKartsEstado")
    public ResponseEntity<List<Kart>> getKartsEstado(@RequestParam String estado) {
        return ResponseEntity.ok(kartServicio.obtenerKartsEstado(estado));
    }

    // Cambiar el estado de un kart
    @PutMapping("/cambiarEstado")
    public void cambiarEstado(@RequestParam String codigo, @RequestParam String newEstado) {
        kartServicio.cambiarEstado(codigo, newEstado);
        return;
    }


}
