package com.example.proyecto.Repository;

import com.example.proyecto.Entity.Kart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KartRepositorio extends JpaRepository<Kart, Long> {
    Kart findByCodigo(String codigo);
    List<Kart> findByEstado(String estado);
}
