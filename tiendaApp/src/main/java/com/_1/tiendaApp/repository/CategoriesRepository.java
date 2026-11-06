package com._1.tiendaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com._1.tiendaApp.entity.Categories;


public interface CategoriesRepository extends JpaRepository<Categories, Long>{
    Boolean existsByName(String name);
}
