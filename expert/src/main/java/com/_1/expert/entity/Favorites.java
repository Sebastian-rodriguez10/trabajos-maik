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

    @Column(name = "id_customers")
    private Integer idCustomer;
    
    @Column(name = "id_product")
    private Integer idProduct;
}