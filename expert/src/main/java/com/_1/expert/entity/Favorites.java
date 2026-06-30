package com._1.expert.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorites")
@Data
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_customers")
    private Customers customer;
    
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Products product;
}