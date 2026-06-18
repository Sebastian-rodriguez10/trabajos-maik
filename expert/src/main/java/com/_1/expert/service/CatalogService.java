package com._1.expert.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._1.expert.dto.HttpGlobalResponse;
import com._1.expert.repository.CatalogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository repository;

    @Transactional
    public HttpGlobalResponse<List<Object>> getCatalog(){
        HttpGlobalResponse<List<Object>> response = new HttpGlobalResponse<>();
        
        List<Object> list = repository.getCatalogCantidad();

        
        response.setMessage("CATALOGO");
        response.setData(list);

        return response;
    }
}
