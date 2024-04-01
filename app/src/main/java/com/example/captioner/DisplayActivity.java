package com.example.captioner;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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

import com.example.captioner.model.DialogueDTO;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {
    Runnable runnable;
    private TextView dialogueTextView;
    private FrameLayout displayPanel;
    private boolean isBlackBackground = false;
    private final Handler handler = new Handler();

    // 将时间字符串转换为秒数
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static int convertToSeconds(String timeStr) {
        LocalTime time = LocalTime.parse(timeStr);
        return time.toSecondOfDay();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long calculateTimeDifferenceInSeconds(String timeStr1, String timeStr2) {
        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // 解析时间字符串为LocalTime对象
        LocalTime time1 = LocalTime.parse(timeStr1, formatter);
        LocalTime time2 = LocalTime.parse(timeStr2, formatter);

        // 计算时间差
        return time1.until(time2, java.time.temporal.ChronoUnit.SECONDS);
    }

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

        // 获取传递的 PlayBean 对象
        List<DialogueDTO> dialogueList = (List<DialogueDTO>) getIntent().getSerializableExtra("dialogueList");

//        String playStartTimeString = (String) getIntent().getSerializableExtra("playStartTime");
//        String nowString = (String) getIntent().getSerializableExtra("now");


        // 解析播放开始时间字符串为 LocalDateTime 对象
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime playStartTime = LocalDateTime.parse(playStartTimeString, formatter);
//        LocalDateTime now = LocalDateTime.parse(nowString, formatter);
//        // 计算时间差
//        long secondsDifference = Duration.between(playStartTime, now).getSeconds();


        if (dialogueList != null) {
            // 在这里处理传递过来的 PlayBean 对象
//            Log.e(TAG, dialogueList.toString());
//            Log.e(TAG, "获取了当前播放节目信息");

            int targetTime = (int) getIntent().getSerializableExtra("targetTime");
//            Log.e(TAG, "Seconds difference: " + targetTime);

            int position = 0;
            //如果当前话剧已经播放 得到当前进入得时间与开始时间得差值
//            int targetTime = 47;//这里138秒 就是差值  然后最近就2分19秒
//            int targetTime = (int) secondsDifference;

            // 初始化最小差值为一个足够大的值
            int minDifference = Integer.MAX_VALUE;
            String nearestTimeNode = null;
            //找到最接近得时间节点
            for (int i = 0; i < dialogueList.size(); i++) {
                int seconds = convertToSeconds(dialogueList.get(i).getDialogueStartTime());
                int difference = Math.abs(targetTime - seconds);
                if (difference < minDifference) {
                    minDifference = difference;
                    nearestTimeNode = dialogueList.get(i).getDialogueStartTime();
                    position = i;
                }
            }
//            runTextView(dialogueList, position);
            //输出最接近的时间节点
//            Log.d(TAG, "onCreate: " + "与时间 " + targetTime + " 秒最接近的时间节点是：" + nearestTimeNode);


            //TODO 如果当前进入时间时间点刚好播放
            //开始时间第一个节点
            LocalTime locFirstTime = LocalTime.parse(dialogueList.get(0).getDialogueStartTime());
            long millisecondFirstTime = locFirstTime.toSecondOfDay()* 1000;
            long timeDelay = (int)(millisecondFirstTime - (targetTime * 1000));
            if(timeDelay > 0){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runTextView(dialogueList,0);
                    }
                },timeDelay);
            }else {
                runTextView(dialogueList,position);
            }
        } else {
            Toast.makeText(this, "无法获取当前播放节目信息", Toast.LENGTH_SHORT).show();
        }

        // Click activity of lightButton
        lightButton.setOnClickListener(v -> {
            // Determine background color and change
            if (isBlackBackground) {
                displayPanel.setBackgroundColor(Color.WHITE);
                dialogueTextView.setTextColor(Color.BLACK);
            } else {
                displayPanel.setBackgroundColor(Color.BLACK);
                dialogueTextView.setTextColor(Color.WHITE);
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

    /**
     * 显示字幕
     *
     * @param textBeanList
     * @param position     默认为0  只有当前话剧已经开始 然后才进来 找到最近得节点 传入对应节点得position 进行播放
     */
    private void runTextView(List<DialogueDTO> textBeanList, int position) {

        runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = position; i < textBeanList.size(); i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        long l = calculateTimeDifferenceInSeconds(textBeanList.get(i).getDialogueEndTime(), textBeanList.get(i).getDialogueStartTime());
                        try {
                            String dialogueText = textBeanList.get(i).getDialogueText();
                            Log.d(TAG, "onCreate: " + dialogueText);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialogueTextView.setText(dialogueText);
                                }
                            }, 0);
                            Thread.sleep(Math.abs(l * 1000));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Detects the orientation of the current screen
    public static class ScreenUtils {
        public static boolean isLandscape(Context context) {
            int orientation = context.getResources().getConfiguration().orientation;
            return orientation == Configuration.ORIENTATION_LANDSCAPE;
        }
    }
}
