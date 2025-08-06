package com.example.schoolmanagement.dto;



import java.util.Set;

public class UserResponseDTO {

    private Long userId;
    private String username;
    private Set<String> roles;

    public UserResponseDTO() {}

    public UserResponseDTO(Long userId, String username, Set<String> roles) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}

