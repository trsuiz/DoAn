package com.example.doan.ExerciseRender;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.PlayerHome.HomeActivity;
import com.example.doan.PlayerHome.HomeFragment;
import com.example.doan.R;

public class ResultActivity extends AppCompatActivity {

    private TextView correctCountTextView, incorrectCountTextView, scoreTextView;
    private Button goBackButton;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        // Retrieve the data from the Intent
        int correctCount = getIntent().getIntExtra("correct_count", 0);
        int incorrectCount = getIntent().getIntExtra("incorrect_count", 0);
        Log.d("ResultFragment", "Correct Count: " + correctCount);
        Log.d("ResultFragment", "Incorrect Count: " + incorrectCount);

        // Get the TextViews
        correctCountTextView = findViewById(R.id.correctCount);
        incorrectCountTextView = findViewById(R.id.incorrectCount);
        scoreTextView = findViewById(R.id.scoreTextView);

        // Set correct and incorrect answers count
        correctCountTextView.setText("Correct Answers: " + correctCount);
        incorrectCountTextView.setText("Incorrect Answers: " + incorrectCount);

        // Calculate the score
        int score = correctCount * 5;  // 5 points for each correct answer

        // Update the score in SharedPreferences
        updateScore(score);

        // Display the current score
        scoreTextView.setText("Your Score: " + score);

        // Handle insets for edge-to-edge UI (optional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.exercise_result), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize and handle "Go Back" button
        goBackButton = findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(v -> navigateToHomeFragment());
    }

    private void navigateToHomeFragment() {
        // Create an Intent to navigate to HomeActivity
        Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
        startActivity(intent); // Start the HomeActivity
        finish();  // Optional: finish the current activity so it is removed from the back stack
    }

    // Method to update the score in SharedPreferences
    private void updateScore(int pointsToAdd) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_score", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Get the current score, add the points, and save it again
        int currentScore = getStoredScore();
        int newScore = currentScore + pointsToAdd;
        editor.putInt("score", newScore);
        editor.apply();  // Commit the changes
    }

    // Method to retrieve the score from SharedPreferences
    private int getStoredScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_score", MODE_PRIVATE);
        return sharedPreferences.getInt("score", 0);  // Default value is 0 if not found
    }
}
