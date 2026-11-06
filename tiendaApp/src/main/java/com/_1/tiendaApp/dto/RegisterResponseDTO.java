package com._1.tiendaApp.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data 
public class RegisterResponseDTO {
    private String name;
    private String email;
    private LocalDateTime localDateTime = LocalDateTime.now();
}
