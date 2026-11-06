package com._1.tiendaApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._1.tiendaApp.dto.CreateUserRequestDTO;
import com._1.tiendaApp.dto.HttpGlobalResponseDTO;
import com._1.tiendaApp.dto.UpdateRoleRequestDTO;
import com._1.tiendaApp.dto.UpdateStatusRequestDTO;
import com._1.tiendaApp.dto.UserDTO;
import com._1.tiendaApp.entity.Roles;
import com._1.tiendaApp.entity.Users;
import com._1.tiendaApp.exception.ConflictException;
import com._1.tiendaApp.exception.ResourceNotFoundException;
import com._1.tiendaApp.repository.RolesRepository;
import com._1.tiendaApp.repository.UsersRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * para que el mismo usuario consulte sus datos
     * @param request 
     * @return
     */
    public HttpGlobalResponseDTO<UserDTO> consultMe(HttpServletRequest request) {

        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Long userId = (Long) request.getAttribute("userId");

        Users userFound = usersRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("usuario no encontrado"));

        Roles rolFound = rolesRepository.findById(userFound.getRolId()).orElseThrow(() -> new ResourceNotFoundException("rol no encontrado"));

        userDTO.setEmail(userFound.getEmail());
        userDTO.setId(userFound.getId());
        userDTO.setUsername(userFound.getUserName());
        userDTO.setRol(rolFound.getName());

        response.setData(userDTO);
        response.setMessage("Perfil:" + userDTO.getUsername());
        response.setSuccess(true);
        return response;

    }

    /**
     * consultar la lista de todos los usuarios
     * @return lista de usuarios
     */
    public HttpGlobalResponseDTO<List<UserDTO>> getAllUsers() {

        HttpGlobalResponseDTO<List<UserDTO>> response = new HttpGlobalResponseDTO<>();

        List<Users> usersList = usersRepository.findAll();

        List<UserDTO> dtoList = new ArrayList<>();

        for (Users user : usersList) {

            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUserName());
            dto.setEmail(user.getEmail());
            dto.setActive(user.isActive());

            Roles rolFound = rolesRepository.findById(user.getRolId()).orElseThrow(() -> new ResourceNotFoundException("rol no encontrado"));
            dto.setRol(rolFound.getName());

            dtoList.add(dto);
        }

        response.setMessage("Usuarios encontrados exitosamente");
        response.setSuccess(true);
        response.setData(dtoList);

        return response;
    }

    /**
     * para crear un usuario con cualquier rol
     * @param request
     * @return
     */
    public HttpGlobalResponseDTO<UserDTO> createUserByAdmin(CreateUserRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        if (usersRepository.findByUserName(request.getName()).isPresent()) {
            throw new ConflictException("El nombre ya está en uso");
        }

        if (usersRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("El email ya está en uso");
        }

        Roles rolFound = rolesRepository.findById(request.getRolId()).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        Users user = new Users();
        user.setUserName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRolId(rolFound.getId());
        user.setActive(true); 

        Users savedUser = usersRepository.save(user);

        userDTO.setId(savedUser.getId());
        userDTO.setUsername(savedUser.getUserName());
        userDTO.setEmail(savedUser.getEmail());
        userDTO.setRol(rolFound.getName());

        response.setMessage("Usuario creado exitosamente con el rol: " + rolFound.getName());
        response.setSuccess(true);
        response.setData(userDTO);

        return response;
    }

    /**
     * para actualizar el rol de un usuario
     * @param id el id del usuario
     * @param request el id del rol
     * @return
     */
    public HttpGlobalResponseDTO<UserDTO> updateUserRole(Long id, UpdateRoleRequestDTO request) {
        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Users user = usersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Roles newRol = rolesRepository.findById(request.getRolId()).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        user.setRolId(newRol.getId());
        usersRepository.save(user);

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.isActive());
        userDTO.setRol(newRol.getName());

        response.setMessage("Rol del usuario actualizado exitosamente");
        response.setSuccess(true);
        response.setData(userDTO);

        return response;
    }

    /**
     * metodo papara acutalizar el estado de active
     * @param id id del usuario
     * @param request boleano del estado active
     * @return
     */
    public HttpGlobalResponseDTO<UserDTO> updateUserStatus(Long id, UpdateStatusRequestDTO request) {

        HttpGlobalResponseDTO<UserDTO> response = new HttpGlobalResponseDTO<>();
        UserDTO userDTO = new UserDTO();

        Users user = usersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setActive(request.isActive());
        usersRepository.save(user);

        Roles rol = rolesRepository.findById(user.getRolId()).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.isActive());
        userDTO.setRol(rol.getName());

        response.setMessage("Estado del usuario actualizado exitosamente");
        response.setSuccess(true);
        response.setData(userDTO);

        return response;
    }

}
