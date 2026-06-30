package com._1.gestorDeTareas.dto;

import java.time.LocalDateTime;

import com._1.gestorDeTareas.enums.TaksPriorityEnum;
import com._1.gestorDeTareas.enums.TaksStateEnum;

import lombok.Data;

@Data
public class AllTaksResponseDTO {
    private String titulo;
    private String descripcion;
    private TaksStateEnum estado;
    private TaksPriorityEnum prioridad;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdated;
}
