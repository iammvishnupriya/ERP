package com.erp.UserManagement.controller;

import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Security.CustomUserDetailsService;
import com.erp.UserManagement.Security.JwtUtil;
import com.erp.UserManagement.Service.UserService;
import com.erp.UserManagement.dto.UserDto;
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

    @Autowired
    private final UserService userService;

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public UserController(UserService userService, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }


    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User createdUser = userService.registerUser(userDto);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }



}
