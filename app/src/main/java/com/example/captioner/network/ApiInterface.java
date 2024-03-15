package com.example.captioner.network;
import com.example.captioner.model.LoginRequest;
import com.example.captioner.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiInterface {
    @POST("/api/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}
