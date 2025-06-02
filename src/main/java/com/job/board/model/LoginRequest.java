package com.job.board.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    
    @NotBlank(message = "Username harus diisi")
    private String username;

    @NotBlank(message = "Password harus diisi")
    private String password;
}
