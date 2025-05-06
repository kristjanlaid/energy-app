package com.enefit.backend.dto;

import lombok.Data;

/**
 * DTO representing login request payload.
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}
