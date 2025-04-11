package com.erp.UserManagement.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    // Optional main method for testing
    public static void main(String[] args) {
        String hashedPassword = encode("123"); // Replace with real password
        System.out.println("Hashed Password: " + hashedPassword);
    }
}
