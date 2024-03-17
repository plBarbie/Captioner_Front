package com.example.captioner.network;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
//    @SerializedName("name")
//    private String name;
private boolean success;
private String message;


    @SerializedName("email")
    private String email;


//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
public boolean isSuccess() {
    return success;
}

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}