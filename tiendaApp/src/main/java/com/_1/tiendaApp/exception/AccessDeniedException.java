package com._1.tiendaApp.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends BusinessException{
    public AccessDeniedException (String message) {
        super(HttpStatus.FORBIDDEN, "ERROR_FORBIDDEN", message);
    }
}
