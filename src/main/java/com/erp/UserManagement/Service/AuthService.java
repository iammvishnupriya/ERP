package com.erp.UserManagement.Service;


import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.dto.ChangePasswordRequest;
import com.erp.UserManagement.dto.LoginRequest;
import com.erp.UserManagement.dto.LoginResponse;

public interface AuthService {
    SuccessResponse<LoginResponse> login(LoginRequest request);
    SuccessResponse<Boolean> validateToken(String token);
    SuccessResponse<String> changePassword(ChangePasswordRequest request);
}