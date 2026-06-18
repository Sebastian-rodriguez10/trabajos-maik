package com._1.expert.dto;

import lombok.Data;

@Data
public class HttpGlobalResponse<T> {
    private String message;
    private T data;
}
