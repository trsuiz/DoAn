package com.example.doan.ExerciseRender;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doan.DatabaseHelper;
import com.example.doan.ExerciseRender.Exercise;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

public class ExerciseHolder extends AppCompatActivity {

    DatabaseHelper db;
    ViewPager2 viewPager;
    ExercisePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercise_holder);

        db = new DatabaseHelper(this);
        /*db.insertSampleData();*/
        /*db.clearAllData();*/
        db.logAllDatabaseData();

        String selectedLesson = getIntent().getStringExtra("selected_lesson");

        viewPager = findViewById(R.id.viewPager);
        adapter = new ExercisePagerAdapter(this, new ArrayList<>());
        viewPager.setAdapter(adapter);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (selectedLesson != null) {
            if (selectedLesson.equals("About yourself: Lesson 1")) {
                loadLessonExercises("About yourself", "About yourself: Lesson1");
            } else if (selectedLesson.equals("About yourself: Lesson 2")) {
                loadLessonExercises("About yourself", "About yourself: Lesson2");
            } else if (selectedLesson.equals("About yourself: Lesson 3")) {
                loadLessonExercises("About yourself", "About yourself: Lesson3");
            } else if (selectedLesson.equals("About yourself: Lesson 4")) {
                loadLessonExercises("About yourself", "About yourself: Lesson4");
            } else if (selectedLesson.equals("Daily routines: Lesson 1")) {
                loadLessonExercises("Daily routines", "Daily routines: Lesson1");
            } else if (selectedLesson.equals("Daily routines: Lesson 2")) {
                loadLessonExercises("Daily routines", "Daily routines: Lesson2");
            } else if (selectedLesson.equals("Daily routines: Lesson 3")) {
                loadLessonExercises("Daily routines", "Daily routines: Lesson3");
            } else if (selectedLesson.equals("Daily routines: Lesson 4")) {
                loadLessonExercises("Daily routines", "Daily routines: Lesson4");
            } else if (selectedLesson.equals("Food: Lesson 1")) {
                loadLessonExercises("Food", "Food: Lesson1");
            } else {
                Toast.makeText(this, "Unknown lesson selected!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No lesson selected!", Toast.LENGTH_SHORT).show();
        }
    }


    // Function to load exercises for a specific topic and lesson
    public void loadLessonExercises(String topicName, String lessonName) {
        int lessonID = db.getLessonID(topicName, lessonName);

        if (lessonID == -1) {  // Invalid ID check
            Toast.makeText(this, "Lesson not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Exercise> exercises = db.getExercisesForLesson(lessonID);  // Correct call

        if (exercises == null || exercises.isEmpty()) {
            Toast.makeText(this, "No exercises found for this lesson!", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter.setExerciseList(exercises);  // Update ViewPager with exercises
    }
}