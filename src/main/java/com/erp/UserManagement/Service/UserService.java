package com.erp.UserManagement.Service;


import com.erp.UserManagement.Model.Department;
import com.erp.UserManagement.Model.Role;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.dto.*;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserDto userDto);
    boolean emailExists(String email);
    SuccessResponse<Department> addDepartment(Department department);
    SuccessResponse<Role> addRole(RoleDTO roleDTO);
    SuccessResponse<List<Department>> getAllDepartments();
    SuccessResponse<List<RoleDTO>> getRolesByDepartment(Integer departmentId);






}
