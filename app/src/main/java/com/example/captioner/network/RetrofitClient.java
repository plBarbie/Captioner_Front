package com.example.captioner.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            // 创建自定义的OkHttpClient实例
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS) // 设置连接超时时间
                    .readTimeout(20, TimeUnit.SECONDS) // 设置读取超时时间
                    .writeTimeout(20, TimeUnit.SECONDS) // 设置写入超时时间
                    .build();

            // 使用OkHttpClient实例创建Retrofit实例
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

