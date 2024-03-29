package com.example.captioner;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.captioner.model.PlayBean;
import com.example.captioner.network.BookService;
import com.example.captioner.network.DisplayRequest;
import com.example.captioner.network.RetrofitClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DisplayActivity extends AppCompatActivity {
    private TextView dialogueTextView;

    private FrameLayout displayPanel;
    private boolean isBlackBackground = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        dialogueTextView = findViewById(R.id.dialogueTextView);

        boolean isLandscape = ScreenUtils.isLandscape(DisplayActivity.this);
        final TextView horizontalHintText = findViewById(R.id.horizontalHintText);

        displayPanel = findViewById(R.id.clientDisplayPanel);

        Button lightButton = findViewById(R.id.lightButton);
        Button setupButton = findViewById(R.id.setupButton);

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化当前时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        // 获取传递的 PlayBean 对象
        PlayBean currentPlay = getIntent().getParcelableExtra("currentPlay");

        if (currentPlay != null) {
            // 在这里处理传递过来的 PlayBean 对象
            Toast.makeText(this, "获取了当前播放节目信息", Toast.LENGTH_SHORT).show();
            // 发送当前时间和播放信息
            sendCurrentTimeAndPlayInfo(formattedNow, currentPlay);
        } else {
            Toast.makeText(this, "无法获取当前播放节目信息", Toast.LENGTH_SHORT).show();
        }

        // Click activity of lightButton
        lightButton.setOnClickListener(v -> {
            // TODO: Change text color
            // Determine background color and change
            if (isBlackBackground) {
                displayPanel.setBackgroundColor(Color.WHITE);
            } else {
                displayPanel.setBackgroundColor(Color.BLACK);
            }
            isBlackBackground = !isBlackBackground;
        });

        // TODO: Click activity of setUpButton
        setupButton.setOnClickListener(v -> {

        });

        // If not landscape, show the hint text for 3s
        if (!isLandscape) {
            // Show the horizontal hint text
            horizontalHintText.setVisibility(View.VISIBLE);
            // Hide the horizontal hint text after 3 seconds
            new Handler().postDelayed(() -> horizontalHintText.setVisibility(View.GONE), 3000); // 3000 milliseconds delay
        }

    }



    // Detects the orientation of the current screen
    public static class ScreenUtils {
        public static boolean isLandscape(Context context) {
            int orientation = context.getResources().getConfiguration().orientation;
            return orientation == Configuration.ORIENTATION_LANDSCAPE;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendCurrentTimeAndPlayInfo(String formattedNow, PlayBean currentPlay) {
        String backendUrl = getString(R.string.backend_url);
        Retrofit retrofit = RetrofitClient.getClient(backendUrl);
        BookService bookingService = retrofit.create(BookService.class);

        // 转换当前播放节目的开始时间为字符串
        String startTime = currentPlay.getStartTime();

        Call<String> call = bookingService.getCurrentDialogue(new DisplayRequest(currentPlay, LocalDateTime.now()));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String currentDialogue = response.body();
                    if (currentDialogue != null) {
                        // 在 TextView 中显示当前台词
                        dialogueTextView.setText(currentDialogue);
                    } else {
                        Toast.makeText(DisplayActivity.this, "无法获取当前台词", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DisplayActivity.this, "获取当前台词失败：" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("DisplayActivity", "Failed to get current dialogue", t);
                Toast.makeText(DisplayActivity.this, "Failed to get current dialogue. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
