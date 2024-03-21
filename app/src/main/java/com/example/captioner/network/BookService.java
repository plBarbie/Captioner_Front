package com.example.captioner.network;

import com.example.captioner.model.PlayBean;
import com.example.captioner.model.PlayDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BookService {
    @GET("api/user/booking") // 这里填写你的后端 API 地址
    Call<List<PlayBean>> getPlays();

    @POST("api/user/book")
//    Call<Void> bookPlay(@Query("email") String email, @Query("title") String title);
    Call<UserResponse> bookPlay(@Body PlayDTO playDTO);
}


