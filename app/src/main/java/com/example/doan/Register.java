package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {

    private EditText emailInput, passwordInput, fullNameInput;
    private EditText confirmPasswordInput;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private ImageView passwordToggle, confirmPasswordToggle;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        fullNameInput = findViewById(R.id.fullname_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        registerBtn = findViewById(R.id.register_btn);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        passwordToggle = findViewById(R.id.password_toggle);
        confirmPasswordToggle = findViewById(R.id.confirm_password_toggle);
        registerBtn.setOnClickListener(v -> registerUser());

        passwordToggle.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            passwordInput.setInputType(isPasswordVisible ?
                    InputType.TYPE_CLASS_TEXT :
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setSelection(passwordInput.length());
            passwordToggle.setImageResource(isPasswordVisible ?
                    R.drawable.ic_eye_open : R.drawable.ic_eye_closed);
        });

        confirmPasswordToggle.setOnClickListener(v -> {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            confirmPasswordInput.setInputType(isConfirmPasswordVisible ?
                    InputType.TYPE_CLASS_TEXT :
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            confirmPasswordInput.setSelection(confirmPasswordInput.length());
            confirmPasswordToggle.setImageResource(isConfirmPasswordVisible ?
                    R.drawable.ic_eye_open : R.drawable.ic_eye_closed);
        });

    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String fullName = fullNameInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Ràng buộc email
        if (email.isEmpty()) {
            emailInput.setError("Vui lòng nhập email");
            emailInput.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Email không hợp lệ");
            emailInput.requestFocus();
            return;
        }

        // Ràng buộc họ tên
        if (fullName.isEmpty()) {
            fullNameInput.setError("Vui lòng nhập họ tên");
            fullNameInput.requestFocus();
            return;
        }

        // Ràng buộc mật khẩu
        if (password.isEmpty()) {
            passwordInput.setError("Vui lòng nhập mật khẩu");
            passwordInput.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError("Mật khẩu phải có ít nhất 6 ký tự");
            passwordInput.requestFocus();
            return;
        }

        // Xác nhận mật khẩu
        if (confirmPassword.isEmpty()) {
            confirmPasswordInput.setError("Vui lòng xác nhận mật khẩu");
            confirmPasswordInput.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Mật khẩu không khớp");
            confirmPasswordInput.requestFocus();
            return;
        }

        // Đăng ký nếu hợp lệ
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Register.this, Login.class));
                                            finish();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Đăng ký thất bại. Email có thể đã tồn tại.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void redirectToLogin(View view) {
        startActivity(new Intent(Register.this, Login.class));
        finish();
    }

}
