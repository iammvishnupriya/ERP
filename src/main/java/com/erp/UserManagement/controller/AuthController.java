package com.erp.UserManagement.controller;

import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Service.AuthService;
import com.erp.UserManagement.dto.ChangePasswordRequest;
import com.erp.UserManagement.dto.LoginRequest;
import com.erp.UserManagement.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public SuccessResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/validate")
    public SuccessResponse<Boolean> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new SuccessResponse<>(200,"Invalid token header", false);
        }
        String token = authHeader.substring(7);
        return authService.validateToken(token);
    }

    @PostMapping("/change-password")
    public SuccessResponse<String> updatePassword(@RequestBody ChangePasswordRequest request) {
        return authService.changePassword(request);
    }
}