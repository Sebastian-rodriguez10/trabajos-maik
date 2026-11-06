package com._1.tiendaApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com._1.tiendaApp.dto.CategoryRequestDTO;
import com._1.tiendaApp.dto.CategoryResponseDTO;
import com._1.tiendaApp.dto.HttpGlobalResponseDTO;
import com._1.tiendaApp.entity.Categories;
import com._1.tiendaApp.exception.ConflictException;
import com._1.tiendaApp.exception.ResourceNotFoundException;
import com._1.tiendaApp.repository.CategoriesRepository;
import com._1.tiendaApp.repository.ProductsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    private final ProductsRepository productRepository; 

    public HttpGlobalResponseDTO<List<CategoryResponseDTO>> getAllCategories() {
        HttpGlobalResponseDTO<List<CategoryResponseDTO>> response = new HttpGlobalResponseDTO<>();

        List<Categories> listCategories = categoriesRepository.findAll();
        List<CategoryResponseDTO> dtoList = new ArrayList<>();

        for (Categories category : listCategories) {
            CategoryResponseDTO dto = new CategoryResponseDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dtoList.add(dto);
        }

        response.setMessage("Categorías encontradas exitosamente");
        response.setSuccess(true);
        response.setData(dtoList);

        return response;
    }

    public HttpGlobalResponseDTO<CategoryResponseDTO> createCategory(CategoryRequestDTO request) {
        HttpGlobalResponseDTO<CategoryResponseDTO> response = new HttpGlobalResponseDTO<>();

        if (categoriesRepository.existsByName(request.getName())) {
            throw new ConflictException("La categoría ya existe con ese nombre");
        }

        Categories category = new Categories();
        category.setName(request.getName());
        categoriesRepository.save(category);

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        response.setMessage("Categoría creada exitosamente");
        response.setSuccess(true);
        response.setData(dto);

        return response;
    }

    public HttpGlobalResponseDTO<Void> deleteCategory(Long id) {
        HttpGlobalResponseDTO<Void> response = new HttpGlobalResponseDTO<>();

        Categories category = categoriesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        boolean hasProducts = productRepository.existsByIdCategory(id);
        if (hasProducts) {
            throw new ConflictException("No se puede eliminar la categoría porque tiene productos asociados");
        }

        categoriesRepository.delete(category);

        response.setMessage("Categoría eliminada exitosamente");
        response.setSuccess(true);
        response.setData(null);

        return response;
    }
}