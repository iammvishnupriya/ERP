package com.erp.UserManagement.controller;

import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Repository.UserRepository;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Security.JwtUtil;
import com.erp.UserManagement.Service.TokenService;
import com.erp.UserManagement.dto.AuthRefreshTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/token")
public class TokenController {


    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public TokenController(JwtUtil jwtUtil, UserRepository userRepository, TokenService tokenService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/create-token")
    public ResponseEntity<?> createToken(@RequestParam String email) {
        SuccessResponse<Map<String, Object>> response = tokenService.createToken(email);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        SuccessResponse<Boolean> response = tokenService.validateToken(token);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<SuccessResponse<Map<String, String>>> refresh(@RequestBody AuthRefreshTokenDTO dto) {
        return ResponseEntity.ok(tokenService.refreshToken(dto));
    }




}