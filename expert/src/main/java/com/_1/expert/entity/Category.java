package com._1.expert.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "category")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "id_catalog")
    private Integer idCatalog;

}
