package com._1.users.dto;

import lombok.Data;

@Data
public class HttpGlobalResponse<T> {
    private String message;
    private T data;
}
