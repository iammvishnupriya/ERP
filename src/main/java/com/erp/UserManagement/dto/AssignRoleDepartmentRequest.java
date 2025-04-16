package com.erp.UserManagement.dto;

import lombok.Data;

@Data
public class AssignRoleDepartmentRequest {
    private int userId;
    private int roleId;
    private int deptId;
    
}
