package com._1.gestorDeTareas.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaksStateEnum {

    PENDIENTE("pendiente"),
    EN_PROGRESO("en_progreso"),
    COMPLETADA("completada"),
    CANCELADA("cancelada");

    private final String description;

    TaksStateEnum(String desc) {
        this.description = desc;
    }

    @JsonCreator
    public static TaksStateEnum fromString(String value) {

        for (TaksStateEnum state : values()) {

            if (value == null || value.isBlank()) {
                return null;
            }

            if (state.description.equalsIgnoreCase(value)) {
                return state;
            }

        }

        throw new IllegalArgumentException("Prioridad inválida: " + value + ". Valores permitidos: baja, media, alta, urgente");
    }
}
