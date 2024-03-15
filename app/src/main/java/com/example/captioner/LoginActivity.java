package com.example.captioner;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.captioner.model.LoginRequest;
import com.example.captioner.model.LoginResponse;
import com.example.captioner.network.ApiClient;
import com.example.captioner.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    Button loginButton, signupButton, forgetPasswordButton;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);
        forgetPasswordButton = findViewById(R.id.forgetPasswordButton);

        // 创建 Retrofit 实例
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // TODO: Add other buttons activities
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // 检查输入是否为空
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // 创建登录请求对象
            LoginRequest request = new LoginRequest(email, password);

            // 发送登录请求到后端
            Call<LoginResponse> call = apiInterface.loginUser(request);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        // 登录成功，处理响应
                        LoginResponse loginResponse = response.body();
                        // TODO: 处理登录成功逻辑
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        // 登录失败，处理错误响应
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    // 网络请求失败，处理错误
                    Toast.makeText(LoginActivity.this, "Network error. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
}