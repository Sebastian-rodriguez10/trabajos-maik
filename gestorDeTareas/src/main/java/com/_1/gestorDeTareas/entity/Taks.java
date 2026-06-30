package com._1.gestorDeTareas.entity;

import java.time.LocalDateTime;

import com._1.gestorDeTareas.enums.TaksStateEnum;
import com._1.gestorDeTareas.enums.TaksPriorityEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "tareas")
@Data
public class Taks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "el titulo es obligatorio")
    @Column(name = "titulo")
    private String title;

    @NotBlank(message = "la descripcion es obligatoria")
    @Column(name = "descripcion")
    private String description;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private TaksStateEnum state;

    @NotNull(message = "la prioridad es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad")
    private TaksPriorityEnum priority;

    @Column(name = "fecha_creacion")
    private LocalDateTime creationDate;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime lastUpdated;

    @PrePersist
    public void prePersist(){
        this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.lastUpdated = LocalDateTime.now();
    }
}
