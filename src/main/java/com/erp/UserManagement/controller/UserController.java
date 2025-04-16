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
        UserResponseDto registered = userService.registerUser(userDto);
        return ResponseEntity.ok(new SuccessResponse<>(200, "Success", registered));
    }

    @PostMapping("/add-department")
    public SuccessResponse<Department> addDepartment(@RequestBody Department department) {
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

    @PostMapping("/assign-role-department")
    public ResponseEntity<SuccessResponse<Object>> assignRoleAndDepartment(@RequestBody AssignRoleDepartmentRequest request) {
        SuccessResponse<Object> response = userService.assignRoleAndDepartment(request);
 
        if (response.getStatusCode() == 404) {
            return ResponseEntity.status(404).body(response);
        }
 
        return ResponseEntity.ok(response);
    }
 

}
