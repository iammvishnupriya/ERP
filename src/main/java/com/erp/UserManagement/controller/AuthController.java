package com.erp.UserManagement.controller;

import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Service.AuthService;
import com.erp.UserManagement.dto.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
//    @PostMapping("/reset-password-request/{userId}")
//    public ResponseEntity<SuccessResponse<String>> resetPasswordRequest(@PathVariable int userId) {
//        SuccessResponse<String> response = authService.resetPasswordRequest(userId);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/reset-password")
//    public SuccessResponse<String> resetPassword(
//            @RequestParam("token") String token,
//            @RequestBody PasswordResetRequest passwordResetRequest) {
//
//        return authService.resetPassword(token,
//                passwordResetRequest.getNewPassword(),
//                passwordResetRequest.getConfirmPassword());
//    }
@PostMapping("/reset-password-request")
public ResponseEntity<SuccessResponse<String>> resetPasswordRequest(
        @RequestParam("email") String email,
        HttpSession session) {

    SuccessResponse<String> response = authService.resetPasswordRequest(email, session);
    return ResponseEntity.ok(response);
}

    @PostMapping("/reset-password")
    public ResponseEntity<SuccessResponse<String>> resetPassword(
            @RequestBody PasswordResetRequest request,
            HttpSession session) {

        SuccessResponse<String> response = authService.resetPassword(
                request.getNewPassword(),
                request.getConfirmPassword(),
                session
        );
        return ResponseEntity.ok(response);
    }





}