package com._1.gestorDeTareas.dto;

import lombok.Data;

@Data
public class HttpGlobalResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
