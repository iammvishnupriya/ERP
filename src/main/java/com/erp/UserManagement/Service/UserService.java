package com.erp.UserManagement.Service;


import com.erp.UserManagement.Model.Department;
import com.erp.UserManagement.Model.Role;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.dto.AssignRoleDepartmentRequest;
import com.erp.UserManagement.dto.RoleDTO;
import com.erp.UserManagement.dto.UserDto;
import com.erp.UserManagement.dto.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(UserDto userDto);
    UserResponseDto assignRoleAndDepartment(int userId, AssignRoleDepartmentRequest request);
    SuccessResponse<Department> addDepartment(Department department);
    SuccessResponse<Role> addRole(RoleDTO roleDTO);


}
