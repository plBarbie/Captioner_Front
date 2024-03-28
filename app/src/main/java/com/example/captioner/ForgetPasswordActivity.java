package com.example.captioner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.captioner.network.ForgetPasswordRequest;
import com.example.captioner.network.ForgetPasswordService;
import com.example.captioner.network.RetrofitClient;
import com.example.captioner.network.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText nameEditText, emailEditText, passwordEditText;
    Button forgetPasswordButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        nameEditText = findViewById(R.id.fnameEditText);
        emailEditText = findViewById(R.id.femailEditText);
        passwordEditText = findViewById(R.id.fpasswordEditText);
        forgetPasswordButton = findViewById(R.id.forgetPasswordButton);

        forgetPasswordButton.setOnClickListener(v -> forgetPassword());
    }

    private void forgetPassword() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String backendUrl = getString(R.string.backend_url);
        // 创建你的RegisterRequest对象
        ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest(email, name, password);

        // 假设LoginService也用于注册，你可能需要根据实际情况调整
        ForgetPasswordService service = RetrofitClient.getClient(backendUrl).create(ForgetPasswordService.class);
        Call<UserResponse> call = service.forgetPassword(forgetPasswordRequest);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        // 注册成功逻辑
                        Toast.makeText(ForgetPasswordActivity.this, "Password finding successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                        finish();
//                        // 可能的操作：跳转到登录页面或主页
                    } else {
                        // 服务器返回失败逻辑
                        Toast.makeText(ForgetPasswordActivity.this, "Password finding failed: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // HTTP错误处理
                    Toast.makeText(ForgetPasswordActivity.this, "Password finding failed: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
//
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ForgetPasswordActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}