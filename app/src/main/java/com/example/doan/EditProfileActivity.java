package com.example.doan;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.DatabaseHelper;
import com.example.doan.R;

public class EditProfileActivity extends AppCompatActivity {
    private EditText fullNameInput, emailInput;
    private Button saveBtn;
    private DatabaseHelper databaseHelper;
    private String currentUsername; // Lưu username hiện tại để cập nhật

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullNameInput = findViewById(R.id.fullNameInput);
        emailInput = findViewById(R.id.emailInput);
        saveBtn = findViewById(R.id.saveBtn);

        databaseHelper = new DatabaseHelper(this);

        // ✅ Lấy username từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        currentUsername = preferences.getString("USERNAME", "");
        Log.d("DEBUG", "Loaded Username from SharedPreferences: " + currentUsername);

        if (currentUsername.isEmpty()) {
            Log.e("ERROR", "Lỗi: Không tìm thấy username trong SharedPreferences!");
        }

        if (!currentUsername.isEmpty()) {
            loadUserData(); // Load dữ liệu hiện tại vào EditText
        }

        saveBtn.setOnClickListener(v -> updateUserData());
    }

    private void loadUserData() {
        Log.d("DEBUG", "Loading data for Username: " + currentUsername);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT FullName, Email FROM Users WHERE Username = ?", new String[]{currentUsername});

        if (cursor.moveToFirst()) {
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow("FullName"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));

            fullNameInput.setText(fullName);
            emailInput.setText(email);
        }
        cursor.close();
    }


    private void updateUserData() {
        if (currentUsername == null || currentUsername.isEmpty()) {
            Log.e("ERROR", "Lỗi: currentUsername bị null hoặc rỗng!");
            Toast.makeText(this, "Lỗi: Không tìm thấy tài khoản hiện tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("DEBUG", "Current Username: " + currentUsername); // Kiểm tra giá trị username trước khi update

        String newFullName = fullNameInput.getText().toString().trim();
        String newEmail = emailInput.getText().toString().trim();

        if (newFullName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FullName", newFullName);
        values.put("Email", newEmail);

        int rows = db.update("Users", values, "Username = ?", new String[]{currentUsername});

        Log.d("DEBUG", "Rows affected: " + rows);
        if (rows > 0) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.e("ERROR", "Lỗi: Không tìm thấy username trong database!");
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }




    // ✅ Hàm mở EditProfileActivity từ ProfileFragment
    public static void openEditProfile(Context context) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        context.startActivity(intent);
    }
}
