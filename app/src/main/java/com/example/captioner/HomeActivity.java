package com.example.captioner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button bookingButton = findViewById(R.id.bookingButton);
        Button bookedButton = findViewById(R.id.bookedButton);

        bookingButton.setOnClickListener(v -> {
            // Start the booking activity
            startActivity(new Intent(HomeActivity.this, BookingActivity.class));
            finish();
        });

        bookedButton.setOnClickListener(v -> {
            // Start the booked activity
            startActivity(new Intent(HomeActivity.this, BookedActivity.class));
            finish();
        });
    }
}