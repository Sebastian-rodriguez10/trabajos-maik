package com._1.tiendaApp.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
public enum StatusEnum {
    PENDIENTE("PENDIENTE"),
    ENVIADO("ENVIADO"),
    ENTREGADO("ENTREGADO"),
    CANCELADO("CANCELADO");

    private final String status;

    public String getStatus() {
        return status;
    }
}
