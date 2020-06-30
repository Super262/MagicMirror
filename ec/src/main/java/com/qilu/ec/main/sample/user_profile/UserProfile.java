package com.qilu.ec.main.sample.user_profile;

public class UserProfile {
    private int code;
    private UserProfile_Data data;
    private String msg;

    public UserProfile(int code, UserProfile_Data data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserProfile_Data getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
