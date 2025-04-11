package com.erp.UserManagement.Service;
 
import com.erp.UserManagement.dto.UserDto;
 
import java.util.List;
 
public interface AdminService {
    List<UserDto> getAllUsersForAdmin();
}