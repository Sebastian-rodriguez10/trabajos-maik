package com._1.users.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import com._1.users.filter.JwtValidationFilter;

@Configuration // fabrica de beans
public class FilterConfig {
    FilterRegistrationBean<JwtValidationFilter> jwtFilter(JwtValidationFilter jwtValidationFilter){
        // crear un contenedor de registro del bean para el filtro
        FilterRegistrationBean<JwtValidationFilter> registrationBean = new FilterRegistrationBean<>();

        // es decirle a sprinboot que este es el filtro con el que quiero que trabaje
        registrationBean.setFilter(jwtValidationFilter);

        // Definir el alcanze de este filtro, quiero que revise todas las peticiones que entren a mi appi 
        registrationBean.addUrlPatterns("/*");

        // Estableciendo la prioridad de ejecucon de los filtros
        // Este filtro se va a ejecutar antes que los otros filtros internos
        registrationBean.setOrder(1);

        // Retornamos el registro configurado para que spring lo guarde en su contexto
        return registrationBean;
    }
}
