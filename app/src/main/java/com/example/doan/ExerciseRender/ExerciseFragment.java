package com.example.doan.ExerciseRender;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doan.DatabaseHelper;
import com.example.doan.ExerciseRender.Exercise;
import com.example.doan.R;

public class ExerciseFragment extends Fragment {

    private static final String ARG_EXERCISE_ID = "exercise_id";
    private int exerciseID;

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
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
            questionTextView.setText(exercise.getQuestion());
            btn1.setText(exercise.getOptionA());
            btn2.setText(exercise.getOptionB());
            btn3.setText(exercise.getOptionC());
            btn4.setText(exercise.getOptionD());

            View.OnClickListener listener = v -> checkAnswer(((Button) v).getText().toString(), exercise.getCorrectAnswer());
            btn1.setOnClickListener(listener);
            btn2.setOnClickListener(listener);
            btn3.setOnClickListener(listener);
            btn4.setOnClickListener(listener);
        } else {
            Toast.makeText(getContext(), "Exercise not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAnswer(String selectedText, String correctAnswer) {
        String selectedOption = "";

        Button btn1 = getView().findViewById(R.id.button1);
        Button btn2 = getView().findViewById(R.id.button2);
        Button btn3 = getView().findViewById(R.id.button3);
        Button btn4 = getView().findViewById(R.id.button4);

        if (selectedText.equals(btn1.getText().toString())) {
            selectedOption = "A";
        } else if (selectedText.equals(btn2.getText().toString())) {
            selectedOption = "B";
        } else if (selectedText.equals(btn3.getText().toString())) {
            selectedOption = "C";
        } else if (selectedText.equals(btn4.getText().toString())) {
            selectedOption = "D";
        }

        String message = selectedOption.equals(correctAnswer) ? "Correct Answer!" : "Wrong Answer!";
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}