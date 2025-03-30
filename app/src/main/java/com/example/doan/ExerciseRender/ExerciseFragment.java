package com.example.doan.ExerciseRender;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;  // Don't forget to import the Log class
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.doan.DatabaseHelper;
import com.example.doan.R;

public class ExerciseFragment extends Fragment {

    private static final String ARG_EXERCISE_ID = "exercise_id";
    private int exerciseID;
    private int lessonNumber;
    int i = 1;
    private ExerciseViewModel exerciseViewModel;  // Reference to ViewModel

    private CheckBox[] checkboxes = new CheckBox[10];

    private int correctCount = 0;  // Variable to count correct answers
    private int incorrectCount = 0;  // Variable to count incorrect answers
    private int answeredExercises = 0;  // Variable to track answered exercises
    private int totalExercises = 10;  // Set this based on the number of exercises in the lesson (assumed 10 here)

    public static ExerciseFragment newInstance(int exerciseID) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EXERCISE_ID, exerciseID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseID = getArguments().getInt(ARG_EXERCISE_ID);
            lessonNumber = (exerciseID - 1) / 10 + 1;  // Calculate the lesson number based on the exerciseID
        }

        exerciseViewModel = new ExerciseViewModel();  // Initialize the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /*disableAllCheckboxes();*/
        boolean isChecked;
        TextView questionTextView = view.findViewById(R.id.textView);
        Button btn1 = view.findViewById(R.id.button1);
        Button btn2 = view.findViewById(R.id.button2);
        Button btn3 = view.findViewById(R.id.button3);
        Button btn4 = view.findViewById(R.id.button4);

        Button btnExit = view.findViewById(R.id.btnExit);  // Use 'view.findViewById'
        btnExit.setOnClickListener(v -> getActivity().finish());  // Close the activity

        DatabaseHelper db = new DatabaseHelper(getContext());
        Exercise exercise = db.getExercise(exerciseID);

        if (exercise != null) {
            // Calculate the question number (1-10 based on exerciseID)
            int questionNumber = (exerciseID - 1) % 10 + 1;

            // Set the question number with the question text
            String questionWithNumber = questionNumber + ". " + exercise.getQuestion();
            questionTextView.setText(questionWithNumber);  // Set the question text with the number
            btn1.setText(exercise.getOptionA());
            btn2.setText(exercise.getOptionB());
            btn3.setText(exercise.getOptionC());
            btn4.setText(exercise.getOptionD());

            View.OnClickListener listener = v -> {
                String selectedText = ((Button) v).getText().toString();
                checkAnswer(selectedText, exercise.getCorrectAnswer(), getArguments().getInt(ARG_EXERCISE_ID));
            };
            btn1.setOnClickListener(listener);
            btn2.setOnClickListener(listener);
            btn3.setOnClickListener(listener);
            btn4.setOnClickListener(listener);
        } else {
            Toast.makeText(getContext(), "Exercise not found!", Toast.LENGTH_SHORT).show();
        }

        // Initialize checkboxes for each exercise (10 checkboxes per fragment)
        checkboxes[0] = view.findViewById(R.id.checkbox1);
        checkboxes[1] = view.findViewById(R.id.checkbox2);
        checkboxes[2] = view.findViewById(R.id.checkbox3);
        checkboxes[3] = view.findViewById(R.id.checkbox4);
        checkboxes[4] = view.findViewById(R.id.checkbox5);
        checkboxes[5] = view.findViewById(R.id.checkbox6);
        checkboxes[6] = view.findViewById(R.id.checkbox7);
        checkboxes[7] = view.findViewById(R.id.checkbox8);
        checkboxes[8] = view.findViewById(R.id.checkbox9);
        checkboxes[9] = view.findViewById(R.id.checkbox10);

        // Set checkbox states based on the ViewModel for this exercise
        for (int i = 0; i < 10; i++) {
            isChecked = exerciseViewModel.getCheckboxState(lessonNumber, exerciseID * 10 + i + 1);
            checkboxes[i].setChecked(isChecked);

            // Handle checkbox change
            int finalI = i;
            checkboxes[i].setOnCheckedChangeListener((buttonView, isChecked1) -> {
                // Update checkbox state in the ViewModel
                exerciseViewModel.setCheckboxState(lessonNumber, exerciseID * 10 + finalI + 1, isChecked1);
            });
        }
    }

    private void checkAnswer(String selectedText, String correctAnswer, int exerciseID) {
        GlobalViewModel globalViewModel = new ViewModelProvider(requireActivity()).get(GlobalViewModel.class);


        String selectedOption = "";

        // Get buttons for comparison
        Button btn1 = getView().findViewById(R.id.button1);
        Button btn2 = getView().findViewById(R.id.button2);
        Button btn3 = getView().findViewById(R.id.button3);
        Button btn4 = getView().findViewById(R.id.button4);

        // Determine selected option (A, B, C, or D)
        if (selectedText.equals(btn1.getText().toString())) {
            selectedOption = "A";
        } else if (selectedText.equals(btn2.getText().toString())) {
            selectedOption = "B";
        } else if (selectedText.equals(btn3.getText().toString())) {
            selectedOption = "C";
        } else if (selectedText.equals(btn4.getText().toString())) {
            selectedOption = "D";
        }

        // Log selected and correct answer for debugging
        Log.d("ExerciseFragment", "Selected Option: " + selectedOption);
        Log.d("ExerciseFragment", "Correct Answer: " + correctAnswer);

        // Show Toast message for correct or incorrect answer
        if (selectedOption.equals(correctAnswer)) {
            globalViewModel.incrementCorrectCount();  // Increment correct count
            Toast toast = Toast.makeText(getContext(), "Correct Answer!", Toast.LENGTH_SHORT);  // Correct answer Toast
            toast.show();

            // Cancel the toast after a very short time to make it disappear quickly
            new Handler().postDelayed(toast::cancel, 400); // Cancel after 200ms
        } else {
            globalViewModel.incrementIncorrectCount();  // Increment incorrect count
            Toast toast = Toast.makeText(getContext(), "Incorrect Answer!", Toast.LENGTH_SHORT);  // Incorrect answer Toast
            toast.show();

            // Cancel the toast after a very short time to make it disappear quickly
            new Handler().postDelayed(toast::cancel, 400); // Cancel after 200ms
        }

        globalViewModel.incrementAnsweredExercises();  // Increment answered exercises

        // Log counts after updating
        Log.d("ExerciseFragment", "Correct Count: " + correctCount);
        Log.d("ExerciseFragment", "Incorrect Count: " + incorrectCount);
        Log.d("ExerciseFragment", "Answered Exercises: " + answeredExercises);

        // Update the UI (checkbox state) and disable buttons
        // Calculate the lesson number based on the exercise ID (e.g., ID 1-10 -> Lesson 1, ID 11-20 -> Lesson 2)
        int lessonNumber = (exerciseID - 1) / 10 + 1;

        // Set the checkbox state for the correct exercise ID in the correct lesson
        exerciseViewModel.setCheckboxState(lessonNumber, exerciseID, selectedOption.equals(correctAnswer));

        // Update the checkbox UI for the exercise
        markCheckbox(selectedOption.equals(correctAnswer), exerciseID);

        // After all exercises are answered, check if we need to navigate
        if (globalViewModel.areAllExercisesAnswered()) {
            goToResultActivity();
        }
    }

    private void goToResultActivity() {
        // Create an Intent to navigate to the ResultActivity
        Intent intent = new Intent(getContext(), ResultActivity.class);

        // Retrieve the data from GlobalViewModel
        GlobalViewModel globalViewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);

        // Assuming correctCount and incorrectCount are values from GlobalViewModel
        int correctCount = globalViewModel.getCorrectCount().getValue() != null ? globalViewModel.getCorrectCount().getValue() : 0;
        int incorrectCount = globalViewModel.getIncorrectCount().getValue() != null ? globalViewModel.getIncorrectCount().getValue() : 0;
        Log.d("ExerciseFragment", "Correct Count: " + correctCount);
        Log.d("ExerciseFragment", "Incorrect Count: " + incorrectCount);
        // Pass the data via Intent
        intent.putExtra("correct_count", correctCount);
        intent.putExtra("incorrect_count", incorrectCount);

        // Start the ResultActivity
        startActivity(intent);
    }




    private void disableAnswerButtons() {
        // Disable all answer buttons after the user has selected an answer
        Button btn1 = getView().findViewById(R.id.button1);
        Button btn2 = getView().findViewById(R.id.button2);
        Button btn3 = getView().findViewById(R.id.button3);
        Button btn4 = getView().findViewById(R.id.button4);

        // Disable all the buttons to prevent further interaction
        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        btn4.setEnabled(false);

        Log.d("ExerciseFragment", "All answer buttons are now disabled.");
    }

    private void markCheckbox(boolean isCorrect, int exercisePosition) {
        Log.d("Checkbox", "markCheckbox called with isCorrect: " + isCorrect + " and exercisePosition: " + exercisePosition);

        // Determine which checkbox to update based on the exerciseID
        int positionInLesson = (exerciseID - 1) % 10; // Get the position (0-9 for each lesson)

        // Get all checkboxes in the layout (assuming you have 10 checkboxes)
        CheckBox[] checkboxes = {
                getView().findViewById(R.id.checkbox1),
                getView().findViewById(R.id.checkbox2),
                getView().findViewById(R.id.checkbox3),
                getView().findViewById(R.id.checkbox4),
                getView().findViewById(R.id.checkbox5),
                getView().findViewById(R.id.checkbox6),
                getView().findViewById(R.id.checkbox7),
                getView().findViewById(R.id.checkbox8),
                getView().findViewById(R.id.checkbox9),
                getView().findViewById(R.id.checkbox10)
        };

        // Check if the position is valid
        if (positionInLesson >= 0 && positionInLesson < 10) {
            checkboxes[positionInLesson].setChecked(isCorrect);
        }
    }


    private void disableAllCheckboxes() {
        // Disable all checkboxes to prevent further interaction
        CheckBox checkbox1 = getView().findViewById(R.id.checkbox1);
        CheckBox checkbox2 = getView().findViewById(R.id.checkbox2);
        CheckBox checkbox3 = getView().findViewById(R.id.checkbox3);
        CheckBox checkbox4 = getView().findViewById(R.id.checkbox4);
        CheckBox checkbox5 = getView().findViewById(R.id.checkbox5);
        CheckBox checkbox6 = getView().findViewById(R.id.checkbox6);
        CheckBox checkbox7 = getView().findViewById(R.id.checkbox7);
        CheckBox checkbox8 = getView().findViewById(R.id.checkbox8);
        CheckBox checkbox9 = getView().findViewById(R.id.checkbox9);
        CheckBox checkbox10 = getView().findViewById(R.id.checkbox10);

        checkbox1.setEnabled(false);
        checkbox2.setEnabled(false);
        checkbox3.setEnabled(false);
        checkbox4.setEnabled(false);
        checkbox5.setEnabled(false);
        checkbox6.setEnabled(false);
        checkbox7.setEnabled(false);
        checkbox8.setEnabled(false);
        checkbox9.setEnabled(false);
        checkbox10.setEnabled(false);

        Log.d("Checkbox", "All checkboxes have been disabled.");
    }
}

