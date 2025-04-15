package com.erp.UserManagement.Repository;


import com.erp.UserManagement.Model.Department;
import com.erp.UserManagement.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
    List<Role> findByDepartment(Department department);

}
