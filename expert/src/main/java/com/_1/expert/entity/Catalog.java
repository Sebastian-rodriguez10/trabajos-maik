package com._1.expert.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "catalog")
@Data
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
