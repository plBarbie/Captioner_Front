package com.example.captioner.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ForgetPasswordService {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("api/user/forget")
    Call<UserResponse> forgetPassword(@Body ForgetPasswordRequest forgetPasswordRequest);
}
