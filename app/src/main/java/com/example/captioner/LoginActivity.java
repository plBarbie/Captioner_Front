package com.example.captioner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.captioner.network.LoginRequest;
import com.example.captioner.network.LoginService;
import com.example.captioner.network.RetrofitClient;
import com.example.captioner.network.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    Button loginButton, signupButton, forgetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        forgetPasswordButton = findViewById(R.id.forgetPasswordButton);

        loginButton.setOnClickListener(v -> loginUser());
        signupButton.setOnClickListener(v -> {
            // Start the Register activity
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
        forgetPasswordButton.setOnClickListener(v -> {
            // Start the Register activity
            startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
        });
    }
//    String backendUrl = getString(R.string.backend_url);
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 假设你已经创建了RetrofitClient类和LoginService接口
        LoginService service = RetrofitClient.getClient("http://10.29.1.170:8080/").create(LoginService.class);
        Call<UserResponse> call = service.loginUser(new LoginRequest(email, password));

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse responseBody = response.body();
                    if (responseBody != null && responseBody.isSuccess()) {
                        // 登录成功逻辑
                        System.out.println("niubi");
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        Toast.makeText(LoginActivity.this, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        // 登录失败逻辑
                        Toast.makeText(LoginActivity.this, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // HTTP 错误处理
                    Toast.makeText(LoginActivity.this, "Login failed: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void saveUserEmail(String userEmail) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_email", userEmail);
        editor.apply();
    }
}
