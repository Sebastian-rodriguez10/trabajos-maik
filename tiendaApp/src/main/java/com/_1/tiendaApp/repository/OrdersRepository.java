package com._1.tiendaApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._1.tiendaApp.entity.Orders;

public interface  OrdersRepository extends JpaRepository<Orders, Long>{
    @Query("SELECT o FROM Orders o WHERE o.productId IN (SELECT p.id FROM Products p WHERE p.sellerId = :sellerId)")
    List<Orders> findByProductSellerId(@Param("sellerId") Long sellerId);
    List<Orders> findByClientId(Long clientId);

    boolean existsByProductId(Long productId);
}
