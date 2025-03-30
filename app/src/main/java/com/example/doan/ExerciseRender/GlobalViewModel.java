package com.example.doan.ExerciseRender;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GlobalViewModel extends ViewModel {
    private final MutableLiveData<Integer> correctCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> incorrectCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> answeredExercises = new MutableLiveData<>(0);
    private int totalExercises = 10;

    public LiveData<Integer> getCorrectCount() {
        return correctCount;
    }

    public LiveData<Integer> getIncorrectCount() {
        return incorrectCount;
    }

    public void incrementCorrectCount() {
        correctCount.setValue(correctCount.getValue() + 1);
    }

    public void incrementIncorrectCount() {
        incorrectCount.setValue(incorrectCount.getValue() + 1);
    }

    public void incrementAnsweredExercises() {
        answeredExercises.setValue(answeredExercises.getValue() + 1);
    }

    public boolean areAllExercisesAnswered() {
        return answeredExercises.getValue() == totalExercises;
    }

    // For setting counts explicitly if needed
    public void setCorrectCount(int count) {
        correctCount.setValue(count);
    }

    public void setIncorrectCount(int count) {
        incorrectCount.setValue(count);
    }
}

