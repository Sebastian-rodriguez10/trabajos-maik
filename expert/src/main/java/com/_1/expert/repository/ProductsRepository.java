package com._1.expert.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com._1.expert.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Integer>{
    
}
