package com.example.doan.ExerciseRender;

public class Exercise {
    private int exerciseID;
    private String question, optionA, optionB, optionC, optionD, correctAnswer;

    public Exercise(int exerciseID, String question, String optionA, String optionB,
                    String optionC, String optionD, String correctAnswer) {
        this.exerciseID = exerciseID;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }

    public int getExerciseID() { return exerciseID; }
    public String getQuestion() { return question; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
}
