package com._1.tiendaApp.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BusinessException{
    public InvalidCredentialsException (String message){
        super(HttpStatus.UNAUTHORIZED, "ERROR-UNAUTHORIZED", message);
    }
}
