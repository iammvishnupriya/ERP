package com.erp.UserManagement.ServiceImpl;

import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Repository.UserRepository;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Service.AdminService;
import com.erp.UserManagement.dto.UserDto;
import com.erp.UserManagement.Enum.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    public SuccessResponse<List<UserDto>> getAllUsersForAdmin(String search) {
        List<User> users = null;
        if (search != null && !search.isEmpty()) {
            users = userRepository.searchByStatusAndKeyword(UserStatus.ACTIVE, search);
        } else {
            users = userRepository.findByStatus(UserStatus.ACTIVE);
        }

        List<UserDto> userDtoList = users.stream().map(user -> {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            if (user.getRole() != null) {
                dto.setRole(user.getRole().getName());
            } else {
                dto.setRole(null); // or set a default like "No Role"
            }

            // Safely set department name
            if (user.getDepartment() != null) {
                dto.setDepartment(user.getDepartment().getName());
            } else {
                dto.setDepartment(null); // or set a default like "No Department"
            }
            dto.setAddress(user.getAddress());
            return dto;
        }).collect(Collectors.toList());

        return new SuccessResponse<>(200, "Users fetched successfully", userDtoList);
    }


    public SuccessResponse<String> softDeleteUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);

        return new SuccessResponse<>(200, "User deleted successfully", null);
    }

}
