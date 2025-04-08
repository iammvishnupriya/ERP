package com.erp.UserManagement.ServiceImpl;


import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Model.UserRole;
import com.erp.UserManagement.Model.UserStatus;
import com.erp.UserManagement.Repository.UserRepository;
import com.erp.UserManagement.Service.UserService;
import com.erp.UserManagement.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


//    @Override
//    public User registerUser(UserDto userDto) {
//        User user = new User();
//        user.setName(userDto.getName());
//        user.setEmail(userDto.getEmail());
//        user.setRole(UserRole.valueOf(userDto.getRole()));
//        user.setStatus(UserStatus.valueOf(userDto.getStatus()));
//        return userRepository.save(user);
//    }

    @Override
    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setStatus(userDto.getStatus());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));  // ✅ Encrypt password

        return userRepository.save(user);
    }




    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    @Override
//    public User updateUser(Long id, UserDto userDto) {
//        User user = getUserById(id);
//        user.setName(userDto.getName());
//        user.setRole(userDto.getRole());
//        user.setStatus(userDto.getStatus());
//        return userRepository.save(user);
//    }

    @Override
    public User updateUser(Long id, UserDto userDto) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        User existingUser = existingUserOptional.get();

        // Updating the fields only if new values are provided
        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getRole() != null) {
            existingUser.setRole(userDto.getRole());
        }
        if (userDto.getStatus() != null) {
            existingUser.setStatus(userDto.getStatus());
        }
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @Override
    public boolean authenticate(String email, String password) {
        // Fetch user by email
        Optional<User> user = userRepository.findByEmail(email);

        // Check if the user doesn't exist
        if (user.isEmpty()) {
            return false; // User doesn't exist
        }

        // Check if the provided password matches the stored password
        return passwordEncoder.matches(password, user.get().getPassword());
    }



    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
