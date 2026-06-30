package com._1.gestorDeTareas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com._1.gestorDeTareas.entity.Taks;
import com._1.gestorDeTareas.enums.TaksStateEnum;
import java.util.List;


public interface TaksRepository extends JpaRepository<Taks, Long>{
    //Optional<Taks> updateByState(TaksStateEnum state);

    List<Taks> findByState(TaksStateEnum state);
}
