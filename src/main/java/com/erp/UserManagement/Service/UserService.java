package com.erp.UserManagement.Service;


import com.erp.UserManagement.Model.Department;
import com.erp.UserManagement.Model.Role;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.dto.*;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserDto userDto);
    boolean emailExists(String email);
    void validateEmail(String email);
    SuccessResponse<Department> addDepartment(Department department);
    SuccessResponse<Role> addRole(RoleDTO roleDTO);
    SuccessResponse<List<Department>> getAllDepartments();
    SuccessResponse<List<RoleDTO>> getRolesByDepartment(Integer departmentId);
    SuccessResponse<Object> assignRoleAndDepartment(AssignRoleDepartmentRequest request);
    SuccessResponse<UserResponseDto> editUser(int userId);
}




