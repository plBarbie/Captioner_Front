package com.example.captioner;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity extends AppCompatActivity {
    private FrameLayout displayPanel;
    private boolean isBlackBackground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        boolean isLandscape = ScreenUtils.isLandscape(DisplayActivity.this);
        final TextView horizontalHintText = findViewById(R.id.horizontalHintText);

        displayPanel = findViewById(R.id.clientDisplayPanel);

        Button lightButton = findViewById(R.id.lightButton);
        Button setupButton = findViewById(R.id.setupButton);

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
}