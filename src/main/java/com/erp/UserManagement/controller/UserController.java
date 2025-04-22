package com.erp.UserManagement.controller;

import com.erp.UserManagement.Model.Department;
import com.erp.UserManagement.Model.Role;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Service.UserService;
import com.erp.UserManagement.dto.*;
import org.springframework.http.ResponseEntity;
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
        try {
            userService.validateEmail(userDto.getEmail());
            UserResponseDto registered = userService.registerUser(userDto);
            return ResponseEntity.ok(new SuccessResponse<>(200, "Success", registered));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>(400, ex.getMessage(), null));
        }
    }



    @PostMapping("/add-department")
    public  ResponseEntity<SuccessResponse<Department>> addDepartment(@RequestBody Department department) {
        return userService.addDepartment(department);
    }

    @PostMapping("/add-role")
    public ResponseEntity<SuccessResponse<Role>> addRole(@RequestBody RoleDTO roleDTO) {
        return userService.addRole(roleDTO);
    }
    @GetMapping("/department")
    public ResponseEntity<SuccessResponse<List<Department>>> getAllDepartments() {
        return userService.getAllDepartments();
    }
    @GetMapping("/roles")
    public ResponseEntity<SuccessResponse<List<RoleDTO>>> getRolesByDepartment(@RequestParam Integer departmentId) {
        return userService.getRolesByDepartment(departmentId);
    }

    @PostMapping("/assign-role-department")
    public ResponseEntity<SuccessResponse<Object>> assignRoleAndDepartment(@RequestBody AssignRoleDepartmentRequest request) {
        System.out.println("Enyering the cntrollerssd");
        SuccessResponse<Object> response = userService.assignRoleAndDepartment(request);
 
        if (response.getStatusCode() == 404) {
            return ResponseEntity.status(404).body(response);
        }
 
        return ResponseEntity.ok(response);
    }

    @GetMapping("/edit-user")
    public ResponseEntity<SuccessResponse<UserResponseDto>> editUser(@RequestParam int userId) {
        SuccessResponse<UserResponseDto> response = userService.editUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }






}
