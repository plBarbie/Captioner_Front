package com.example.captioner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.captioner.network.RegisterRequest;
import com.example.captioner.network.LoginService;
import com.example.captioner.network.RegisterService;
import com.example.captioner.network.RetrofitClient;
import com.example.captioner.network.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText nameEditText, emailEditText, passwordEditText;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.rnameEditText);
        emailEditText = findViewById(R.id.remailEditText);
        passwordEditText = findViewById(R.id.rpasswordEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 创建你的RegisterRequest对象
        RegisterRequest registerRequest = new RegisterRequest(name, email, password);

        // 假设LoginService也用于注册，你可能需要根据实际情况调整
        RegisterService service = RetrofitClient.getClient("http://192.168.7.82:8080/").create(RegisterService.class);
        Call<UserResponse> call = service.registerUser(registerRequest);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        // 注册成功逻辑
                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                        // 可能的操作：跳转到登录页面或主页
                    } else {
                        // 服务器返回失败逻辑
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // HTTP错误处理
                    Toast.makeText(RegisterActivity.this, "Registration failed: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
