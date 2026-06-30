package com._1.expert.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._1.expert.dto.HttpGlobalResponse;
import com._1.expert.dto.category.AllCategoryDTO;
import com._1.expert.service.CategoryService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/allCategory")
    public ResponseEntity<HttpGlobalResponse<List<AllCategoryDTO>>> getCategory() {
        try {
            HttpGlobalResponse<List<AllCategoryDTO>> response = service.getCategory();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            HttpGlobalResponse<List<AllCategoryDTO>> error = new HttpGlobalResponse<>();
            error.setMessage("Hubo un error en la peticion");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        
    }
    
    
}
