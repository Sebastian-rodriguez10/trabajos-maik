package com._1.tiendaApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._1.tiendaApp.dto.CategoryRequestDTO;
import com._1.tiendaApp.dto.CategoryResponseDTO;
import com._1.tiendaApp.dto.HttpGlobalResponseDTO;
import com._1.tiendaApp.enums.RoleEnum;
import com._1.tiendaApp.security.RequiresRole;
import com._1.tiendaApp.service.CategoriesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/categories")
@RestController
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoriesService service;

    /**
     * metodo para consultar todas las categorias
     * @return lista de categorias
     */
    @RequiresRole({RoleEnum.ADMIN, RoleEnum.CLIENTE, RoleEnum.VENDEDOR})
    @GetMapping
    public ResponseEntity<HttpGlobalResponseDTO<List<CategoryResponseDTO>>> getAllCategories() {
        HttpGlobalResponseDTO<List<CategoryResponseDTO>> response = service.getAllCategories();
        return ResponseEntity.ok(response);
    }

    /**
     * metodo para crear una categoria
     * @param request el nombre de la categoria
     * @return la categoria creada
     */
    @RequiresRole(RoleEnum.ADMIN)
    @PostMapping
    public ResponseEntity<HttpGlobalResponseDTO<CategoryResponseDTO>> createCategory(@Valid @RequestBody CategoryRequestDTO request) {
        HttpGlobalResponseDTO<CategoryResponseDTO> response = service.createCategory(request);
        return ResponseEntity.ok(response);
    }

    /**
     * metodo para eliminar una categoria si existe
     * @param id el id de la categoria que se va a eliminar
     * @return categoria eliminada correctamente
     */
    @RequiresRole(RoleEnum.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpGlobalResponseDTO<Void>> deleteCategory(@PathVariable Long id) {
        HttpGlobalResponseDTO<Void> response = service.deleteCategory(id);
        return ResponseEntity.ok(response);
    }
}