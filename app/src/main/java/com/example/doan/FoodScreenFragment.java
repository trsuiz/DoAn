package com.example.doan;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class FoodScreenFragment extends Fragment {

    private DatabaseHelper db;
    private TextView textViewQuestion;
    private Button buttonAnswer1, buttonAnswer2, buttonAnswer3, buttonAnswer4;

    // onCreateView to inflate the fragment's layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foodscreen, container, false);
        db = new DatabaseHelper(getContext());

        // Initialize views
        textViewQuestion = view.findViewById(R.id.textView);
        buttonAnswer1 = view.findViewById(R.id.button1);
        buttonAnswer2 = view.findViewById(R.id.button2);
        buttonAnswer3 = view.findViewById(R.id.button3);
        buttonAnswer4 = view.findViewById(R.id.button4);

        return view;
    }

    // onResume is called when the fragment becomes visible
    @Override
    public void onResume() {
        super.onResume();
        int currentPage = getArguments() != null ? getArguments().getInt("pageIndex") : 0;
        loadExerciseData(currentPage);  // Load data for the exercise based on the page index
    }

    public void loadExerciseData(int page) {
        // Get the lesson ID (could be hardcoded or based on the page)
        int lessonId = getLessonIdForPage(page); // Map the page number to the lesson ID
        if (lessonId == -1) return;

        // Now get the specific exercise for this lesson and page
        int exerciseId = page + 1; // Assuming page 0 is the first exercise, page 1 is the second, etc.

        // Query for the exercise based on the exercise ID
        Cursor exerciseCursor = db.getExerciseForLessonAndId(lessonId, exerciseId); // Use a query that fetches the correct exercise

        if (exerciseCursor != null && exerciseCursor.moveToFirst()) {
            // Extract exercise details
            int exerciseTextIndex = exerciseCursor.getColumnIndex("exercise_text");
            if (exerciseTextIndex != -1) {
                String exerciseText = exerciseCursor.getString(exerciseTextIndex);
                textViewQuestion.setText(exerciseText);
            }

            // Check answers
            int exerciseIdIndex = exerciseCursor.getColumnIndex("exercise_id");
            if (exerciseIdIndex != -1) {
                exerciseId = (int) exerciseCursor.getLong(exerciseIdIndex);
                Cursor answerCursor = db.getAnswersForExercise(exerciseId);

                if (answerCursor != null && answerCursor.moveToFirst()) {
                    String[] answers = new String[4];
                    int correctAnswerIndex = -1;
                    int index = 0;

                    do {
                        int answerTextIndex = answerCursor.getColumnIndex("answer_text");
                        int isCorrectIndex = answerCursor.getColumnIndex("is_correct");

                        if (answerTextIndex != -1 && isCorrectIndex != -1) {
                            answers[index] = answerCursor.getString(answerTextIndex);
                            if (answerCursor.getInt(isCorrectIndex) == 1) {
                                correctAnswerIndex = index;
                            }
                            index++;
                        }
                    } while (answerCursor.moveToNext());

                    // Assign answers to buttons
                    buttonAnswer1.setText(answers[0]);
                    buttonAnswer2.setText(answers[1]);
                    buttonAnswer3.setText(answers[2]);
                    buttonAnswer4.setText(answers[3]);

                    // Handle button click for correct/incorrect answer
                    int finalCorrectAnswerIndex = correctAnswerIndex;
                    buttonAnswer1.setOnClickListener(v -> checkAnswer(0, finalCorrectAnswerIndex));
                    buttonAnswer2.setOnClickListener(v -> checkAnswer(1, finalCorrectAnswerIndex));
                    buttonAnswer3.setOnClickListener(v -> checkAnswer(2, finalCorrectAnswerIndex));
                    buttonAnswer4.setOnClickListener(v -> checkAnswer(3, finalCorrectAnswerIndex));
                }

                answerCursor.close();
            }
        }

        exerciseCursor.close();
    }


    private void checkAnswer(int selectedAnswerIndex, int correctAnswerIndex) {
        if (selectedAnswerIndex == correctAnswerIndex) {
            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private int getLessonIdForPage(int page) {
        // You can modify this logic as needed
        return page + 1;  // Simple example: 1-based lesson ID
    }
}


