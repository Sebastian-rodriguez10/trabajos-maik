package com._1.tiendaApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._1.tiendaApp.dto.CreateUserRequestDTO;
import com._1.tiendaApp.dto.HttpGlobalResponseDTO;
import com._1.tiendaApp.dto.UpdateRoleRequestDTO;
import com._1.tiendaApp.dto.UpdateStatusRequestDTO;
import com._1.tiendaApp.dto.UserDTO;
import com._1.tiendaApp.enums.RoleEnum;
import com._1.tiendaApp.security.RequiresRole;
import com._1.tiendaApp.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequestMapping("/users")
@RestController 
@RequiredArgsConstructor 
public class UsersController {

    private final UsersService service;

    /**
     * metodo para que el usuario consulte su informacion
     * @param request
     * @return
     */
    @RequiresRole({RoleEnum.ADMIN, RoleEnum.CLIENTE, RoleEnum.VENDEDOR})
    @GetMapping("/me")
    public ResponseEntity<HttpGlobalResponseDTO<UserDTO>> consultMe(HttpServletRequest request) {
        HttpGlobalResponseDTO<UserDTO> response = service.consultMe(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Metodo para ver todos los usuarios
     * @return
     */
    @RequiresRole(RoleEnum.ADMIN) 
    @GetMapping
    public ResponseEntity<HttpGlobalResponseDTO<List<UserDTO>>> getAllUsers() {
        HttpGlobalResponseDTO<List<UserDTO>> response = service.getAllUsers();
        return ResponseEntity.ok(response);
    }

    /**
     * metodo para crear un usuario
     * @param request los datos que se nececitan para crear el usuario
     * @return
     */
    @RequiresRole(RoleEnum.ADMIN)
    @PostMapping
    public ResponseEntity<HttpGlobalResponseDTO<UserDTO>> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = service.createUserByAdmin(request);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * metodo para actualizar el rol de un usuario
     * @param id id del usuario
     * @param request el rol que le valla a poner
     * @return
     */
    @RequiresRole(RoleEnum.ADMIN)
    @PatchMapping("/{id}/role")
    public ResponseEntity<HttpGlobalResponseDTO<UserDTO>> updateUserRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = service.updateUserRole(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * meto para actualizar el estado de active
     * @param id es el id del usuario que va a cambiarle el estado active
     * @param request el nuevo estado de active
     * @return
     */
    @RequiresRole(RoleEnum.ADMIN)
    @PatchMapping("/{id}/status")
    public ResponseEntity<HttpGlobalResponseDTO<UserDTO>> updateUserStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = service.updateUserStatus(id, request);
        return ResponseEntity.ok(response);
    }
    
}
