package com._1.tiendaApp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com._1.tiendaApp.filter.JwtValidatorFilter;

@Configuration 
public class FilterConfig {
    @Bean 
    FilterRegistrationBean<JwtValidatorFilter> jwtFilter(JwtValidatorFilter jwtValidatorFilter) {
        //crear un contenedor de registro del bean para el filtro
        FilterRegistrationBean<JwtValidatorFilter> registrationBean = new FilterRegistrationBean<>();

        //es decirle a spring que con este filtro quiero que trabaje
        registrationBean.setFilter(jwtValidatorFilter);

        //definir el alcanze del filtro, quiero que reviso todas las peticiones de la api
        registrationBean.addUrlPatterns("/*");

        //Establecemos la prioridad de ejecucion de los filtros
        //Este filtro se va a ejecutar antes que los otros filtros internos
        registrationBean.setOrder(1);

        //Retornamos el registro configurado para que spring lo guarde en su contexto
        return  registrationBean;
    }
}
