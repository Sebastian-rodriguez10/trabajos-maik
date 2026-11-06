package com._1.tiendaApp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDTO {
    @NotBlank(message = "El estado es obligatorio")
    private String status;
}