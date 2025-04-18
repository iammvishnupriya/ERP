package com.erp.UserManagement.dto;


import com.erp.UserManagement.Model.Department;

public class LoginResponse {
    private String token;
    private String email;
    private String role;
    private String department;

    public LoginResponse(String token, String email, String role,   String department) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.department = department;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}