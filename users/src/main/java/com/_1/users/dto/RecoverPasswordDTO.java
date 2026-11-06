package com._1.users.dto;

import lombok.Data;

@Data 
public class RecoverPasswordDTO {

    private String email;
    private String userName;
    private String newPassword;
    private String confirmNewPassword;

}


