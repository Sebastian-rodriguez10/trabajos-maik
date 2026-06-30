package com._1.expert.controller;

import org.springframework.web.bind.annotation.RestController;

import com._1.expert.dto.HttpGlobalResponse;
import com._1.expert.dto.catalog.AllCatalogDTO;
import com._1.expert.service.CatalogService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService service;

    @GetMapping("/allCatalog")
    public ResponseEntity<HttpGlobalResponse<List<AllCatalogDTO>>> getCatalog() {

        try {

            HttpGlobalResponse<List<AllCatalogDTO>> response = service.getCatalog();
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {

            HttpGlobalResponse<List<AllCatalogDTO>> error = new HttpGlobalResponse<>();
            error.setMessage("Error en la peticion");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            
        }

    }

}