package com._1.expert.controller;

import org.springframework.web.bind.annotation.RestController;

import com._1.expert.dto.HttpGlobalResponse;
import com._1.expert.dto.catalog.AllCatalogDTO;
import com._1.expert.service.CatalogService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService service;
    @GetMapping("/allCatalog")
    public HttpGlobalResponse<List<Object>> getCatalog() {
        HttpGlobalResponse<List<Object> > response = service.getCatalog();
        return response;
    }
    
}