package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.Data;

@Data
public class UserRegistrationDto {
    private String username;
    private String password;
    private User.Role role = User.Role.USER;
}