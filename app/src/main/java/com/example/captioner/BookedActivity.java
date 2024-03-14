package com.example.captioner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class BookedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);

        Button returnButton = findViewById(R.id.returnButton);

        returnButton.setOnClickListener(v -> {
            // Start the home activity
            startActivity(new Intent(BookedActivity.this, HomeActivity.class));
        });
    }
}