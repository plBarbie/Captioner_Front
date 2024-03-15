package com.example.captioner.model;

public class LoginRequest {
    private String email;

    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // 其他字段和方法

    // 以下是字段的 getter 和 setter 方法
    public String getUsername() {
        return email;
    }

    public void setUsername(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
