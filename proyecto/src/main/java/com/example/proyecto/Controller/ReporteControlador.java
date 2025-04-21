package com.example.proyecto.Controller;

import com.example.proyecto.Entity.Reporte;
import com.example.proyecto.Service.ReporteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/reporte")
public class ReporteControlador {
    @Autowired
    ReporteServicio reporteServicio;

    @PostMapping("/hacerReporte")
    public ResponseEntity<Reporte> hacerReporte(@RequestBody Reporte reporte) {
        return ResponseEntity.ok(reporteServicio.crearReporte(reporte.getTipo(), reporte.getFechaInicio(), reporte.getFechaFin()));
    }
}
