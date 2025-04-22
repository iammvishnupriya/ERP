package com.erp.UserManagement.Service;

import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.dto.AuthRefreshTokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface TokenService {
    SuccessResponse<Map<String, String>> refreshToken(AuthRefreshTokenDTO requestDto);
    SuccessResponse<Boolean> validateToken(String token);
    SuccessResponse<Map<String, Object>> createToken(String email);
}