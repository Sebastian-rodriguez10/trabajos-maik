package com._1.expert.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com._1.expert.entity.Favorites;

public interface FavoritesRepository extends JpaRepository<Favorites, Integer>{
    
}
