package com.erp.UserManagement.ServiceImpl;
 
import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Repository.UserRepository;
import com.erp.UserManagement.Service.AdminService;
import com.erp.UserManagement.Service.UserService;
import com.erp.UserManagement.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class AdminServiceImpl implements AdminService {
 
    @Autowired
    private UserRepository userRepository;
 
    @Override
    public List<UserDto> getAllUsersForAdmin() {
        List<User> users = userRepository.findAll();
 
        return users.stream().map(user -> {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setAddress(user.getAddress());
            return dto;
        }).collect(Collectors.toList());
    }
}
 