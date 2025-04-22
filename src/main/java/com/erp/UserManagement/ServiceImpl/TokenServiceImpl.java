package com.erp.UserManagement.ServiceImpl;

import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Repository.UserRepository;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Security.JwtUtil;
import com.erp.UserManagement.Service.TokenService;
import com.erp.UserManagement.dto.AuthRefreshTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public TokenServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public SuccessResponse<Map<String, Object>> createToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email"));

        String token = jwtUtil.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole() != null ? user.getRole().getName() : null);
        response.put("department", user.getDepartment());

        return new SuccessResponse<>(200, "Token created successfully", response);
    }


    @Override
    public SuccessResponse<Boolean> validateToken(String token) {
        try {
            String email = jwtUtil.extractUsername(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("Invalid token"));

            boolean isValid = jwtUtil.validateToken(token, user);
            return new SuccessResponse<>(200, "Token validation result", isValid);
        } catch (Exception e) {
            return new SuccessResponse<>(200, "Invalid token", false);
        }
    }


    @Override
    public SuccessResponse<Map<String, String>> refreshToken(AuthRefreshTokenDTO requestDto) {
        Optional<User> optionalUser = userRepository.findById(requestDto.getUserId());

        if (optionalUser.isEmpty()) {
            return new SuccessResponse<>(404, "User not found", null);
        }

        User user = optionalUser.get();
        String emailFromToken = jwtUtil.extractUsername(requestDto.getToken());

        if (!emailFromToken.equals(user.getEmail()) || !jwtUtil.validateToken(requestDto.getToken(), user)) {
            throw new BadCredentialsException("Invalid or expired token");
        }

        String newToken = jwtUtil.generateToken(user);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("newToken", newToken);

        return new SuccessResponse<>(200, "Token refreshed successfully", responseData);
    }
}