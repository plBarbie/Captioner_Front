package com.example.captioner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.captioner.model.Play;
import com.example.captioner.network.BookingService;
import com.example.captioner.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingActivity extends AppCompatActivity {

    private ListView playsListView;
    private BookedListAdapter adapter;
    private List<Play> plays = new ArrayList<>();
//    String backendUrl = getString(R.string.backend_url);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> {
            startActivity(new Intent(BookingActivity.this, HomeActivity.class));
            finish();
        });

        playsListView = findViewById(R.id.playsListView);
        adapter = new BookedListAdapter(this, plays);
        playsListView.setAdapter(adapter);

        // 使用 RetrofitClient 获取 Retrofit 实例
        Retrofit retrofit = RetrofitClient.getClient("http://10.29.1.170:8080/");

        // 使用 Retrofit 实例创建 API 接口实例
        BookingService bookingService = retrofit.create(BookingService.class);

        Call<List<Play>> call = bookingService.getPlays();
        call.enqueue(new Callback<List<Play>>() {
            @Override
            public void onResponse(Call<List<Play>> call, Response<List<Play>> response) {
                System.out.println("Response received");
                if (response.isSuccessful()) {
                    List<Play> fetchedPlays = response.body();
                    if (fetchedPlays != null) {
                        plays.clear();
                        plays.addAll(fetchedPlays);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    System.out.println("Response not successful: " + response.code());
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Play>> call, Throwable t) {
                Log.e("BookingActivity", "Failed to fetch plays", t);
                Toast.makeText(BookingActivity.this, "Failed to load plays. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

        playsListView.setOnItemClickListener((parent, view, position, id) -> {
            Play selectedPlay = plays.get(position);
            // 详情页面跳转逻辑
        });
    }

    private void handleErrorResponse(int statusCode) {
        String errorMessage = "Something went wrong: ";
        switch (statusCode) {
            case 404:
                errorMessage += "Requested resource not found.";
                break;
            case 500:
                errorMessage += "Server is busy, please try again later.";
                break;
            default:
                errorMessage += "Error code " + statusCode;
                break;
        }
        Toast.makeText(BookingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
