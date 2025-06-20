package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Ім'я користувача не може бути порожнім")
    private String username;
    
    @Column(nullable = false)
    @NotBlank(message = "Пароль не може бути порожнім")
    private String password;
    
    @Column(unique = true, nullable = false)
    @Email(message = "Некоректний формат email")
    @NotBlank(message = "Email не може бути порожнім")
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
    public enum Role {
        USER, ADMIN
    }
    
    // Constructors
    public User() {}
    
    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}