package com._1.tiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com._1.tiendaApp.entity.Products;
import java.util.List;
import java.util.Optional;


public interface  ProductsRepository extends JpaRepository<Products, Long>{
    boolean existsByIdCategory(Long idCategory);
    List<Products> findByIdCategory(Long idCategory);

    List<Products> findBySellerId(Long sellerId);

    @Query("SELECT p FROM Products p WHERE p.id = :id")
    Optional<Products> findByIdForUpdate(@Param("id") Long id);
}
