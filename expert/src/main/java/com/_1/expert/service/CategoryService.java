package com._1.expert.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com._1.expert.dto.HttpGlobalResponse;
import com._1.expert.dto.category.AllCategoryDTO;
import com._1.expert.entity.Category;
import com._1.expert.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public HttpGlobalResponse<List<AllCategoryDTO>> getCategory(){

        HttpGlobalResponse<List<AllCategoryDTO>> response = new HttpGlobalResponse<>();

        List<Category> categoryFound = repository.findAll();

        List<AllCategoryDTO> list = new ArrayList<>();
        
        for (Category category : categoryFound) {

            AllCategoryDTO allCategory = new AllCategoryDTO();

            allCategory.setIdCategory(category.getId());
            allCategory.setNameCatalog(category.getCatalog().getName());
            allCategory.setName(category.getName());
            list.add(allCategory);
            
        }

        response.setMessage("CATEGORIAS");
        response.setData(list);
        return response;
    }
}
