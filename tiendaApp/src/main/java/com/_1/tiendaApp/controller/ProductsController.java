package com._1.tiendaApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._1.tiendaApp.dto.HttpGlobalResponseDTO;
import com._1.tiendaApp.dto.ProductRequestDTO;
import com._1.tiendaApp.dto.ProductResponseDTO;
import com._1.tiendaApp.enums.RoleEnum;
import com._1.tiendaApp.security.RequiresRole;
import com._1.tiendaApp.service.ProductsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService service;

    /**
     * 
     * @param categoryId
     * @return
     */
    @GetMapping
    public ResponseEntity<HttpGlobalResponseDTO<List<ProductResponseDTO>>> getAllProducts(@RequestParam(required = false) Long categoryId) {
        HttpGlobalResponseDTO<List<ProductResponseDTO>> response = service.getAllProducts(categoryId);
        return ResponseEntity.ok(response);
    }

    /**
     * metodo para consultar un solo producto
     * @param id id del producto
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<HttpGlobalResponseDTO<ProductResponseDTO>> getProductById(@PathVariable Long id) {
        HttpGlobalResponseDTO<ProductResponseDTO> response = service.getProductById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * obtener los productos de un solo vendedor
     * @param request
     * @return
     */
    @RequiresRole(RoleEnum.VENDEDOR)
    @GetMapping("/mine")
    public ResponseEntity<HttpGlobalResponseDTO<List<ProductResponseDTO>>> getMyProducts(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<ProductResponseDTO>> response = service.getMyProducts(request);
        return ResponseEntity.ok(response);
    }

    /**
     * crear un nuevo producto
     * @param requestDTO
     * @param request
     * @return
     */
    @RequiresRole(RoleEnum.VENDEDOR)
    @PostMapping
    public ResponseEntity<HttpGlobalResponseDTO<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO, HttpServletRequest request) {
        HttpGlobalResponseDTO<ProductResponseDTO> response = service.createProduct(requestDTO, request);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * metodo para actualizar producto
     * @param id es el id del producto que se va a actualizar
     * @param requestDTO los campos a actualizar 
     * @param request 
     * @return
     */
    @RequiresRole({RoleEnum.VENDEDOR, RoleEnum.ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<HttpGlobalResponseDTO<ProductResponseDTO>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO requestDTO, HttpServletRequest request) {
        HttpGlobalResponseDTO<ProductResponseDTO> response = service.updateProduct(id, requestDTO, request);
        return ResponseEntity.ok(response);
    }

    /**
     * borrar un producto
     * @param id id del producto
     * @param request
     * @return
     */
    @RequiresRole({RoleEnum.VENDEDOR, RoleEnum.ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpGlobalResponseDTO<Void>> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        HttpGlobalResponseDTO<Void> response = service.deleteProduct(id, request);
        return ResponseEntity.ok(response);
    }
}