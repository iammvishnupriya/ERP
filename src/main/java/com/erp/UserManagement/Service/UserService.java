package com.erp.UserManagement.Service;


import com.erp.UserManagement.dto.AssignRoleDepartmentRequest;
import com.erp.UserManagement.dto.UserDto;
import com.erp.UserManagement.dto.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(UserDto userDto);
    UserResponseDto assignRoleAndDepartment(int userId, AssignRoleDepartmentRequest request);
}
