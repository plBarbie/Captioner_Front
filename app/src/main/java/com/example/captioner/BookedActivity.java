package com.example.captioner;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.captioner.model.PlayBean;
import com.example.captioner.network.BookService;
import com.example.captioner.network.RetrofitClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookedActivity extends AppCompatActivity {
    private static final String TAG = "BookedActivity";
    private List<PlayBean> plays = new ArrayList<>();
    private BookedAdapter bookedAdapter;
    private BookService bookService;

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
        bookedAdapter = new BookedAdapter(plays, getString(R.string.backend_url));
        recyclerView.setAdapter(bookedAdapter);

        bookedAdapter.setOnItemClickListener(new OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                LocalDateTime now;
                LocalDateTime startTime;
                LocalDateTime endTime;
                try {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String nowString = dateTimeFormatter.format(LocalDateTime.now());
                    now = LocalDateTime.parse(nowString,dateTimeFormatter);
                    startTime = LocalDateTime.parse(bookedAdapter.getItem(position).getStartTime(),dateTimeFormatter);
                    endTime = LocalDateTime.parse(bookedAdapter.getItem(position).getEndTime(), dateTimeFormatter);
                    if (startTime.isBefore(now) && endTime.isAfter(now)) {
                        startActivity(new Intent(BookedActivity.this, DisplayActivity.class));
                        PlayBean currentPlay = (PlayBean) adapter.getItem(position);
//                        Call<String> call = bookService.getCurrentDialogue(new DisplayRequest(currentPlay, LocalDateTime.now()));
//
//                        call.enqueue(new Callback<String>() {
//                            @Override
//                            public void onResponse(Call<String> call, Response<String> response) {
//                                if (response.isSuccessful()) {
//                                    String currentDialogue = response.body();
//                                    if (currentDialogue != null) {
//                                        // 在 TextView 中显示当前台词
//                                        dialogueTextView.setText(currentDialogue);
//                                    } else {
//                                        Toast.makeText(DisplayActivity.this, "无法获取当前台词", Toast.LENGTH_SHORT).show();
//                                    }
//                                } else {
//                                    Toast.makeText(DisplayActivity.this, "获取当前台词失败：" + response.code(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<String> call, Throwable t) {
//                                Log.e("DisplayActivity", "Failed to get current dialogue", t);
//                                Toast.makeText(DisplayActivity.this, "Failed to get current dialogue. Please try again.", Toast.LENGTH_LONG).show();
//                            }
//                        });
                        Intent intent = new Intent(BookedActivity.this, DisplayActivity.class);
                        //先写getid，后面怎么定义play再改
                        intent.putExtra("currentPlay", currentPlay.getId());
                        startActivity(intent);
                    } else if (endTime.isBefore(now)) {
                        Toast.makeText(BookedActivity.this, bookedAdapter.getItem(position).getTitle() + "已播放完毕", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BookedActivity.this, bookedAdapter.getItem(position).getTitle() + "还未到播放时间", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(BookedActivity.this, bookedAdapter.getItem(position).getTitle()+"错了", Toast.LENGTH_SHORT).show();

                }
            }
        });

        // 使用 RetrofitClient 获取 Retrofit 实例
        Retrofit retrofit = RetrofitClient.getClient(getString(R.string.backend_url));

        // 使用 Retrofit 实例创建 API 接口实例
        BookService bookService = retrofit.create(BookService.class);

        Call<List<PlayBean>> call = bookService.getBookedPlays();
        call.enqueue(new Callback<List<PlayBean>>() {
            @Override
            public void onResponse(Call<List<PlayBean>> call, Response<List<PlayBean>> response) {
                if (response.isSuccessful()) {
                    List<PlayBean> fetchedPlays = response.body();
                    if (fetchedPlays != null) {
                        plays.clear();
                        plays.addAll(fetchedPlays);
                        bookedAdapter.notifyDataSetChanged();
                    }
                } else {
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<PlayBean>> call, Throwable t) {
                Log.e("BookedActivity", "Failed to fetch plays", t);
                Toast.makeText(BookedActivity.this, "Failed to load plays. Please try again.", Toast.LENGTH_LONG).show();
            }
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
        Toast.makeText(BookedActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
