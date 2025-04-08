package com.erp.UserManagement.controller;

import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Repository.UserRepository;
import com.erp.UserManagement.Security.JwtUtil;
import com.erp.UserManagement.dto.LoginRequest;
import com.erp.UserManagement.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())


        );

        System.out.println("Authenticated User: " + authentication.getPrincipal());

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Generate JWT Token
        String token = jwtUtil.generateToken(user);

        // ✅ Return token and role
        return ResponseEntity.ok(new LoginResponse(token, user.getRole().name()));
    }
}
