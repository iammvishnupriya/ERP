package com.erp.UserManagement.Service;


import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.dto.ChangePasswordRequest;
import com.erp.UserManagement.dto.LoginRequest;
import com.erp.UserManagement.dto.LoginResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public ResponseEntity<SuccessResponse<LoginResponse>> login(LoginRequest request);
    public ResponseEntity<SuccessResponse<Boolean>> validateToken(String authHeader);
    public ResponseEntity<SuccessResponse<String>> changePassword(ChangePasswordRequest request);
    public ResponseEntity<SuccessResponse<String>> resetPassword(String token, String newPassword, String confirmPassword);
    public ResponseEntity<SuccessResponse<String>> resetPasswordRequest(String email);


    }