package com._1.tiendaApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 30,message = "el nombre debe tener entre 3 y 30 caracteres")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 5, max = 20, message = "la contraseña debe contener entre 5 y 20 caracteres")
    private String password;

    @NotNull(message = "El ID del rol es obligatorio")
    private Long rolId;
}