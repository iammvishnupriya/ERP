package com.erp.UserManagement.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("123"); // Replace with real password
        System.out.println("Hashed Password: " + hashedPassword);
    }
}