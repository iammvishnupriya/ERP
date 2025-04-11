package com.erp.UserManagement.controller;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Service.AdminService;
import com.erp.UserManagement.Service.UserService;
import com.erp.UserManagement.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @GetMapping("/users")
    public SuccessResponse<List<UserDto>> getAllUsers() {
        List<UserDto> userList = adminService.getAllUsersForAdmin();
        return new SuccessResponse<>(200, "Users fetched successfully", userList);
    }
 
}
 