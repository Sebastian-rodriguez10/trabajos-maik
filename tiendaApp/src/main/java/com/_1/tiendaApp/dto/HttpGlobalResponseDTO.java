package com._1.tiendaApp.dto;

import lombok.Data;

@Data 
public class HttpGlobalResponseDTO<T>{
    private String message;
    private T data;
    private boolean success;
}
