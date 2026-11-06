package com._1.tiendaApp.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data 
public class ErrorResponse {

    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String message;
    private List<FieldErrorDetail> errors;

    @Data 
    public static class FieldErrorDetail {
        private String field;
        private Object rejectedValue;
        private String message;
    }

}
