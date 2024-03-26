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

public class BookedActivity extends AppCompatActivity {


    private List<PlayBean> plays = new ArrayList<>();
    BookedAdapter bookedAdapter = new BookedAdapter(plays);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> {
            startActivity(new Intent(BookedActivity.this, HomeActivity.class));
            finish();
        });

        RecyclerView recyclerView = findViewById(R.id.booked_plays);
        recyclerView.setAdapter(bookedAdapter);

//        bookingAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//                Toast.makeText(BookingActivity.this, bookingAdapter.getItem(position).getTitle()+"", Toast.LENGTH_SHORT).show();
//            }
//        });
        // 使用 RetrofitClient 获取 Retrofit 实例
        Retrofit retrofit = RetrofitClient.getClient("http://172.20.10.5:8080/");

        // 使用 Retrofit 实例创建 API 接口实例
        BookService bookService = retrofit.create(BookService.class);

        Call<List<PlayBean>> call = bookService.getBookedPlays();
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
                        // 创建 BookedAdapter 并将数据传递给适配器
                        bookedAdapter = new BookedAdapter(plays);
                        // 设置 RecyclerView 的适配器
                        recyclerView.setAdapter(bookedAdapter);
                        // 刷新列表
                        bookedAdapter.notifyDataSetChanged();
                    }
                } else {
                    System.out.println("Response not successful: " + response.code());
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<PlayBean>> call, Throwable t) {
                Log.e("BookingActivity", "Failed to fetch plays", t);
                Toast.makeText(BookedActivity.this, "Failed to load plays. Please try again.", Toast.LENGTH_LONG).show();
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
        Toast.makeText(BookedActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
