package com._1.expert.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com._1.expert.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
