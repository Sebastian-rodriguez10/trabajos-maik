package com._1.expert.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "category")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_catalog", nullable = false)
    private Catalog catalog;

}
