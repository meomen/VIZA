package com.vuducminh.viza.models;

public class UserRequest {
    private User data;
    private String msg;
    private int errorCode;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
