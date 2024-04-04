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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.captioner.model.DialogueDTO;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {
    private final Handler handler = new Handler();
    Runnable runnable;
    private TextView dialogueTextView;
    private FrameLayout displayPanel;
    private boolean isBlackBackground = false;

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

        if (dialogueList != null) {
            // 在这里处理传递过来的 PlayBean 对象
            int targetTime = (int) getIntent().getSerializableExtra("targetTime");

            int position = 0;
            // 初始化最小差值为一个足够大的值
            int minDifference = Integer.MAX_VALUE;
//            String nearestTimeNode = null;
            //找到最接近得时间节点
            for (int i = 0; i < dialogueList.size(); i++) {
                int seconds = convertToSeconds(dialogueList.get(i).getDialogueStartTime());
                int difference = Math.abs(targetTime - seconds);
                if (difference < minDifference) {
                    minDifference = difference;
//                    nearestTimeNode = dialogueList.get(i).getDialogueStartTime();
                    position = i;
                }
            }
            //输出最接近的时间节点
//            Log.d(TAG, "onCreate: " + "与时间 " + targetTime + " 秒最接近的时间节点是：" + nearestTimeNode);

            //开始时间第一个节点
            LocalTime locFirstTime = LocalTime.parse(dialogueList.get(0).getDialogueStartTime());
            long millisecondFirstTime = locFirstTime.toSecondOfDay() * 1000L;
            long timeDelay = (int) (millisecondFirstTime - (targetTime * 1000));
            if (timeDelay > 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runTextView(dialogueList, 0);
                    }
                }, timeDelay);
            } else {
                runTextView(dialogueList, position);
            }
        } else {
            Toast.makeText(this, "Failed to load this play.", Toast.LENGTH_SHORT).show();
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

        setupButton.setOnClickListener(v -> {
            // Create a dialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(DisplayActivity.this);
            // Set the title and message for the dialog
            builder.setTitle("Adjust font size and select font color:");
//            builder.setMessage("Adjust font size and select font color:");

            // Inflate the custom layout for the dialog
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_setup, null);
            builder.setView(dialogView);

            // Find views in the custom layout
            SeekBar seekBarFontSize = dialogView.findViewById(R.id.seekBarFontSize);
            TextView textViewFontSize = dialogView.findViewById(R.id.textViewFontSize);
            Spinner spinnerFontColor = dialogView.findViewById(R.id.spinnerFontColor);

            // Initialize font size seek bar and text view
            int currentFontSize = (int) dialogueTextView.getTextSize();
            seekBarFontSize.setProgress(currentFontSize);
            textViewFontSize.setText("Font Size: " + currentFontSize);

            // Define the font size seek bar listener
            seekBarFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // Update the font size text view as the seek bar changes
                    textViewFontSize.setText("Font Size: " + progress);
                    dialogueTextView.setTextSize(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            // Define the font color spinner listener
            spinnerFontColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Set the font color based on the selected item in the spinner
                    String selectedColor = parent.getItemAtPosition(position).toString();
                    switch (selectedColor) {
                        case "Black":
                            dialogueTextView.setTextColor(Color.BLACK);
                            break;
                        case "White":
                            dialogueTextView.setTextColor(Color.WHITE);
                            break;
                        case "Red":
                            dialogueTextView.setTextColor(Color.RED);
                            break;
                        case "Yellow":
                            dialogueTextView.setTextColor(Color.YELLOW);
                            break;
                        case "Blue":
                            dialogueTextView.setTextColor(Color.BLUE);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // Add buttons for positive and negative actions
            builder.setPositiveButton("Save", (dialog, which) -> {
                // Save the selected options if needed
                // You can add your save logic here
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Dismiss the dialog without saving
                dialog.dismiss();
            });

            // Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        // If not landscape, show the hint text for 3s
        if (!isLandscape) {
            // Show the horizontal hint text
            horizontalHintText.setVisibility(View.VISIBLE);
            // Hide the horizontal hint text after 3 seconds
            new Handler().postDelayed(() -> horizontalHintText.setVisibility(View.GONE), 3000); // 3000 milliseconds delay
        }
    }

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
