package com.example.doan.ExerciseRender;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan.ExerciseRender.Exercise;
import com.example.doan.ExerciseRender.ExerciseFragment;

import java.util.List;

public class ExercisePagerAdapter extends FragmentStateAdapter {

    private List<Exercise> exerciseList;

    public ExercisePagerAdapter(@NonNull FragmentActivity fa, List<Exercise> exercises) {
        super(fa);
        this.exerciseList = exercises;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Pass exercise ID to fragment
        return ExerciseFragment.newInstance(exerciseList.get(position).getExerciseID());
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();  // Size is determined by the exercise list
    }

    public void setExerciseList(List<Exercise> exercises) {
        this.exerciseList = exercises;
        notifyDataSetChanged();
    }
}
