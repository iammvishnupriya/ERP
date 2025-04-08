package com.erp.UserManagement.dto;

import com.erp.UserManagement.Model.UserRole;
import com.erp.UserManagement.Model.UserStatus;

public class UserDto {
    private String name;
    private String email;
    private UserRole role;
    private UserStatus status;
    private String password;

    // Constructor
    public UserDto() {
    }

    public UserDto(String name, String email, UserRole role, UserStatus status, String password) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
        this.password = password;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
