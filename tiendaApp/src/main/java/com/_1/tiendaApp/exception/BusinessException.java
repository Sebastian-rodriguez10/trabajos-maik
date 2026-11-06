package com._1.tiendaApp.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data 
public class BusinessException extends RuntimeException{

    private final HttpStatus status;
    private final String errorCode;

    public BusinessException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
