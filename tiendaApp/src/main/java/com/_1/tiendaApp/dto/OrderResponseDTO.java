package com._1.tiendaApp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String status;
}