package com._1.equipoFutbol5.dto;

import lombok.Data;

@Data
public class HttpGlobalResponse<T> {
    private String message;
    private T data;
}
