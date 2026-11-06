package com._1.users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._1.users.dto.HttpGlobalResponse;
import com._1.users.dto.UserDTO;
import com._1.users.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController 
@RequestMapping("/admin")
@RequiredArgsConstructor 
public class adminController {

    private final AdminService adminService;

    @GetMapping("/get-user")
    public ResponseEntity<HttpGlobalResponse<UserDTO>> getUserByUserName(@RequestParam String userName) {
        HttpGlobalResponse<UserDTO> response = new HttpGlobalResponse<>();

        try {
            response = adminService.getUserByUserName(userName);
            return  ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("token is invalid or expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
}
