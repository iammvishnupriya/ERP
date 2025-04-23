package com.erp.UserManagement.Service;


import com.erp.UserManagement.Model.Department;
import com.erp.UserManagement.Model.Role;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserDto userDto);
    boolean emailExists(String email);
    void validateEmail(String email);
    ResponseEntity<SuccessResponse<Department>> addDepartment(Department department);
    ResponseEntity<SuccessResponse<Role>> addRole(RoleDTO roleDTO);
    ResponseEntity<SuccessResponse<List<Department>>> getAllDepartments();
    ResponseEntity<SuccessResponse<List<RoleDTO>>> getRolesByDepartment(Integer departmentId);
    SuccessResponse<Object> assignRoleAndDepartment(AssignRoleDepartmentRequest request);
    SuccessResponse<UserResponseDto> editUser(int userId);
    SuccessResponse<byte[]> generateExcel(List<Integer> userIds);

}




