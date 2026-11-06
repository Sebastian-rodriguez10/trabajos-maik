package com._1.tiendaApp.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._1.tiendaApp.dto.HttpGlobalResponseDTO;
import com._1.tiendaApp.dto.LoginRequestDTO;
import com._1.tiendaApp.dto.LoginResponseDTO;
import com._1.tiendaApp.dto.RegisterRequestDTO;
import com._1.tiendaApp.dto.RegisterResponseDTO;
import com._1.tiendaApp.dto.UserDTO;
import com._1.tiendaApp.entity.Roles;
import com._1.tiendaApp.entity.Users;
import com._1.tiendaApp.enums.RoleEnum;
import com._1.tiendaApp.exception.ConflictException;
import com._1.tiendaApp.exception.InvalidCredentialsException;
import com._1.tiendaApp.repository.RolesRepository;
import com._1.tiendaApp.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService {

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public HttpGlobalResponseDTO<RegisterResponseDTO> register (RegisterRequestDTO request){
        RegisterResponseDTO dto = new RegisterResponseDTO();
        HttpGlobalResponseDTO<RegisterResponseDTO> response = new HttpGlobalResponseDTO<>();

        Optional<Users> userName = usersRepository.findByUserName(request.getName());
        Optional<Users> userEmail = usersRepository.findByEmail(request.getEmail());

        if (userName.isPresent()) {
            throw new  ConflictException("El nombre ya esta en uso");
        }

        if (userEmail.isPresent()) {
            throw new ConflictException("El email ya esta en uso");
        }

        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRolId(RoleEnum.CLIENTE.getId());
        user.setUserName(request.getName());

        usersRepository.save(user);

        dto.setName(request.getName());
        dto.setEmail(request.getEmail());

        response.setMessage("usuario registrado correctamente");
        response.setSuccess(true);
        response.setData(dto);

        return response;
        
    }

    public HttpGlobalResponseDTO<LoginResponseDTO> login(LoginRequestDTO request) {

        HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();

        Optional<Users> email = usersRepository.findByEmail(request.getUsernameOrEmail());
        Optional<Users> username = usersRepository.findByUserName(request.getUsernameOrEmail());

        if (username.isEmpty() && email.isEmpty()){
            throw new InvalidCredentialsException("usuario/email o contraseña incorrectos");
        }

        Users userFound;

        if (email.isPresent()) {
            userFound = email.get();
        } else {
            userFound = username.get();
        }
        
        if (!passwordEncoder.matches(request.getPassword(), userFound.getPassword())) {
            throw new InvalidCredentialsException("usuario/email o contraseña incorrectos");
        }

        if (!userFound.isActive()) {
            throw new InvalidCredentialsException("El usuario esta inactivo");
        }

        String token = jwtService.generateToken(userFound.getId(), userFound.getUserName(), userFound.getRolId());
        LoginResponseDTO responseToken = new LoginResponseDTO();
        responseToken.setJwt(token);
        response.setMessage("Inicio de sesión exitoso");
        response.setData(responseToken);
        response.setSuccess(true);

        return response;
    }

    public HttpGlobalResponseDTO<LoginResponseDTO> refreshToken(String token) throws Exception{

        HttpGlobalResponseDTO<LoginResponseDTO> response = new HttpGlobalResponseDTO<>();
        LoginResponseDTO refresh = new LoginResponseDTO();

        String jwt = jwtService.refreshToken(token);
        Long userId = jwtService.extractUserId(token);
        Optional<Users> userFound = usersRepository.findById(userId);

        Users user = userFound.get();
        UserDTO userDTO = new UserDTO();

        Optional<Roles> rolFound = rolesRepository.findById(user.getRolId());
        Roles rolEntity = rolFound.get();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRol(rolEntity.getName());

        refresh.setJwt(jwt);
        refresh.setUser(userDTO);

        response.setMessage("Token refrescado correctamente");
        response.setData(refresh);
        return response;
    }

    
}
