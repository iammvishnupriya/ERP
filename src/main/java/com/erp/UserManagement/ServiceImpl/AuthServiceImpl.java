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
    public SuccessResponse<LoginResponse> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return new SuccessResponse<>(404, "User not found", null);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new SuccessResponse<>(401, "Invalid email or password", null);
        }

        String token = jwtUtil.generateToken(user);

        LoginResponse response = new LoginResponse(
                token,
                user.getEmail(),
                user.getRole() != null ? user.getRole().getName() : null,
                user.getDepartment()
        );

        return new SuccessResponse<>(200, "Login successful", response);
    }



    @Override
    public SuccessResponse<Boolean> validateToken(String token) {
        try {
            String email = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            boolean isValid = jwtUtil.validateToken(token, userDetails);
            return new SuccessResponse<>(200,"Token validation result", isValid);
        } catch (Exception e) {
            return new SuccessResponse<>(200,"Invalid token", false);
        }
    }

    @Override
    public SuccessResponse<String> changePassword(ChangePasswordRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return new SuccessResponse<>(404, "User not found", null);
        }

        User user = optionalUser.get();
        if (!PasswordHasher.matches(request.getOldPassword(), user.getPassword())) {
            return new SuccessResponse<>(400, "Old password is incorrect", null);
        }

        user.setPassword(PasswordHasher.encode(request.getNewPassword()));
        userRepository.save(user);

        return new SuccessResponse<>(200, "Password updated successfully", null);
    }

    @Override
    public ResponseEntity<SuccessResponse<String>> resetPasswordRequest(String email, HttpSession session) {
        if (!isValidEmail(email)) {
            return new ResponseEntity<>(
                    new SuccessResponse<>(400, "Invalid mail", null),
                    HttpStatus.BAD_REQUEST
            );
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new SuccessResponse<>(403, "Not a User Mail", null),
                    HttpStatus.FORBIDDEN
            );
        }

        // Store email in session
        session.setAttribute("forgotEmail", email);

        User user = userOpt.get();
        sendResetPasswordEmail(user);

        return new ResponseEntity<>(
                new SuccessResponse<>(200, "Password reset link sent successfully", null),
                HttpStatus.OK
        );
    }



    private void sendResetPasswordEmail(User user) {
        String resetLink = "http://localhost:5182/email-forgot-password";

        String subject = "Password Reset Request";
        String content = "<p>Hello " + user.getName() + ",</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to reset it:</p>"
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
    public SuccessResponse<String> resetPassword(String newPassword, String confirmPassword, HttpSession session) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        if (!isValidPassword(newPassword)) {
            throw new RuntimeException("Password must be at least 6 characters long, "
                    + "contain one special character, one uppercase letter, one lowercase letter, and one digit");
        }

        String email = (String) session.getAttribute("forgotEmail");
        if (email == null) {
            throw new RuntimeException("Email not found in session. Please initiate reset password again.");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("No user found with this email");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Clear session after successful reset
        session.removeAttribute("forgotEmail");

        return new SuccessResponse<>(200, "Password reset successfully", null);
    }
    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{6,}$");
        return pattern.matcher(password).matches();
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

}



