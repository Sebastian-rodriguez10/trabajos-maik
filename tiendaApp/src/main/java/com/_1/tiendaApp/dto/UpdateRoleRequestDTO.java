package com._1.tiendaApp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequestDTO {
    @NotNull(message = "El ID del rol es obligatorio")
    private Long rolId;
}