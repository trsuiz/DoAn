package com.example.doan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.Admin.Admin_panel;
import com.example.doan.PlayerHome.HomeActivity;

public class Login extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginBtn;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*SQLiteDatabase db = databaseHelper.getWritableDatabase();*/
        databaseHelper = new DatabaseHelper(this);
        /*databaseHelper.onUpgrade(db,1,2);*/
        /*databaseHelper.insertSampleData();*/
        databaseHelper.logAllDatabaseData();
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT Role FROM Users WHERE Username = ? AND PasswordHash = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String role = cursor.getString(cursor.getColumnIndexOrThrow("Role"));
                cursor.close();

                if ("admin".equalsIgnoreCase(role)) {
                    startActivity(new Intent(Login.this, Admin_panel.class));
                    finish();
                } else if ("player".equalsIgnoreCase(role)) {
                    startActivity(new Intent(Login.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Unknown role. Contact support.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                cursor.close();
            }
        }
    }

}