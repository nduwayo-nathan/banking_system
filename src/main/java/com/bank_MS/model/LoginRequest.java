package com.bank_MS.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Account required")
    private String account;
    @NotBlank(message = "Password is required")
    private String password;
}
