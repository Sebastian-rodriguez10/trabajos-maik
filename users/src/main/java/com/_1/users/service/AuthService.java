package com._1.users.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._1.users.dto.HttpGlobalResponse;
import com._1.users.dto.LoginRequestDTO;
import com._1.users.dto.LoginResponseDTO;
import com._1.users.dto.MessageResponseDTO;
import com._1.users.dto.RecoverPasswordDTO;
import com._1.users.dto.RegisterRequestDTO;
import com._1.users.dto.UserDTO;
import com._1.users.entity.Roles;
import com._1.users.entity.Users;
import com._1.users.repository.RolesRepository;
import com._1.users.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
@Service 
@RequiredArgsConstructor 
public class AuthService {
    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public HttpGlobalResponse<UserDTO> register(RegisterRequestDTO request) {
        HttpGlobalResponse<UserDTO> response = new  HttpGlobalResponse<>();
        UserDTO userDTO = new UserDTO();

        Optional<Users> usernameFound = usersRepository.findByUsername(request.getUsername());
        Optional<Users> emailFound = usersRepository.findByEmail(request.getEmail());
        Optional<Roles> rol = rolesRepository.findById(request.getRolId());

        if (usernameFound.isPresent()) {
            response.setMessage("El nombre de usuario ya está en uso");
            return response;
        }

        if (emailFound.isPresent()) {
            response.setMessage("El correo de usuario ya está en uso");
            return response;
        }

        if (rol.isEmpty()) {
            response.setMessage("El rol no existe");
            return response;
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(rol.get());

        usersRepository.save(user);

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRolId(rol.get().getName());

        response.setMessage("Usuario registrado correctamente");
        response.setData(userDTO);

        return response;
    }

    public HttpGlobalResponse<LoginResponseDTO> login(LoginRequestDTO request) {
        HttpGlobalResponse<LoginResponseDTO> response = new HttpGlobalResponse<>();
        Optional<Users> email = usersRepository.findByEmail(request.getUser());
        Optional<Users> username = usersRepository.findByUsername(request.getUser());

        if (username.isEmpty() && email.isEmpty()){
            response.setMessage("Usuario y/o contraseña incorrectos");
            return response;
        }

        Users userFound;

        if (email.isPresent()) {
            userFound = email.get();
        } else {
            userFound = username.get();
        }
        
        if (!passwordEncoder.matches(request.getPassword(), userFound.getPassword())) {
            response.setMessage("Usuario y/o contraseña incorrectos");
            return response;
        }

        String jwt = jwtService.generateToken(userFound.getId(), userFound.getUsername(), userFound.getRoles().getId());
        LoginResponseDTO token = new LoginResponseDTO();
        token.setJwt(jwt);
        response.setMessage("Inicio de sesión exitoso");
        response.setData(token);

        return response;
    }


    public HttpGlobalResponse<LoginResponseDTO> refreshToken(String token) throws Exception{

        HttpGlobalResponse<LoginResponseDTO> response = new HttpGlobalResponse<>();

        LoginResponseDTO refresh = new  LoginResponseDTO();

        String jwt = jwtService.refreshToken(token);
        Long userId = jwtService.extractUserId(token);

        Optional<Users> userFound = usersRepository.findById(userId);
        Users user = userFound.get();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRolId(user.getRoles().getName());

        refresh.setJwt(jwt);
        refresh.setUser(userDTO);

        response.setMessage("token refrescado correctamente");
        response.setData(refresh);

        return response;

    }

	public MessageResponseDTO recoverPassword(RecoverPasswordDTO request) {
        MessageResponseDTO response = new MessageResponseDTO();
        Users userFound;

        Optional<Users> email = usersRepository.findByEmail(request.getEmail());
        Optional<Users> username = usersRepository.findByUsername(request.getUserName());

        if (email.isEmpty() && username.isEmpty()) {
            response.setMessage("datos invalidos");
            return response;
        }

        if (!email.get().getId().equals(username.get().getId())) {
            response.setMessage("el usuario y el correo no coinciden");
            return response;
        }

        userFound = email.get();

        if (passwordEncoder.matches(request.getNewPassword(), userFound.getPassword())) {
            
        }
	}


}
