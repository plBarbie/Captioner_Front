package com.example.captioner.network;

import com.example.captioner.model.DialogueDTO;
import com.example.captioner.model.PlayBean;
import com.example.captioner.model.PlayDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BookService {
    @GET("api/user/booking") // 这里填写你的后端 API 地址
    Call<List<PlayBean>> getPlays();

    @POST("api/user/book")
    Call<UserResponse> bookPlay(@Body PlayDTO playDTO);

    @GET("api/user/booked") // 这里填写你的后端 API 地址
    Call<List<PlayBean>> getBookedPlays();

    @POST("api/user/cancel")
    Call<UserResponse> cancelPlay(@Body PlayDTO playDTO);

//    @GET("api/user/getDialogues")
//    Call<List<Dialogue>> getDialogues(@Query("playId") String playTitle, @Query("currentTime") String currentTime);

    @POST("api/user/currentdialogue")
    Call<List<DialogueDTO>> getCurrentDialogue(@Query("playTitle") String playTitle);
}



