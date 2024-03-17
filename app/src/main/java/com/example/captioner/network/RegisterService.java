package com.example.captioner.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RegisterService {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("api/user/register")
    Call<UserResponse> registerUser(@Body RegisterRequest registerRequest);
}
