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
import com.erp.UserManagement.dto.AssignRoleDepartmentRequest;
import com.erp.UserManagement.dto.RoleDTO;
import com.erp.UserManagement.dto.UserDto;
import com.erp.UserManagement.dto.UserResponseDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserResponseDto assignRoleAndDepartment(int userId, AssignRoleDepartmentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);

        try {
            Department department = departmentRepository.findByName(request.getDepartmentName())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            user.setDepartment(department);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid department: " + request.getDepartmentName());
        }

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
        response.setStatusMessage("Department has been added successfully");
        return response;
    }


    @Override
    public SuccessResponse<Role> addRole(RoleDTO roleDTO) {
        System.out.println("ROLE DTOOOO : " + roleDTO);
        Department department = departmentRepository.findById(roleDTO.getDept_id())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        System.out.println("111111111111deafafe : " + department);
        Role roles = new Role();
        roles.setName(roleDTO.getRoleName());
        roles.setDepartment(department);
        roleRepository.save(roles);
        SuccessResponse successResponse=new SuccessResponse<>();
        successResponse.setData(roles);
        successResponse.setStatusCode(200);
        successResponse.setStatusMessage("Role has been added successfully");
        return successResponse;
    }
}
