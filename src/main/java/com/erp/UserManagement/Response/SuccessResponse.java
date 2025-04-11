package com.erp.UserManagement.Response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter

public class SuccessResponse<T> {
    private int statusCode;
    private String statusMessage;
    private T data;
    public SuccessResponse(){}
    public SuccessResponse(int statusCode, String statusMessage, T data) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

