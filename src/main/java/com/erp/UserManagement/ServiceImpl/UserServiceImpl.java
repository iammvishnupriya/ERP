package com.erp.UserManagement.ServiceImpl;

import com.erp.UserManagement.Enum.UserStatus;
import com.erp.UserManagement.Model.Department;
import com.erp.UserManagement.Model.Role;
import com.erp.UserManagement.Model.User;
import com.erp.UserManagement.Repository.DepartmentRepository;
import com.erp.UserManagement.Repository.RoleRepository;
import com.erp.UserManagement.Repository.UserRepository;
import com.erp.UserManagement.Response.SuccessResponse;
import com.erp.UserManagement.Service.UserService;
import com.erp.UserManagement.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public UserResponseDto registerUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setPassword(passwordEncoder.encode("123")); // default password
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);


        UserResponseDto response = new UserResponseDto();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAddress(user.getAddress());
        return response;
    }

    @Override
    public SuccessResponse<Department> addDepartment(Department department) {
        System.out.printf("Department1111111 ");
        Department departments = new Department();
        departments.setName(department.getName());
        departmentRepository.save(departments);
        SuccessResponse response=new SuccessResponse<>();
        response.setData(departments);
        response.setStatusCode(200);
        response.setStatusMessage("Success");
        return response;
    }

    @Override
    public SuccessResponse<Role> addRole(RoleDTO roleDTO) {
        Department department = departmentRepository.findById(roleDTO.getDept_id())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        Role roles = new Role();
        roles.setName(roleDTO.getRoleName());
        roles.setDepartment(department);
        roleRepository.save(roles);
        SuccessResponse<Role> successResponse=new SuccessResponse<>();
        successResponse.setData(roles);
        successResponse.setStatusCode(200);
        successResponse.setStatusMessage("Success");
        return successResponse;
    }

    @Override
    public SuccessResponse<List<Department>> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        SuccessResponse<List<Department>> response = new SuccessResponse<>();
        response.setData(departments);
        response.setStatusCode(200);
        response.setStatusMessage("Success");
        return response;
    }

    @Override
    public SuccessResponse<List<RoleDTO>> getRolesByDepartment(Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        List<Role> roles = roleRepository.findByDepartment(department);

        // Map Role objects to RoleDTO, excluding dept_id in the response
        List<RoleDTO> roleResponseList = roles.stream()
                .map(role -> new RoleDTO(
                        role.getId(),
                        role.getName(),
                        null // Set dept_id as null since we don't want it in the response
                ))
                .collect(Collectors.toList());

        SuccessResponse<List<RoleDTO>> response = new SuccessResponse<>();
        response.setData(roleResponseList);
        response.setStatusCode(200);
        response.setStatusMessage("Success");

        return response;
    }

    @Override
    public SuccessResponse<Object> assignRoleAndDepartment(AssignRoleDepartmentRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()) {
            return new SuccessResponse<>(404, "User not found", null);
        }
 
        Optional<Role> optionalRole = roleRepository.findById(request.getRoleId());
        if (optionalRole.isEmpty()) {
            return new SuccessResponse<>(404, "Role not found", null);
        }
 
        Optional<Department> optionalDept = departmentRepository.findById(request.getDeptId());
        if (optionalDept.isEmpty()) {
            return new SuccessResponse<>(404, "Department not found", null);
        }
 
        User user = optionalUser.get();
        user.setRole(optionalRole.get());
        user.setDepartment(optionalDept.get());
 
        userRepository.save(user);
 
        return new SuccessResponse<>(200, "Role and Department assigned successfully", user);
    }

    @Override
    public SuccessResponse<UserResponseDto> editUser(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return new SuccessResponse<>(404, "User not found", null);
        }

        User user = optionalUser.get();
        UserResponseDto dto = new UserResponseDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());

        if (user.getRole() != null) {
            dto.setRoleName(user.getRole().getName());
        }

        if (user.getDepartment() != null) {
            dto.setDepartmentName(user.getDepartment().getName());
        }



        return new SuccessResponse<>(200, "User details fetched", dto);
    }





}
