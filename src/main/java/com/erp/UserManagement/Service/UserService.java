package com.erp.UserManagement.Service;

import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.dto.UserDto;

import java.util.List;

public interface UserService {
    User registerUser(UserDto userDto);
    User getUserById(Long id);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    boolean authenticate(String email, String password);

}
