package com.example.quanlydanhba.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlydanhba.R;
import com.example.quanlydanhba.ui.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    // Tài khoản cố định theo yêu cầu đề bài
    private static final String FIXED_USERNAME = "admin";
    private static final String FIXED_PASSWORD = "123456";

    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private TextView tvErrorMessage;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        // Xóa thông báo lỗi trước đó
        tilUsername.setError(null);
        tilPassword.setError(null);
        tvErrorMessage.setVisibility(View.GONE);

        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        // Kiểm tra không được để trống
        boolean hasError = false;
        if (TextUtils.isEmpty(username)) {
            tilUsername.setError(getString(R.string.username_hint) + " không được để trống");
            hasError = true;
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError(getString(R.string.password_hint) + " không được để trống");
            hasError = true;
        }

        if (hasError) {
            tvErrorMessage.setText(R.string.error_empty_fields);
            tvErrorMessage.setVisibility(View.VISIBLE);
            return;
        }

        // So khớp với tài khoản cố định
        if (FIXED_USERNAME.equals(username) && FIXED_PASSWORD.equals(password)) {
            // Đúng tài khoản -> Chuyển sang MainActivity và finish()
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Sai tài khoản hoặc mật khẩu -> Hiển thị thông báo và ở lại màn hình
            tvErrorMessage.setText(R.string.error_invalid_credentials);
            tvErrorMessage.setVisibility(View.VISIBLE);
        }
    }
}
