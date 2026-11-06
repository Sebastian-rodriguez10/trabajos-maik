package com._1.tiendaApp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com._1.tiendaApp.enums.StatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Table(name = "orders")
@Data 
public class Orders {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total")
    private BigDecimal total;

    @Column (name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column (name = "created_at")
    private LocalDateTime localDateTime;

    @PrePersist
    public void prepersist(){
        this.localDateTime = LocalDateTime.now();
    }



}
