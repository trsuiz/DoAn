package com.example.doan;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfileActivity extends AppCompatActivity {

    private EditText newPasswordInput;
    private Button changePasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        newPasswordInput = findViewById(R.id.newPasswordInput);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);

        changePasswordBtn.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String newPassword = newPasswordInput.getText().toString().trim();

        if (newPassword.isEmpty() || newPassword.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            finish(); // quay lại Profile
                        } else {
                            Toast.makeText(this, "Failed to change password", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Hàm mở activity từ ProfileFragment
    public static void openEditProfile(android.content.Context context) {
        context.startActivity(new android.content.Intent(context, EditProfileActivity.class));
    }
}
