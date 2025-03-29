package com.example.proyecto.Repository;


import com.example.proyecto.Entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TarifaRepositorio extends JpaRepository<Tarifa, Long> {

}
