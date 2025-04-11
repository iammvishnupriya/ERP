package com.erp.UserManagement.dto;

public class DepartmentDTO {

        private Integer id;
        private String departmentName;


        public DepartmentDTO(){}
        public DepartmentDTO(Integer id, String departmentName) {
            this.id = id;
            this.departmentName = departmentName;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }
    }



