package com.erp.UserManagement.dto;

public class AuthRefreshTokenDTO {

    private int userId;

    private String token;

    public int getUserId() {

        return userId;

    }

    public void setUserId(int userId) {

        this.userId = userId;

    }

    public String getToken() {

        return token;

    }

    public void setToken(String token) {

        this.token = token;

    }

}

