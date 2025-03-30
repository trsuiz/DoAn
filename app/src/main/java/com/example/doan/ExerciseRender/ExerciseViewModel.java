package com.example.doan.ExerciseRender;

import androidx.lifecycle.ViewModel;


import androidx.lifecycle.ViewModel;

public class ExerciseViewModel extends ViewModel {
    // Store the checkbox state for each exercise in each lesson (10 checkboxes per exercise)
    private boolean[][] checkboxStates;
    private boolean[][] answeredState;  // Track whether the exercise has been answered

    private boolean[] exerciseAnswered;  // Track the answered status of exercises globally

    public ExerciseViewModel() {
        checkboxStates = new boolean[10][10];  // 10 lessons, each having 10 checkboxes
        answeredState = new boolean[10][10];  // 10 lessons, each having 10 exercises
        exerciseAnswered = new boolean[100];  // Assuming 100 exercises in total
    }

    // Get the checkbox state for a given lesson and exercise ID (1-10, 11-20, etc.)
    public boolean getCheckboxState(int lessonNumber, int exerciseID) {
        int positionInLesson = (exerciseID - 1) % 10; // Maps exerciseID to position (0-9)
        return checkboxStates[lessonNumber - 1][positionInLesson];
    }

    // Set the checkbox state for a given lesson and exercise ID
    public void setCheckboxState(int lessonNumber, int exerciseID, boolean isChecked) {
        int positionInLesson = (exerciseID - 1) % 10;
        checkboxStates[lessonNumber - 1][positionInLesson] = isChecked;
    }

    // Check if the exercise has been answered
    public boolean isExerciseAnswered(int exerciseID) {
        return exerciseAnswered[exerciseID - 1];
    }

    // Set the exercise as answered
    public void setExerciseAnswered(int exerciseID) {
        exerciseAnswered[exerciseID - 1] = true;
    }


}
