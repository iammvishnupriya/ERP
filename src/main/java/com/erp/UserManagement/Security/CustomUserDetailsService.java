package com.erp.UserManagement.Security;


import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.get().getEmail())
                .password(user.get().getPassword()) // Ensure password is encoded in DB
                .roles(user.get().getRole().name()) // Convert Enum role to String
                .build();
    }
}
