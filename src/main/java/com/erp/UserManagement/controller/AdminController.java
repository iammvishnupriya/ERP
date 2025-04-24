package com.erp.UserManagement.controller;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Service.AdminService;
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
    public SuccessResponse<List<UserDto>> getAllUsers(@RequestParam(required = false) String search) {
        return adminService.getAllUsersForAdmin(search);
    }
    @DeleteMapping("/user/delete")
    public SuccessResponse<String> deleteUser(@RequestParam int userId) {
        return adminService.softDeleteUserById(userId);
    }

}
 