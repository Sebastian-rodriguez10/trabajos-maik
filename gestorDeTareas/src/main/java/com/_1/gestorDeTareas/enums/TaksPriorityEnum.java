package com._1.gestorDeTareas.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaksPriorityEnum {
    BAJA("baja"),
    MEDIA("media"),
    ALTA("alta"),
    URGENTE("urgente");
    
    private final String description;

    TaksPriorityEnum(String desc) {
        this.description = desc;
    }
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static TaksPriorityEnum fromString(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        for (TaksPriorityEnum priority : values()) {
            if (priority.description.equalsIgnoreCase(value.trim())) {
                return priority;
            }
        }

        throw new IllegalArgumentException("Prioridad inválida: " + value + ". Valores permitidos: baja, media, alta, urgente");
    }
}
