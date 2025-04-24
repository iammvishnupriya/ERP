package com.erp.UserManagement.Repository;

import com.erp.UserManagement.Enum.UserStatus;
import com.erp.UserManagement.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByStatus(UserStatus status);
    
    @Query("SELECT u FROM User u WHERE u.status = :status AND " +
        "(LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(u.address) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<User> searchByStatusAndKeyword(@Param("status") UserStatus status, @Param("search") String search);
}
