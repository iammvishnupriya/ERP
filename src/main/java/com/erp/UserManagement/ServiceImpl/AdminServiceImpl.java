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

    public SuccessResponse<List<UserDto>> getAllUsersForAdmin() {
        List<User> users = userRepository.findByStatus(UserStatus.ACTIVE);

        List<UserDto> userDtoList = users.stream().map(user -> {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setAddress(user.getAddress());
            if (user.getRole() != null) {
                dto.setRole(user.getRole().getName());
            }

            if (user.getDepartment() != null) {
                dto.setDepartment(user.getDepartment().getName());
            }

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
