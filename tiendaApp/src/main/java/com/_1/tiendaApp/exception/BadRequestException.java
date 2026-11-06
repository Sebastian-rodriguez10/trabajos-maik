package com._1.tiendaApp.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BusinessException{
    public BadRequestException (String message) {
        super(HttpStatus.BAD_REQUEST, "ERROR_BAD_REQUEST", message);
    }

}
