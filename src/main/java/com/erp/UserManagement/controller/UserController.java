package com.erp.UserManagement.controller;

import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Security.CustomUserDetailsService;
import com.erp.UserManagement.Security.JwtUtil;
import com.erp.UserManagement.Service.UserService;
import com.erp.UserManagement.dto.AssignRoleDepartmentRequest;
import com.erp.UserManagement.dto.UserDto;
import com.erp.UserManagement.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<UserResponseDto>> register(@RequestBody UserDto userDto) {
        UserResponseDto registered = userService.registerUser(userDto);
        return ResponseEntity.ok(new SuccessResponse<>(200, "Success", registered));
    }

    @PutMapping("/{userId}/assign-role-department")
    public ResponseEntity<?> assignRoleAndDepartment(
            @PathVariable int userId,
            @RequestBody AssignRoleDepartmentRequest request) {

        UserResponseDto responseDto = userService.assignRoleAndDepartment(userId, request);

        return ResponseEntity.ok(
                new SuccessResponse<>(200, "Role and department assigned successfully", responseDto)
        );
    }



}
