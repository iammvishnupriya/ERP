package com.erp.UserManagement.ServiceImpl;
import com.erp.UserManagement.Enum.UserStatus;
import com.erp.UserManagement.Model.ResetToken;
import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Repository.ResetTokenRepository;
import com.erp.UserManagement.Repository.UserRepository;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Security.JwtUtil;
import com.erp.UserManagement.Security.PasswordHasher;
import com.erp.UserManagement.Service.AuthService;
import com.erp.UserManagement.dto.ChangePasswordRequest;
import com.erp.UserManagement.dto.LoginRequest;
import com.erp.UserManagement.dto.LoginResponse;
import com.erp.UserManagement.dto.UserDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtils;
    private ResetTokenRepository resetTokenRepository;
    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository, UserDetailsService userDetailsService, JavaMailSender mailSender, PasswordEncoder passwordEncoder, JwtUtil jwtUtils, ResetTokenRepository resetTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.resetTokenRepository = resetTokenRepository;
    }
    private static final java.util.UUID UUID = java.util.UUID.randomUUID();
    @Value("${app.reset-password.url}")
    private String resetPasswordUrl;

    @Override
    public ResponseEntity<SuccessResponse<LoginResponse>> login(LoginRequest request) {
        SuccessResponse<LoginResponse> response = new SuccessResponse<>();

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            response.setStatusCode(404);
            response.setStatusMessage("User not found");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            response.setStatusCode(401);
            response.setStatusMessage("Invalid email or password");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(user);

        LoginResponse loginResponse = new LoginResponse(
                user.getId(),
                user.getName(),
                token,
                user.getEmail(),
                user.getRole() != null ? user.getRole().getName() : null,
                user.getDepartment() != null ? user.getDepartment().getName() :null
        );

        response.setStatusCode(200);
        response.setStatusMessage("Login successful");
        response.setData(loginResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SuccessResponse<Boolean>> validateToken(String authHeader) {
        SuccessResponse<Boolean> response = new SuccessResponse<>();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatusCode(400);
            response.setStatusMessage("Invalid token header");
            response.setData(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            boolean isValid = jwtUtil.validateToken(token, userDetails);

            response.setStatusCode(200);
            response.setStatusMessage("Token validation result");
            response.setData(isValid);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatusCode(403);
            response.setStatusMessage("Invalid token");
            response.setData(false);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
    }


    @Override
    public ResponseEntity<SuccessResponse<String>> changePassword(ChangePasswordRequest request) {
        SuccessResponse<String> response = new SuccessResponse<>();

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            response.setStatusCode(404);
            response.setStatusMessage("User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();
        if (!PasswordHasher.matches(request.getOldPassword(), user.getPassword())) {
            response.setStatusCode(400);
            response.setStatusMessage("Old password is incorrect");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        user.setPassword(PasswordHasher.encode(request.getNewPassword()));
        userRepository.save(user);

        response.setStatusCode(200);
        response.setStatusMessage("Password updated successfully");
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<SuccessResponse<String>> resetPasswordRequest(String email) {
        SuccessResponse<String> response = new SuccessResponse<>();

        if (email == null || email.isEmpty()) {
            response.setStatusCode(400);
            response.setStatusMessage("Email cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            response.setStatusCode(403);
            response.setStatusMessage("Email is not registered");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

        ResetToken resetToken = new ResetToken();
        resetToken.setToken(token);
        resetToken.setExpirationTime(expiryDate);
        resetToken.setCreatedAt(LocalDateTime.now());
        resetToken.setUser(user);

        resetTokenRepository.save(resetToken);


        sendResetPasswordEmail(user, token);

        response.setStatusCode(200);
        response.setStatusMessage("Reset email sent successfully");
        response.setData(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    private void sendResetPasswordEmail(User user, String token) {
        String resetLink = resetPasswordUrl + "?token=" + token;
        String subject = "Password Reset Request";
        String content = "<p>Hello Dear,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below link to reset it:</p>"
                + "<p><a href=\"" + resetLink + "\">Reset Password</a></p>"
                + "<br><p>If you did not request this, please ignore this email.</p>";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("afreenaa685@gmail.com");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


    @Override
    public ResponseEntity<SuccessResponse<String>> resetPassword(String token, String newPassword, String confirmPassword) {
        SuccessResponse<String> response = new SuccessResponse<>();

        if (!newPassword.equals(confirmPassword)) {
            response.setStatusCode(400);
            response.setStatusMessage("Passwords do not match");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!isValidPassword(newPassword)) {
            response.setStatusCode(400);
            response.setStatusMessage("Password must be at least 6 characters long, contain one special character, one uppercase, one lowercase, and one digit");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<ResetToken> tokenOpt = resetTokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            response.setStatusCode(403);
            response.setStatusMessage("Invalid token");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        ResetToken resetToken = tokenOpt.get();
        if (resetToken.isExpired()) {
            response.setStatusCode(403);
            response.setStatusMessage("Token has expired");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetTokenRepository.delete(resetToken);

        response.setStatusCode(200);
        response.setStatusMessage("Password reset successfully");
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{6,}$");
        return pattern.matcher(password).matches();
    }
}