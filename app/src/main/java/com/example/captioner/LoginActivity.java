package com.example.captioner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText idEditText, passwordEditText;
    Button loginButton, signupButton, forgetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signupButton = findViewById(R.id.signupButton);
        Button loginButton = findViewById(R.id.loginButton);
        Button forgetPasswordButton = findViewById(R.id.forgetPasswordButton);

        // TODO: Add other buttons activities
        loginButton.setOnClickListener(v -> {
            // TODO: Add how to login
            // Start the display activity
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        });

    }
}