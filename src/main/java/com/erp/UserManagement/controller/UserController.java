package com.erp.UserManagement.controller;

import com.erp.UserManagement.Model.Department;
import com.erp.UserManagement.Model.Role;
import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Security.CustomUserDetailsService;
import com.erp.UserManagement.Security.JwtUtil;
import com.erp.UserManagement.Service.UserService;
import com.erp.UserManagement.dto.*;
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

        String email = userDto.getEmail();


        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>(400, "Email cannot be null or blank", null));
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>(400, "Invalid email format", null));
        }

        if (userService.emailExists(email)) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>(400, "Email already registered", null));
        }

        UserResponseDto registered = userService.registerUser(userDto);
        return ResponseEntity.ok(new SuccessResponse<>(200, "Success", registered));
    }



    @PostMapping("/add-department")
    public SuccessResponse<Department> addDepartment(@RequestBody Department department) {
        System.out.println("Entering the department");
        return userService.addDepartment(department);
    }

    @PostMapping("/add-role")
    public SuccessResponse<Role> addRole(@RequestBody RoleDTO roleDTO) {
        return userService.addRole(roleDTO);
    }
    @GetMapping("/department")
    public SuccessResponse<List<Department>> getAllDepartments() {
        return userService.getAllDepartments();
    }
    @GetMapping("/roles")
    public SuccessResponse<List<RoleDTO>> getRolesByDepartment(@RequestParam Integer departmentId) {
        return userService.getRolesByDepartment(departmentId);
    }



}
