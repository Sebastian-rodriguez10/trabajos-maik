package com._1.expert.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com._1.expert.dto.HttpGlobalResponse;
import com._1.expert.dto.favorites.FavoriteRequest;
import com._1.expert.entity.Favorites;
import com._1.expert.service.FavoritesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FavoritesController {
    private final FavoritesService favoritesService;

    @GetMapping
    public HttpGlobalResponse<List<Favorites>> getAll() {
        return favoritesService.getAllFavorites();
    }

    @GetMapping("/{id}")
    public HttpGlobalResponse<Object> getById(@PathVariable Integer id) {
        return favoritesService.getFavoriteById(id);
    }

    @PostMapping
    public HttpGlobalResponse<Object> save(@RequestBody FavoriteRequest request) {
        return favoritesService.saveFavorite(request.getCustomerId(), request.getProductId());
    }

    @PutMapping("/{id}")
    public HttpGlobalResponse<Object> update(@PathVariable Integer id, @RequestBody FavoriteRequest request) {

        return favoritesService.updateFavorite(id, request.getCustomerId(), request.getProductId()
        );
    }

    @DeleteMapping("/{id}")
    public HttpGlobalResponse<Object> delete(@PathVariable Integer id) {
        return favoritesService.deleteFavorite(id);
    }
}

