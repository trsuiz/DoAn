package com.example.doan.PlayerHome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.R;

public class ThumbnailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail);

        // Delay 2s before entering homeactivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(ThumbnailActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }, 2000);

    }
}
