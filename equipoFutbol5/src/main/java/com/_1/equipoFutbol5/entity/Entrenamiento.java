package com._1.equipoFutbol5.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "entrenamiento1")
public class Entrenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_entrenamiento")
    @NotNull
    private Integer numeroEntrenamiento;

    @NotNull
    @Column(name = "jugador")
    private String jugador;

    @NotNull
    @Column(name = "potencia_tiro")
    private Double potenciaTiro;

    @NotNull
    @Column(name = "velocidad")
    private Double velocidad;

    @NotNull
    @Column(name = "pases")
    private Integer pases;

    @NotNull
    @Column(name = "resultado")
    private Double resultado;
}
