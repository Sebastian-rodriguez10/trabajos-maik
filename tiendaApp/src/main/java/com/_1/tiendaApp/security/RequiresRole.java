package com._1.tiendaApp.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com._1.tiendaApp.enums.RoleEnum;

//defino en donde quiero utilizar mi anotacion
@Target(ElementType.METHOD)
// RUNTIME: disponible en tiempo de ejecución, en todo momento (esto es necesario para que el interceptor lo lea)
@Retention(RetentionPolicy.RUNTIME)
public @interface  RequiresRole {
    RoleEnum[] value();
}
