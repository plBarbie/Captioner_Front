package com.example.captioner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.captioner.model.PlayBean;
import com.example.captioner.network.BookService;
import com.example.captioner.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingActivity extends AppCompatActivity {

    private List<PlayBean> plays = new ArrayList<>();
    private BookingAdapter bookingAdapter;

    private String backendUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        backendUrl = getString(R.string.backend_url);

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> {
            startActivity(new Intent(BookingActivity.this, HomeActivity.class));
            finish();
        });

        RecyclerView recyclerView = findViewById(R.id.booking_plays);
        bookingAdapter = new BookingAdapter(plays, backendUrl);
        recyclerView.setAdapter(bookingAdapter);

//        bookingAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
////                Toast.makeText(BookingActivity.this, bookingAdapter.getItem(position).getTitle()+"", Toast.LENGTH_SHORT).show();
//            }
//        });

        // 使用 RetrofitClient 获取 Retrofit 实例
        Retrofit retrofit = RetrofitClient.getClient(backendUrl);

        // 使用 Retrofit 实例创建 API 接口实例
        BookService bookingService = retrofit.create(BookService.class);

        Call<List<PlayBean>> call = bookingService.getPlays();
        call.enqueue(new Callback<List<PlayBean>>() {
            @Override
            public void onResponse(Call<List<PlayBean>> call, Response<List<PlayBean>> response) {
                System.out.println("Response received");

                if (response.isSuccessful()) {
                    List<PlayBean> fetchedPlays = response.body();
                    if (fetchedPlays != null) {
                        plays.clear();
                        plays.addAll(fetchedPlays);
//                        Toast.makeText(BookingActivity.this, "数据" + plays.size(), Toast.LENGTH_SHORT).show();
//                        adapter.notifyDataSetChanged();
                        bookingAdapter.notifyDataSetChanged();
                    }
                } else {
                    System.out.println("Response not successful: " + response.code());
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<PlayBean>> call, Throwable t) {
                Log.e("BookingActivity", "Failed to fetch plays", t);
                Toast.makeText(BookingActivity.this, "Failed to load plays. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

//        playsListView.setOnItemClickListener((parent, view, position, id) -> {
//            Play selectedPlay = plays.get(position);
//            // 详情页面跳转逻辑
//        });
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
