package com.example.captioner.network;
import com.example.captioner.network.LoginRequest;

import com.example.captioner.network.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginService {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("api/user/login")
    Call<UserResponse> loginUser(@Body LoginRequest loginRequest);
}

