package com._1.tiendaApp.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends  BusinessException{
    public ConflictException (String message){
        super(HttpStatus.CONFLICT, "BUSINESS_CONFLICT", message);
    }
}
