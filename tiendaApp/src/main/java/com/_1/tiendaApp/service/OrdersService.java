package com._1.tiendaApp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._1.tiendaApp.dto.HttpGlobalResponseDTO;
import com._1.tiendaApp.dto.OrderRequestDTO;
import com._1.tiendaApp.dto.OrderResponseDTO;
import com._1.tiendaApp.dto.UpdateOrderStatusRequestDTO;
import com._1.tiendaApp.entity.Orders;
import com._1.tiendaApp.entity.Products;
import com._1.tiendaApp.entity.Users;
import com._1.tiendaApp.enums.RoleEnum;
import com._1.tiendaApp.enums.StatusEnum;
import com._1.tiendaApp.exception.AccessDeniedException;
import com._1.tiendaApp.exception.BadRequestException;
import com._1.tiendaApp.exception.ConflictException;
import com._1.tiendaApp.exception.ResourceNotFoundException;
import com._1.tiendaApp.repository.OrdersRepository;
import com._1.tiendaApp.repository.ProductsRepository;
import com._1.tiendaApp.repository.UsersRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository orderRepository;
    private final ProductsRepository productRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public HttpGlobalResponseDTO<OrderResponseDTO> createOrder(OrderRequestDTO requestDTO, HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = new HttpGlobalResponseDTO<>();

        Long clientId = (Long) request.getAttribute("userId");
        Users client = usersRepository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        Products product = productRepository.findById(requestDTO.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (product.getStock() < requestDTO.getQuantity()) {
            throw new BadRequestException("Stock insuficiente para realizar el pedido. Stock actual: " + product.getStock());
        }

        product.setStock(product.getStock() - requestDTO.getQuantity());
        productRepository.save(product);

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(requestDTO.getQuantity()));

        Orders order = new Orders();
        order.setClientId(client.getId());
        order.setProductId(product.getId());
        order.setQuantity(requestDTO.getQuantity());
        order.setTotal(totalPrice);
        order.setStatus(StatusEnum.PENDIENTE);

        orderRepository.save(order);

        response.setMessage("Pedido creado exitosamente");
        response.setSuccess(true);
        response.setData(mapToDto(order));

        return response;
    }

    public HttpGlobalResponseDTO<List<OrderResponseDTO>> getMyOrders(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = new HttpGlobalResponseDTO<>();
        Long clientId = (Long) request.getAttribute("userId");

        List<Orders> orders = orderRepository.findByClientId(clientId);

        response.setMessage("Tus pedidos consultados exitosamente");
        response.setSuccess(true);
        response.setData(mapToDtoList(orders));

        return response;
    }

    public HttpGlobalResponseDTO<List<OrderResponseDTO>> getSales(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = new HttpGlobalResponseDTO<>();
        Long sellerId = (Long) request.getAttribute("userId");

        List<Orders> orders = orderRepository.findByProductSellerId(sellerId);

        response.setMessage("Ventas consultadas exitosamente");
        response.setSuccess(true);
        response.setData(mapToDtoList(orders));

        return response;
    }

    public HttpGlobalResponseDTO<List<OrderResponseDTO>> getAllOrders() {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = new HttpGlobalResponseDTO<>();
        List<Orders> orders = orderRepository.findAll();

        response.setMessage("Todos los pedidos consultados exitosamente");
        response.setSuccess(true);
        response.setData(mapToDtoList(orders));

        return response;
    }

    @Transactional
    public HttpGlobalResponseDTO<OrderResponseDTO> updateOrderStatus(Long id, UpdateOrderStatusRequestDTO requestDTO, HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = new HttpGlobalResponseDTO<>();

        Orders order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        Long currentUserId = (Long) request.getAttribute("userId");
        Users currentUser = usersRepository.findById(currentUserId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // validar que el estado enviado exista en el enum (si no, 400 y no 500)
        StatusEnum newStatus;
        try {
            newStatus = StatusEnum.valueOf(requestDTO.getStatus().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado invalido. Valores permitidos: PENDIENTE, ENVIADO, ENTREGADO, CANCELADO");
        }

        boolean isAdmin = RoleEnum.ADMIN.getId().equals(currentUser.getRolId());
        if (!isAdmin) {
            Products product = productRepository.findById(order.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            if (!product.getSellerId().equals(currentUserId)) {
                throw new AccessDeniedException("No puedes modificar el estado de un pedido que no pertenece a tus productos");
            }
        }

        if (order.getStatus() == StatusEnum.CANCELADO || order.getStatus() == StatusEnum.ENTREGADO) {
            throw new ConflictException("El pedido ya esta " + order.getStatus().name() + " y no puede cambiar de estado");
        }

        if (newStatus == StatusEnum.CANCELADO) {
            restoreStock(order);
        }

        order.setStatus(newStatus);
        Orders updatedOrder = orderRepository.save(order);

        response.setMessage("Estado del pedido actualizado exitosamente");
        response.setSuccess(true);
        response.setData(mapToDto(updatedOrder));

        return response;
    }

    @Transactional
    public HttpGlobalResponseDTO<OrderResponseDTO> cancelOrder(Long id, HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = new HttpGlobalResponseDTO<>();
        Long clientId = (Long) request.getAttribute("userId");

        Orders order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        if (!order.getClientId().equals(clientId)) {
            throw new AccessDeniedException("No puedes cancelar un pedido que no te pertenece");
        }

        if (order.getStatus() != StatusEnum.PENDIENTE) {
            throw new ConflictException("Solo se pueden cancelar pedidos que se encuentren en estado PENDIENTE");
        }

        restoreStock(order);

        order.setStatus(StatusEnum.CANCELADO);
        orderRepository.save(order);

        response.setMessage("Pedido cancelado exitosamente y stock devuelto");
        response.setSuccess(true);
        response.setData(mapToDto(order));

        return response;
    }

    private void restoreStock(Orders order) {
        Products product = productRepository.findByIdForUpdate(order.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        product.setStock(product.getStock() + order.getQuantity());
        productRepository.save(product);
    }

    private OrderResponseDTO mapToDto(Orders order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setClientId(order.getClientId());

        usersRepository.findById(order.getClientId()).ifPresent(client -> {
            dto.setClientName(client.getUserName());
        });

        dto.setProductId(order.getProductId());

        productRepository.findById(order.getProductId()).ifPresent(product -> {
            dto.setProductName(product.getName());
        });

        dto.setQuantity(order.getQuantity());
        dto.setTotalPrice(order.getTotal());
        dto.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        return dto;
    }

    private List<OrderResponseDTO> mapToDtoList(List<Orders> orders) {
        List<OrderResponseDTO> dtoList = new ArrayList<>();
        for (Orders order : orders) {
            dtoList.add(mapToDto(order));
        }
        return dtoList;
    }
}