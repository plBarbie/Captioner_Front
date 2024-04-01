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

import com.blankj.utilcode.util.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.captioner.model.DialogueDTO;
import com.example.captioner.model.PlayBean;
import com.example.captioner.network.BookService;
import com.example.captioner.network.RetrofitClient;

import java.time.Duration;
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
    private final List<PlayBean> plays = new ArrayList<>();
    private BookedAdapter bookedAdapter;
    private BookService bookService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);


        // 使用 RetrofitClient 获取 Retrofit 实例
        Retrofit retrofit = RetrofitClient.getClient(getString(R.string.backend_url));

        // 使用 Retrofit 实例创建 API 接口实例
        this.bookService = retrofit.create(BookService.class);

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
                    now = LocalDateTime.parse(nowString, dateTimeFormatter);
                    startTime = LocalDateTime.parse(bookedAdapter.getItem(position).getStartTime(), dateTimeFormatter);
                    endTime = LocalDateTime.parse(bookedAdapter.getItem(position).getEndTime(), dateTimeFormatter);
                    Duration duration = Duration.between(startTime, now);
                    long secondsDifference = duration.getSeconds();
                    int targetTime = (int) secondsDifference;
                    if (startTime.isBefore(now) && endTime.isAfter(now)) {
                        PlayBean currentPlay = (PlayBean) adapter.getItem(position);
                        Call<List<DialogueDTO>> call = bookService.getCurrentDialogue(currentPlay.getTitle());
                        Log.d(TAG, "PlayTitle: " + currentPlay.getTitle());
//                        Toast.makeText(BookedActivity.this,"正在播放"+currentPlay.getTitle(),Toast.LENGTH_SHORT).show();
                        call.enqueue(new Callback<List<DialogueDTO>>() {
                            @Override
                            public void onResponse(Call<List<DialogueDTO>> call, Response<List<DialogueDTO>> response) {
                                if (response.body() != null) {
                                    // 成功获取对话列表后，在界面上显示对话内容
                                    List<DialogueDTO> dialogueList = response.body();
                                    String s = GsonUtils.toJson(dialogueList);
                                    Log.d(TAG, "onResponse: "+s);
                                    if (!dialogueList.isEmpty()) {
                                        // 创建Intent
//                                        Log.e("DisplayActivity", dialogueList.toString());
//                                        Toast.makeText(BookedActivity.this, "打开了display", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(BookedActivity.this, DisplayActivity.class);
                                        // 将对话列表作为额外数据放入Intent中
                                        intent.putExtra("dialogueList", new ArrayList<>(dialogueList));
//                                        intent.putExtra("playStartTime", bookedAdapter.getItem(position).getStartTime());
                                        intent.putExtra("targetTime", targetTime);
                                        // 启动DisplayActivity并传递Intent
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(BookedActivity.this, "对话列表为空", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(BookedActivity.this, "获取对话列表失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<DialogueDTO>> call, Throwable t) {
                                Log.e("DisplayActivity", "Failed to get current dialogue", t);
                                Toast.makeText(BookedActivity.this, "Failed to get current dialogue. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        });
//                        Intent intent = new Intent(BookedActivity.this, DisplayActivity.class);
//                        //先写getid，后面怎么定义play再改
//                        intent.putExtra("currentPlay", currentPlay.getId());
//                        startActivity(intent);
                    } else if (endTime.isBefore(now)) {
                        Toast.makeText(BookedActivity.this, bookedAdapter.getItem(position).getTitle() + "已播放完毕", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BookedActivity.this, bookedAdapter.getItem(position).getTitle() + "还未到播放时间", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("BookedActivity", "Error in handling item click: " + e.getMessage());
                    Toast.makeText(BookedActivity.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



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
