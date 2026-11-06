package com._1.tiendaApp.dto;

import lombok.Data;

@Data
public class UserDTO {
    /**
     * Id del usuario
     */
    private Long id;

    /**
     * Nombre de usuario
     */
    private String username;

    /**
     * Correo del usuario
     */
    private String email;

    /**
     * rol del usuario
     */
    private String rol;

    /**
     * es el estado del usuario si etsa activo o no
     */
    private Boolean active;
}
