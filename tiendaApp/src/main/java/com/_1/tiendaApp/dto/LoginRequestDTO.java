package com._1.tiendaApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class LoginRequestDTO {

    @NotBlank (message = "El campo no puede estar vacio")
    private String usernameOrEmail;

    @NotBlank(message = "la contraseña esta vacia")
    @Size (min = 5, message = "contraseña invalida")
    private String password;
}
