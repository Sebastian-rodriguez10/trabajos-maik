package com._1.equipoFutbol5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com._1.equipoFutbol5.entity.Entrenamiento;

@Repository
public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Integer>{
    long countByNumeroEntrenamiento(Integer numeroEntrenamiento);

    @Query(value = "call equipo_titular()", nativeQuery = true)
    List<Object[]> obtenerTitular();
} 
    

