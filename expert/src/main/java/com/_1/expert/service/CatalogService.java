package com._1.expert.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._1.expert.dto.HttpGlobalResponse;
import com._1.expert.dto.catalog.AllCatalogDTO;
import com._1.expert.entity.Products;
import com._1.expert.repository.ProductsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final ProductsRepository productRepository;

    @Transactional
    public HttpGlobalResponse<List<AllCatalogDTO>> getCatalog(){

        HttpGlobalResponse<List<AllCatalogDTO>> response = new HttpGlobalResponse<>();

        List<Products> productFound = productRepository.findAll(); 

        List<AllCatalogDTO> list = new ArrayList<>();

        for (Products p : productFound) {
            AllCatalogDTO dto = new AllCatalogDTO();
            
            dto.setNameCatalog(p.getCategory().getCatalog().getName());
            dto.setCategoryName(p.getCategory().getName());
            dto.setPrice(p.getPrice());
            dto.setProductStock(p.getStock());
            dto.setProductName(p.getName());

            list.add(dto);
        }
        
        response.setMessage("CATALOGO DE LA EMPRESA");
        response.setData(list);
        return response;

    }
}
