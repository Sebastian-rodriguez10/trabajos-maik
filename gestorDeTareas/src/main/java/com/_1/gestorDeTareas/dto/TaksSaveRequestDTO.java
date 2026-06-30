package com._1.gestorDeTareas.dto;


import com._1.gestorDeTareas.enums.TaksPriorityEnum;

import lombok.Data;

@Data
public class TaksSaveRequestDTO {
    private String titulo;
    private String descripcion;
    private TaksPriorityEnum prioridad;
}
