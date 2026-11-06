package com._1.tiendaApp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequestDTO {
    @NotNull(message = "El estado active es obligatorio")
    private boolean active;
}