package com._1.expert.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;


import com._1.expert.service.FavoritesService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoritesController {
    private final FavoritesService favoritesService;

    @GetMapping("/allFavorites")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    
}

