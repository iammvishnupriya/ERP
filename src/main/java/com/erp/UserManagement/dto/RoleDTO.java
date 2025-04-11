package com.erp.UserManagement.dto;
public class RoleDTO {
    private Integer id;
    private String roleName;
    private Integer dept_id;

    public RoleDTO(){}
    public RoleDTO(Integer id, String roleName, Integer dept_id) {
        this.id = id;
        this.roleName = roleName;
        this.dept_id = dept_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getDept_id() {
        return dept_id;
    }

    public void setDept_id(Integer dept_id) {
        this.dept_id = dept_id;
    }

}


