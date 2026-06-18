package com._1.expert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.transaction.annotation.Transactional;

import com._1.expert.entity.Category;
@Transactional
public interface CatalogRepository extends JpaRepository<Category, Integer>{
    @Procedure(procedureName = "listar_categoria_cantidad")
    List<Object> getCatalogCantidad();
    
}