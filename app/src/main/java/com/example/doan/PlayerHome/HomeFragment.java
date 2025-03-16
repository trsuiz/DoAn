package com.example.doan.PlayerHome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.doan.ExerciseRender.ExerciseHolder;
import com.example.doan.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupButton(view, R.id.btnAboutYourself1, "About yourself: Lesson 1");
        setupButton(view, R.id.btnAboutYourself2, "About yourself: Lesson 2");
        setupButton(view, R.id.btnAboutYourself3, "About yourself: Lesson 3");
        setupButton(view, R.id.btnAboutYourself4, "About yourself: Lesson 4");
        setupButton(view, R.id.btnDailyRoutines1, "Daily routines: Lesson 1");
        setupButton(view, R.id.btnDailyRoutines2, "Daily routines: Lesson 2");
        setupButton(view, R.id.btnDailyRoutines3, "Daily routines: Lesson 3");
        setupButton(view, R.id.btnDailyRoutines4, "Daily routines: Lesson 4");
        setupButton(view, R.id.btnFood1, "Food: Lesson 1");

        return view;
    }

    private void setupButton(View view, int buttonId, String lessonName) {
        Button button = view.findViewById(buttonId);  // Correct initialization
        if (button != null) {
            button.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ExerciseHolder.class);
                intent.putExtra("selected_lesson", lessonName);
                startActivity(intent);
            });
        } else {
            // Optional: Log an error or handle the null case
            Log.e("HomeFragment", "Button with ID " + buttonId + " not found.");
        }
    }
}
