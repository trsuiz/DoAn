package com.example.doan.Admin;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.DatabaseHelper;
import com.example.doan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExerciseCreate extends AppCompatActivity {

    private static final String TAG = "ExerciseCreate";
    private LinearLayout exerciseContentArea;

    private int questionCounter = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercise_create);

        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.logAllDatabaseData();

        int lessonID = getIntent().getIntExtra("LessonID", -1); // Default to -1 if not found
        String lessonName = getIntent().getStringExtra("LessonName");

        exerciseContentArea = findViewById(R.id.exercise_content_area); // Ensure this ID exists in the layout

        // Set the lesson name in the TextView
        TextView exerciseHeader = findViewById(R.id.adminExerciseCreateHeader);
        if (lessonName != null) {
            exerciseHeader.setText(lessonName);
        } else {
            exerciseHeader.setText("No Lesson Name"); // Fallback text
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.exercise_create), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (lessonID != -1) {
            displayExercises(lessonID);
        } else {
            Log.e(TAG, "Invalid LessonID received.");
        }

        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(v -> onBackPressed());

        // menu de them, xoa, sua topic
        FloatingActionButton fab = findViewById(R.id.fab_add_exercise);
        fab.setOnClickListener(v -> showPopupMenu(v));
    }

    private void displayExercises(int lessonID) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getReadableDatabase();

            try (Cursor cursor = db.rawQuery(
                    "SELECT ExerciseID, Question, OptionA, OptionB, OptionC, OptionD, CorrectAnswer " +
                            "FROM Exercises WHERE LessonID = ? ORDER BY ExerciseID ASC",
                    new String[]{String.valueOf(lessonID)})) {

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        String question = cursor.getString(cursor.getColumnIndexOrThrow("Question"));
                        String optionA = cursor.getString(cursor.getColumnIndexOrThrow("OptionA"));
                        String optionB = cursor.getString(cursor.getColumnIndexOrThrow("OptionB"));
                        String optionC = cursor.getString(cursor.getColumnIndexOrThrow("OptionC"));
                        String optionD = cursor.getString(cursor.getColumnIndexOrThrow("OptionD"));
                        String correctAnswer = cursor.getString(cursor.getColumnIndexOrThrow("CorrectAnswer"));

                        addExerciseToLayout(question, optionA, optionB, optionC, optionD, correctAnswer);
                    } while (cursor.moveToNext());
                } else {
                    Log.d(TAG, "No exercises found for this lesson.");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching exercises: ", e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    private void addExerciseToLayout(String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        LinearLayout exerciseLayout = new LinearLayout(this);
        exerciseLayout.setOrientation(LinearLayout.VERTICAL);
        exerciseLayout.setPadding(16, 16, 16, 16);
        exerciseLayout.setBackgroundColor(Color.parseColor("#FAFAFA"));

        // Prefix the question with the question number
        TextView questionView = createTextView(questionCounter + ". " + question, 18, true);
        TextView optionAView = createTextView("A: " + optionA, 16, false);
        TextView optionBView = createTextView("B: " + optionB, 16, false);
        TextView optionCView = createTextView("C: " + optionC, 16, false);
        TextView optionDView = createTextView("D: " + optionD, 16, false);
        TextView correctAnswerView = createTextView("Correct Answer: " + correctAnswer, 16, true);

        exerciseLayout.addView(questionView);
        exerciseLayout.addView(optionAView);
        exerciseLayout.addView(optionBView);
        exerciseLayout.addView(optionCView);
        exerciseLayout.addView(optionDView);
        exerciseLayout.addView(correctAnswerView);

        // Divider between exercises
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 2));
        divider.setBackgroundColor(Color.GRAY);

        exerciseContentArea.addView(exerciseLayout);
        exerciseContentArea.addView(divider);

        // Increment the question counter
        questionCounter++;
    }

    private TextView createTextView(String text, int textSize, boolean bold) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setGravity(Gravity.START);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(0, 8, 0, 8);
        textView.setTypeface(null, bold ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        return textView;
    }






    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.exercise_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            Log.d("ExerciseActivity", "Popup item clicked: " + title);

            switch (title) {
                case "Thêm bài tập":
                    openAddExerciseDialog();
                    return true;
                case "Sửa bài tập":
                    openEditExerciseDialog();
                    return true;
                case "Xoá bài tập":
                    openDeleteExerciseDialog();
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }

    private void openAddExerciseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm bài tập");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
        builder.setView(dialogView);

        EditText editQuestion = dialogView.findViewById(R.id.edit_question);
        EditText editOptionA = dialogView.findViewById(R.id.edit_option_a);
        EditText editOptionB = dialogView.findViewById(R.id.edit_option_b);
        EditText editOptionC = dialogView.findViewById(R.id.edit_option_c);
        EditText editOptionD = dialogView.findViewById(R.id.edit_option_d);
        EditText editCorrectAnswer = dialogView.findViewById(R.id.edit_correct_answer);
        Button btnAddExercise = dialogView.findViewById(R.id.btn_add_exercise);

        // Set hints for EditTexts
        editQuestion.setHint("Câu hỏi");
        editOptionA.setHint("Đáp án A");
        editOptionB.setHint("Đáp án B");
        editOptionC.setHint("Đáp án C");
        editOptionD.setHint("Đáp án D");
        editCorrectAnswer.setHint("Đáp án đúng (A, B, C, D)");
        btnAddExercise.setText("Thêm");

        AlertDialog dialog = builder.create();

        btnAddExercise.setOnClickListener(v -> {
            String question = editQuestion.getText().toString().trim();
            String optionA = editOptionA.getText().toString().trim();
            String optionB = editOptionB.getText().toString().trim();
            String optionC = editOptionC.getText().toString().trim();
            String optionD = editOptionD.getText().toString().trim();
            String correctAnswer = editCorrectAnswer.getText().toString().trim().toUpperCase();

            if (question.isEmpty()) {
                editQuestion.setError("Câu hỏi không được để trống");
            } else if (optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty()) {
                Toast.makeText(this, "Tất cả đáp án phải được điền đầy đủ", Toast.LENGTH_SHORT).show();
            } else if (!correctAnswer.matches("[ABCD]")) {
                editCorrectAnswer.setError("Đáp án đúng phải là A, B, C hoặc D");
            } else {
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("LessonID", getIntent().getIntExtra("LessonID", -1));
                values.put("Question", question);
                values.put("OptionA", optionA);
                values.put("OptionB", optionB);
                values.put("OptionC", optionC);
                values.put("OptionD", optionD);
                values.put("CorrectAnswer", correctAnswer);

                long result = db.insert("Exercises", null, values);
                if (result != -1) {
                    Toast.makeText(this, "Bài tập đã được thêm!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    displayExercises(getIntent().getIntExtra("LessonID", -1));
                } else {
                    Toast.makeText(this, "Thêm bài tập thất bại", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });

        dialog.show();
    }

    private void openDeleteExerciseDialog(){}
    private void openEditExerciseDialog(){}
}
