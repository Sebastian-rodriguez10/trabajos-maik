package com._1.tiendaApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._1.tiendaApp.dto.HttpGlobalResponseDTO;
import com._1.tiendaApp.dto.LoginRequestDTO;
import com._1.tiendaApp.dto.LoginResponseDTO;
import com._1.tiendaApp.dto.RegisterRequestDTO;
import com._1.tiendaApp.dto.RegisterResponseDTO;
import com._1.tiendaApp.exception.InvalidCredentialsException;
import com._1.tiendaApp.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/auth")
@RequiredArgsConstructor 
public class AuthController {

    private final AuthService service;

    /**
     * metodo para registrarse 
     * @param request
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<HttpGlobalResponseDTO<RegisterResponseDTO>> postMethodName(@Valid @RequestBody RegisterRequestDTO request) {

        HttpGlobalResponseDTO<RegisterResponseDTO> response = service.register(request);
        return ResponseEntity.ok(response);

    }

    /**
     * metodo para iniciar sesion
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<HttpGlobalResponseDTO<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request) {

        HttpGlobalResponseDTO<LoginResponseDTO> response = service.login(request);
        return ResponseEntity.ok(response);

    }

    /**
     * metodo para actualizar el token cuando valla a expirar
     * @param request
     * @return
     */
    @PostMapping("/refreshtoken")
    public ResponseEntity<HttpGlobalResponseDTO<LoginResponseDTO>> refreshToken(HttpServletRequest request) {
        HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidCredentialsException("Token is invalid");
        }

        String token = authHeader.replace("Bearer ", "");
        
        try {
            response = service.refreshToken(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("token is invalid or expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    
    
}
