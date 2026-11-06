package com._1.tiendaApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class RegisterRequestDTO {

    @NotBlank(message = "el nombre es obligatorio")
    @Size(min = 3, max = 30,message = "el nombre debe tener entre 3 y 30 caracteres")
    private String name;

    @Email(message = "email invalido")
    @NotBlank(message = "el email es obligatorio")
    private String email;

    @NotBlank(message = "la contraseña es obligatoria")
    @Size (min = 5, max = 20, message = "la contraseña debe contener entre 5 y 20 caracteres")
    private String password;
}
