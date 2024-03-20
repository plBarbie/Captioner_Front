package com.example.captioner.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            // 创建 Cookie 管理器
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            // 创建自定义的OkHttpClient实例，添加 Cookie 管理和 Stetho 网络拦截
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS) // 设置连接超时时间
                    .readTimeout(20, TimeUnit.SECONDS) // 设置读取超时时间
                    .writeTimeout(20, TimeUnit.SECONDS) // 设置写入超时时间
                    .cookieJar(new JavaNetCookieJar(cookieManager)) // 添加 CookieJar
                    .addNetworkInterceptor(new StethoInterceptor()) // 添加 Stetho 网络拦截器
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

