package com._1.gestorDeTareas.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com._1.gestorDeTareas.entity.Taks;
import java.util.List;



public interface TaksRepository extends JpaRepository<Taks, Long>{
    //Optional<Taks> updateByState(TaksStateEnum state);

    List<Taks> findByState(String state);

    List<Taks> findByPriority(String priority);
}
