package com._1.tiendaApp.security;

import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class RoleInterceptor implements  HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //verificamos que el handler sea un metodo del controller y no otra cosa
        if (!(handler instanceof HandlerMethod method)) {
            return true; // si no es un endpoint no importa, que siga su camino
        }

        RequiresRole annotation = method.getMethodAnnotation(RequiresRole.class);

        if (annotation == null) {
            return true; //tampoco nos importa
        }

        Object rol = request.getAttribute("rolId");

        if (!(rol instanceof Long rolId)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"User is not authenticated\"}");
            return false; //mochamos la peticion no alcanza a llegar al controlador
        }

        Boolean hasRole = Arrays.stream(annotation.value()).anyMatch(role -> role.getId() == rolId);

        if (!hasRole) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"The user does not have permission to perform this action. \"}");
            return false;
        }

        return true;
    }
}
