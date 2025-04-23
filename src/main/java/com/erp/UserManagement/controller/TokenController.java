package com.erp.UserManagement.controller;

import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Repository.UserRepository;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user_management/api/token")
public class TokenController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public TokenController(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    // ✅ 1. Generate Token
    @PostMapping("/create-token")
    public ResponseEntity<?> createToken(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email"));

        String token = jwtUtil.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole() != null ? user.getRole().getName() : null);
        response.put("department", user.getDepartment());

        return ResponseEntity.ok(new SuccessResponse<>(200,"Token created successfully", response));
    }

    // ✅ 2. Validate Token
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        try {
            String email = jwtUtil.extractUsername(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("Invalid token"));

            boolean isValid = jwtUtil.validateToken(token, user);

            return ResponseEntity.ok(new SuccessResponse<>(200,"Token validation result", isValid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>(200,"Invalid token", false));
        }
    }

    // ✅ 3. Refresh Token
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>(200,"Missing token", null));
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid token"));

        String newToken = jwtUtil.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("newToken", newToken);

        return ResponseEntity.ok(new SuccessResponse<>(200,"Token refreshed successfully", response));
    }
}