package com._1.users.service;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com._1.users.dto.HttpGlobalResponse;
import com._1.users.dto.UserDTO;
import com._1.users.entity.Users;
import com._1.users.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AdminService {
    private final JwtService jwtService;
    private final UsersRepository usersRepository;
    
    public HttpGlobalResponse<UserDTO> getUserByUserName(String userName){
        HttpGlobalResponse<UserDTO> response = new HttpGlobalResponse<>();
        UserDTO userDTO = new UserDTO();

        

        if (rolId != 2) {
            response.setMessage("No tiene permisos para poder hacer esta peticion");
            return response;
        }

        Optional<Users> userFound = usersRepository.findByUsername(userName);

        if (userFound.isEmpty()) {
            response.setMessage("usuario no encontrado");
            return response;
        }

        Users user = userFound.get();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setRolId(user.getRoles().getName());

        response.setMessage("Usuarion encontrado");
        response.setData(userDTO);
        return response;

    }
}
