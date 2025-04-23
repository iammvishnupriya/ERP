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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (emailExists(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
    }



    @Override
    public ResponseEntity<SuccessResponse<Department>> addDepartment(Department department) {
        SuccessResponse<Department> response = new SuccessResponse<>();
        try {
            Department departments = new Department();
            departments.setName(department.getName());
            departmentRepository.save(departments);

            response.setData(departments);
            response.setStatusCode(200);
            response.setStatusMessage("Success");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setStatusMessage("Failed to add department: " + e.getMessage());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SuccessResponse<Role>> addRole(RoleDTO roleDTO) {
        SuccessResponse<Role> response = new SuccessResponse<>();
        try {
            Department department = departmentRepository.findById(roleDTO.getDept_id())
                    .orElse(null);

            if (department == null) {
                response.setStatusCode(404);
                response.setStatusMessage("Department not found");
                response.setData(null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Role roles = new Role();
            roles.setName(roleDTO.getRoleName());
            roles.setDepartment(department);
            roleRepository.save(roles);

            response.setData(roles);
            response.setStatusCode(200);
            response.setStatusMessage("Success");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setStatusMessage("Failed to add role: " + e.getMessage());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SuccessResponse<List<Department>>> getAllDepartments() {
        SuccessResponse<List<Department>> response = new SuccessResponse<>();
        try {
            List<Department> departments = departmentRepository.findAll();
            response.setData(departments);
            response.setStatusCode(200);
            response.setStatusMessage("Success");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setStatusMessage("Failed to fetch departments: " + e.getMessage());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SuccessResponse<List<RoleDTO>>> getRolesByDepartment(Integer departmentId) {
        SuccessResponse<List<RoleDTO>> response = new SuccessResponse<>();
        try {
            Department department = departmentRepository.findById(departmentId)
                    .orElse(null);

            if (department == null) {
                response.setStatusCode(404);
                response.setStatusMessage("Department not found");
                response.setData(null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            List<Role> roles = roleRepository.findByDepartment(department);
            List<RoleDTO> roleResponseList = roles.stream()
                    .map(role -> new RoleDTO(role.getId(), role.getName(), null))
                    .collect(Collectors.toList());

            response.setData(roleResponseList);
            response.setStatusCode(200);
            response.setStatusMessage("Success");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setStatusMessage("Failed to fetch roles: " + e.getMessage());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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


    @Override
    public SuccessResponse<byte[]> generateExcel(List<Integer> userIds) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<User> users = userRepository.findAllById(userIds);
            Sheet sheet = workbook.createSheet("Users");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Name");
            headerRow.createCell(1).setCellValue("Email");
            headerRow.createCell(2).setCellValue("Phone");
            headerRow.createCell(3).setCellValue("Address");
            headerRow.createCell(4).setCellValue("Role");
            headerRow.createCell(5).setCellValue("Department");

            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getName());
                row.createCell(1).setCellValue(user.getEmail());
                row.createCell(2).setCellValue(user.getPhone());
                row.createCell(3).setCellValue(user.getAddress());
                row.createCell(4).setCellValue(user.getRole().getName());
                row.createCell(5).setCellValue(user.getDepartment().getName());
            }

            workbook.write(outputStream);
            return new SuccessResponse<>(200, "Excel file generated successfully", outputStream.toByteArray());
        } catch (IOException e) {
            return new SuccessResponse<>(500, "Failed to generate Excel file: " + e.getMessage(), null);
        }
    }




}
