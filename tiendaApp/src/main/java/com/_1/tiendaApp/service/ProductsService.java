package com._1.tiendaApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com._1.tiendaApp.dto.HttpGlobalResponseDTO;
import com._1.tiendaApp.dto.ProductRequestDTO;
import com._1.tiendaApp.dto.ProductResponseDTO;
import com._1.tiendaApp.entity.Categories;
import com._1.tiendaApp.entity.Products; // Ajusta según tu entidad de producto
import com._1.tiendaApp.entity.Users;
import com._1.tiendaApp.enums.RoleEnum;
import com._1.tiendaApp.exception.AccessDeniedException; // O tu excepción personalizada para prohibido (403)
import com._1.tiendaApp.exception.ResourceNotFoundException;
import com._1.tiendaApp.repository.CategoriesRepository;
import com._1.tiendaApp.repository.ProductsRepository;
import com._1.tiendaApp.repository.UsersRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductsRepository productRepository;
    private final CategoriesRepository categoryRepository;
    private final UsersRepository usersRepository;

    public HttpGlobalResponseDTO<List<ProductResponseDTO>> getAllProducts(Long categoryId) {
        HttpGlobalResponseDTO<List<ProductResponseDTO>> response = new HttpGlobalResponseDTO<>();
        
        List<Products> products;
        if (categoryId != null) {
            products = productRepository.findByIdCategory(categoryId);
        } else {
            products = productRepository.findAll();
        }

        List<ProductResponseDTO> dtoList = mapToDtoList(products);

        response.setMessage("Productos consultados exitosamente");
        response.setSuccess(true);
        response.setData(dtoList);

        return response;
    }

    public HttpGlobalResponseDTO<ProductResponseDTO> getProductById(Long id) {
        HttpGlobalResponseDTO<ProductResponseDTO> response = new HttpGlobalResponseDTO<>();

        Products product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        response.setMessage("Producto encontrado");
        response.setSuccess(true);
        response.setData(mapToDto(product));

        return response;
    }

    public HttpGlobalResponseDTO<List<ProductResponseDTO>> getMyProducts(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<ProductResponseDTO>> response = new HttpGlobalResponseDTO<>();

        Long sellerId = (Long) request.getAttribute("userId");
        List<Products> products = productRepository.findBySellerId(sellerId);

        List<ProductResponseDTO> dtoList = mapToDtoList(products);

        response.setMessage("Tus productos consultados exitosamente");
        response.setSuccess(true);
        response.setData(dtoList);

        return response;
    }

    // 4. Crear producto (El ID del vendedor se extrae del token)[cite: 6]
    public HttpGlobalResponseDTO<ProductResponseDTO> createProduct(ProductRequestDTO requestDTO, HttpServletRequest request) {
        HttpGlobalResponseDTO<ProductResponseDTO> response = new HttpGlobalResponseDTO<>();

        Long sellerId = (Long) request.getAttribute("userId");

        Categories category = categoryRepository.findById(requestDTO.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        Products product = new Products();
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());
        product.setIdCategory(category.getId());
        product.setSellerId(sellerId);

        Products savedProduct = productRepository.save(product);

        response.setMessage("Producto creado exitosamente");
        response.setSuccess(true);
        response.setData(mapToDto(savedProduct));

        return response;
    }

    public HttpGlobalResponseDTO<ProductResponseDTO> updateProduct(Long id, ProductRequestDTO requestDTO, HttpServletRequest request) {
        HttpGlobalResponseDTO<ProductResponseDTO> response = new HttpGlobalResponseDTO<>();

        Products product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrada"));

        validarPermisoPropietarioOAdmin(product, request);

        Categories category = categoryRepository.findById(requestDTO.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());
        product.setIdCategory(category.getId());

        productRepository.save(product);

        response.setMessage("Producto actualizado exitosamente");
        response.setSuccess(true);
        response.setData(mapToDto(product));

        return response;
    }

    public HttpGlobalResponseDTO<Void> deleteProduct(Long id, HttpServletRequest request) {
        HttpGlobalResponseDTO<Void> response = new HttpGlobalResponseDTO<>();

        Products product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        validarPermisoPropietarioOAdmin(product, request);

        productRepository.delete(product);

        response.setMessage("Producto eliminado exitosamente");
        response.setSuccess(true);
        response.setData(null);

        return response;
    }

    private void validarPermisoPropietarioOAdmin(Products product, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("userId");
        
        Users currentUser = usersRepository.findById(currentUserId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        boolean isAdmin = currentUser.getRolId() == RoleEnum.ADMIN.getId();

        if (!isAdmin && !product.getSellerId().equals(currentUserId)) {
            throw new AccessDeniedException("No tienes permisos para modificar este producto porque no te pertenece");
        }
    }

    private ProductResponseDTO mapToDto(Products product) {

        ProductResponseDTO dto = new ProductResponseDTO();
        Optional<Categories> categoryFound = categoryRepository.findById(product.getIdCategory());

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCategoryName(categoryFound != null ? categoryFound.get().getName() : null);
        dto.setSellerId(product.getSellerId());
        return dto;
    }

    private List<ProductResponseDTO> mapToDtoList(List<Products> products) {
        List<ProductResponseDTO> dtoList = new ArrayList<>();
        for (Products product : products) {
            dtoList.add(mapToDto(product));
        }
        return dtoList;
    }
}