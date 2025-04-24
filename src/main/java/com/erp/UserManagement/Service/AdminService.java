package com.erp.UserManagement.Service;
 
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.dto.UserDto;
 
import java.util.List;
 
public interface AdminService {
    public SuccessResponse<List<UserDto>> getAllUsersForAdmin(String search);
    public SuccessResponse<String> softDeleteUserById(int userId);
}