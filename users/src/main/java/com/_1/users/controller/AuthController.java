package com._1.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._1.users.dto.HttpGlobalResponse;
import com._1.users.dto.LoginRequestDTO;
import com._1.users.dto.LoginResponseDTO;
import com._1.users.dto.MessageResponseDTO;
import com._1.users.dto.RecoverPasswordDTO;
import com._1.users.dto.RegisterRequestDTO;
import com._1.users.dto.UserDTO;
import com._1.users.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/auth/")
@RequiredArgsConstructor 
public class AuthController {

    /**
     * Servicio de auth
     */
    private final AuthService authService;

    /**
     * Método para registrar un usuario
     * 
     * @param request
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<HttpGlobalResponse<UserDTO>> register(@RequestBody  RegisterRequestDTO request) {
        try {
            HttpGlobalResponse<UserDTO> response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("login")
    public ResponseEntity<HttpGlobalResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        try {
            HttpGlobalResponse<LoginResponseDTO> response = authService.login(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("refreshToken")
    public ResponseEntity<HttpGlobalResponse<LoginResponseDTO>> postMethodName(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        HttpGlobalResponse<LoginResponseDTO> response = new HttpGlobalResponse<>();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setMessage("token is invalid");
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        String token = authHeader.replace("Bearer ", "");

        try {
            response = authService.refreshToken(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("token is invalid or expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping()
    public ResponseEntity<MessageResponseDTO> recoverPassword(@RequestBody RecoverPasswordDTO request){
        MessageResponseDTO response = new MessageResponseDTO();
        
        try {
            response = authService.recoverPassword();
            return ResponseEntity.status(HttpStatus.OK).body(response);
           
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("ha ocurrido un error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //girasoles margaritas rosas tulipanes herves chocolate negro gomitas 
    
}
