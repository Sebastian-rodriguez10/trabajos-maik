package com._1.tiendaApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._1.tiendaApp.dto.HttpGlobalResponseDTO;
import com._1.tiendaApp.dto.OrderRequestDTO;
import com._1.tiendaApp.dto.OrderResponseDTO;
import com._1.tiendaApp.dto.UpdateOrderStatusRequestDTO;
import com._1.tiendaApp.enums.RoleEnum;
import com._1.tiendaApp.security.RequiresRole;
import com._1.tiendaApp.service.OrdersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/orders")
@RestController
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService service;

    @RequiresRole(RoleEnum.CLIENTE)
    @PostMapping
    public ResponseEntity<HttpGlobalResponseDTO<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO requestDTO,HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = service.createOrder(requestDTO, request);
        return ResponseEntity.ok(response);
    }

    @RequiresRole(RoleEnum.CLIENTE)
    @GetMapping("/mine")
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderResponseDTO>>> getMyOrders(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = service.getMyOrders(request);
        return ResponseEntity.ok(response);
    }

    @RequiresRole(RoleEnum.VENDEDOR)
    @GetMapping("/sales")
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderResponseDTO>>> getSales(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = service.getSales(request);
        return ResponseEntity.ok(response);
    }

    @RequiresRole(RoleEnum.ADMIN)
    @GetMapping
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderResponseDTO>>> getAllOrders() {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = service.getAllOrders();
        return ResponseEntity.ok(response);
    }

    @RequiresRole({RoleEnum.VENDEDOR, RoleEnum.ADMIN})
    @PatchMapping("/{id}/status")
    public ResponseEntity<HttpGlobalResponseDTO<OrderResponseDTO>> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequestDTO requestDTO,HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = service.updateOrderStatus(id, requestDTO, request);
        return ResponseEntity.ok(response);
    }

    @RequiresRole(RoleEnum.CLIENTE)
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<HttpGlobalResponseDTO<OrderResponseDTO>> cancelOrder(@PathVariable Long id, HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = service.cancelOrder(id, request);
        return ResponseEntity.ok(response);
    }
}