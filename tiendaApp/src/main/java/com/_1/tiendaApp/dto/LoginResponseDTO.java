package com._1.tiendaApp.dto;

import lombok.Data;

@Data 
public class LoginResponseDTO {

    private String jwt;

    private UserDTO user;
    
}
