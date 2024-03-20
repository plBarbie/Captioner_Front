package com.example.captioner.network;

import com.example.captioner.model.Play;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BookingService {
    @GET("api/user/booking") // 这里填写你的后端 API 地址
    Call<List<Play>> getPlays();
}



