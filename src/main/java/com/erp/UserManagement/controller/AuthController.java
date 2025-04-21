package com.erp.UserManagement.controller;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Service.AuthService;
import com.erp.UserManagement.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }


    @GetMapping("/validate")
    public ResponseEntity<SuccessResponse<Boolean>> validateToken(@RequestHeader("Authorization") String authHeader) {
        return authService.validateToken(authHeader);
    }


    @PostMapping("/change-password")
    public ResponseEntity<SuccessResponse<String>> updatePassword(@RequestBody ChangePasswordRequest request) {
        return authService.changePassword(request);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<SuccessResponse<String>> requestReset(@RequestParam String email) {
        return authService.resetPasswordRequest(email);
    }


    @PostMapping("/reset")
    public ResponseEntity<SuccessResponse<String>> resetPassword(
            @RequestParam String token,
            @RequestBody PasswordResetRequest passwordResetRequest
    ) {
        return authService.resetPassword(
                token,
                passwordResetRequest.getNewPassword(),
                passwordResetRequest.getConfirmPassword()
        );
    }


}