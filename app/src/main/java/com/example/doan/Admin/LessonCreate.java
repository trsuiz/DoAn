package com.example.doan.Admin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.DatabaseHelper;
import com.example.doan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LessonCreate extends AppCompatActivity {

    private static final String TAG = "LessonCreate";
    private LinearLayout contentArea;
    private int topicID;
    private String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson_create);

        contentArea = findViewById(R.id.lesson_content_area);
        setupEdgeToEdge();

        Intent intent = getIntent();
        topicID = getIntent().getIntExtra("TopicID", -1); // Default to -1 if not found
        topicName = getIntent().getStringExtra("TopicName");
        Log.d("LessonActivity", "Received TopicID: " + topicID);
        Log.d("LessonCreate", "Received TopicID: " + topicID + ", TopicName: " + topicName);


        TextView adminTopicCreateHeader;
        adminTopicCreateHeader = (TextView) findViewById(R.id.adminLessonCreateHeader);

        if (topicName != null) {
            adminTopicCreateHeader.setText(topicName);
        } else {
            adminTopicCreateHeader.setText("Topic"); // Fallback text
        }

        displayLessons();
        if (topicID == -1) {
            Toast.makeText(this, "Error loading topic details.", Toast.LENGTH_SHORT).show();
            finish();
        }

        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(v -> onBackPressed());

        // menu de them, xoa, sua topic
        FloatingActionButton fab = findViewById(R.id.fab_add_lesson);
        fab.setOnClickListener(v -> showPopupMenu(v));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (popupMenu != null) popupMenu.dismiss();*/
    }



    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lesson_create), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    ////////////////////////////////
    // Hien thi menu them, xoa, sua
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.lesson_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            Log.d("LessonActivity", "Popup item clicked: " + item.getTitle());
            String title = item.getTitle().toString();

            switch (title) {
                case "Thêm bài học":
                    openAddLessonDialog();
                    return true;
                case "Xoá bài học":
                    openDeleteLessonDialog();
                    return true;
                case "Sửa bài học":
                    openEditLessonDialog();
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }

    private void openAddLessonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm bài học");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_topic, null);
        builder.setView(dialogView);

        EditText editLessonName = dialogView.findViewById(R.id.edit_topic_name);
        EditText editLessonContent = dialogView.findViewById(R.id.edit_description);
        Button btnAddLesson = dialogView.findViewById(R.id.btn_add_topic);

        // Update hints and button text
        editLessonName.setHint("Tên bài học");
        editLessonContent.setHint("Nội dung bài học (tùy chọn)");
        btnAddLesson.setText("Thêm");

        AlertDialog dialog = builder.create();

        btnAddLesson.setOnClickListener(v -> {
            String lessonName = editLessonName.getText().toString().trim();
            String lessonContent = editLessonContent.getText().toString().trim();

            if (!lessonName.isEmpty()) {
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                dbHelper.insertLesson(topicID, lessonName, lessonContent);
                Toast.makeText(this, "Bài học đã được thêm!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                displayLessons();
            } else {
                editLessonName.setError("Tên bài học không được để trống");
            }
        });

        dialog.show();
    }

    private void openDeleteLessonDialog() {
        Log.d("LessonActivity", "openDeleteLessonDialog called");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xoá bài học");

        // Create layout container for dialog
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(32, 32, 32, 32);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Integer> lessonIDs = new ArrayList<>();
        List<String> lessonNames = new ArrayList<>();
        List<CheckBox> checkBoxes = new ArrayList<>();

        try (Cursor cursor = db.rawQuery(
                "SELECT LessonID, LessonName FROM Lessons WHERE TopicID = ?",
                new String[]{String.valueOf(topicID)}))
        { // Filter by TopicID
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int lessonID = cursor.getInt(cursor.getColumnIndexOrThrow("LessonID"));
                    String lessonName = cursor.getString(cursor.getColumnIndexOrThrow("LessonName"));

                    lessonIDs.add(lessonID);
                    lessonNames.add(lessonName);

                    // Create layout for each lesson row
                    LinearLayout lessonRow = new LinearLayout(this);
                    lessonRow.setOrientation(LinearLayout.HORIZONTAL);
                    lessonRow.setPadding(0, 8, 0, 8);

                    TextView lessonText = new TextView(this);
                    lessonText.setText(lessonName);
                    lessonText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

                    CheckBox checkBox = new CheckBox(this);
                    checkBoxes.add(checkBox);

                    lessonRow.addView(lessonText);
                    lessonRow.addView(checkBox);
                    dialogLayout.addView(lessonRow);

                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "Không có bài học nào để xóa.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Log.e("LessonActivity", "Error fetching lessons: ", e);
        } finally {
            db.close();
        }

        // Delete button
        Button deleteButton = new Button(this);
        deleteButton.setText("Xoá");
        deleteButton.setOnClickListener(v -> {
            List<Integer> lessonsToDelete = new ArrayList<>();

            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isChecked()) {
                    lessonsToDelete.add(lessonIDs.get(i));
                }
            }

            Log.d("LessonActivity", "Selected lessons to delete: " + lessonsToDelete);

            if (lessonsToDelete.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một bài học để xóa.", Toast.LENGTH_SHORT).show();
            } else {
                showDeleteLessonConfirmationDialog(lessonsToDelete);
            }
        });

        dialogLayout.addView(deleteButton);
        builder.setView(dialogLayout);
        builder.setNegativeButton("Huỷ", null);
        builder.show();
    }



    private void showDeleteLessonConfirmationDialog(List<Integer> lessonIDs) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setTitle("Bạn có chắc muốn xoá các bài học này?");

        LinearLayout confirmLayout = new LinearLayout(this);
        confirmLayout.setOrientation(LinearLayout.VERTICAL);
        confirmLayout.setPadding(32, 32, 32, 32);

        TextView message = new TextView(this);
        message.setText("Bạn không thể khôi phục hành động này.");
        confirmLayout.addView(message);

        LinearLayout checkboxRow = new LinearLayout(this);
        checkboxRow.setOrientation(LinearLayout.HORIZONTAL);
        checkboxRow.setGravity(Gravity.CENTER);

        List<CheckBox> confirmBoxes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CheckBox cb = new CheckBox(this);
            confirmBoxes.add(cb);
            checkboxRow.addView(cb);
        }

        confirmLayout.addView(checkboxRow);

        LinearLayout buttonsLayout = new LinearLayout(this);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setGravity(Gravity.END);

        Button cancelButton = new Button(this);
        cancelButton.setText("Huỷ");
        cancelButton.setOnClickListener(v -> confirmBuilder.create().dismiss());

        Button confirmDeleteButton = new Button(this);
        confirmDeleteButton.setText("Xoá");
        confirmDeleteButton.setOnClickListener(v -> {
            boolean allChecked = confirmBoxes.stream().allMatch(CheckBox::isChecked);
            Log.d("LessonActivity", "All checkboxes checked: " + allChecked);

            if (allChecked) {
                deleteSelectedLessons(lessonIDs);
                confirmBuilder.create().dismiss();
            } else {
                Toast.makeText(this, "Vui lòng xác nhận bằng cách chọn tất cả các ô.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonsLayout.addView(cancelButton);
        buttonsLayout.addView(confirmDeleteButton);
        confirmLayout.addView(buttonsLayout);

        confirmBuilder.setView(confirmLayout);
        confirmBuilder.show();
    }


    private void deleteSelectedLessons(List<Integer> lessonIDs) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            for (int lessonID : lessonIDs) {
                int rowsDeleted = db.delete("Lessons", "LessonID = ?", new String[]{String.valueOf(lessonID)});
                Log.d("LessonActivity", "Deleted LessonID " + lessonID + ", rows: " + rowsDeleted);
            }
            db.setTransactionSuccessful();
            Toast.makeText(this, "Bài học đã được xóa thành công.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("LessonActivity", "Error deleting lessons: ", e);
            Toast.makeText(this, "Lỗi khi xóa bài học.", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
            db.close();
            displayLessons();  // Refresh lessons list
        }
    }





    private void openEditLessonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa bài học");

        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(32, 32, 32, 32);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Integer> lessonIDs = new ArrayList<>();
        List<CheckBox> checkBoxes = new ArrayList<>();
        EditText editLessonName = new EditText(this);
        EditText editContent = new EditText(this);
        editLessonName.setHint("Tên bài học");
        editContent.setHint("Nội dung");

        try (Cursor cursor = db.rawQuery("SELECT LessonID, LessonName, Content FROM Lessons WHERE TopicID = ?",
                new String[]{String.valueOf(topicID)})) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int lessonID = cursor.getInt(cursor.getColumnIndexOrThrow("LessonID"));
                    String lessonName = cursor.getString(cursor.getColumnIndexOrThrow("LessonName"));
                    String content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));

                    lessonIDs.add(lessonID);

                    CheckBox checkBox = new CheckBox(this);
                    checkBox.setText(lessonName);
                    checkBoxes.add(checkBox);
                    dialogLayout.addView(checkBox);

                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            for (CheckBox cb : checkBoxes) {
                                if (cb != checkBox) cb.setChecked(false);
                            }
                            editLessonName.setText(lessonName);
                            editContent.setText(content);
                        } else {
                            editLessonName.setText("");
                            editContent.setText("");
                        }
                    });

                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "Không có bài học nào trong chủ đề này để sửa.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching lessons for edit: ", e);
        } finally {
            db.close();
        }

        dialogLayout.addView(editLessonName);
        dialogLayout.addView(editContent);

        Button btnEdit = new Button(this);
        btnEdit.setText("Sửa bài học");
        btnEdit.setOnClickListener(v -> {
            int selectedIndex = -1;
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isChecked()) {
                    selectedIndex = i;
                    break;
                }
            }

            if (selectedIndex != -1) {
                String newName = editLessonName.getText().toString().trim();
                String newContent = editContent.getText().toString().trim();

                if (!newName.isEmpty()) {
                    updateLesson(lessonIDs.get(selectedIndex), newName, newContent);
                    Toast.makeText(this, "Bài học đã được sửa!", Toast.LENGTH_SHORT).show();
                    displayLessons(); // Refresh the lesson list
                } else {
                    editLessonName.setError("Tên bài học không được để trống.");
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn một bài học.", Toast.LENGTH_SHORT).show();
            }
        });

        dialogLayout.addView(btnEdit);
        builder.setView(dialogLayout);
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    private void updateLesson(int lessonID, String newName, String newContent) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("LessonName", newName);
        values.put("Content", newContent);

        db.update("Lessons", values, "LessonID = ?", new String[]{String.valueOf(lessonID)});
        db.close();
    }




    private void displayLessons() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getReadableDatabase();
            contentArea.removeAllViews();

            try (Cursor cursor = db.rawQuery("SELECT LessonID, LessonName FROM Lessons WHERE TopicID = ?",
                    new String[]{String.valueOf(topicID)})) {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        int lessonID = cursor.getInt(cursor.getColumnIndexOrThrow("LessonID"));
                        String lessonName = cursor.getString(cursor.getColumnIndexOrThrow("LessonName"));
                        addLessonCard(lessonID, lessonName);
                    } while (cursor.moveToNext());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "No lessons found for this topic.", Toast.LENGTH_SHORT).show());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching lessons: ", e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }


    private void addLessonCard(int lessonID, String lessonName) {
        runOnUiThread(() -> {
            if (isFinishing() || isDestroyed()) return;

            LinearLayout rowLayout = createNewRow(); // Always create a new row for each card

            // Define colors
            int[] colors = {
                    Color.parseColor("#F44336"), // Red
                    Color.parseColor("#FF9800"), // Orange
                    Color.parseColor("#FFEB3B"), // Yellow
                    Color.parseColor("#4CAF50"), // Green
                    Color.parseColor("#2196F3"), // Blue
                    Color.parseColor("#9C27B0"), // Purple
                    Color.parseColor("#00BCD4")  // Cyan
            };

            int colorIndex = topicID % colors.length;
            int cardColor = colors[colorIndex];

            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // Match parent width
                    200  // Set desired card height (e.g., 200 pixels)
            );
            cardParams.setMargins(16, 16, 16, 16);
            cardView.setLayoutParams(cardParams);
            cardView.setRadius(20);
            cardView.setCardElevation(8);
            cardView.setCardBackgroundColor(cardColor);

            LinearLayout innerLayout = new LinearLayout(this);
            innerLayout.setOrientation(LinearLayout.VERTICAL);
            innerLayout.setGravity(Gravity.CENTER);
            innerLayout.setPadding(16, 16, 16, 16);

            TextView lessonText = new TextView(this);
            lessonText.setText(lessonName);
            lessonText.setTextSize(28);
            lessonText.setTypeface(null, Typeface.BOLD);
            lessonText.setGravity(Gravity.CENTER);
            lessonText.setTextColor(Color.BLACK);

            innerLayout.addView(lessonText);
            cardView.addView(innerLayout);

            cardView.setOnClickListener(v -> openLessonDetails(lessonID, lessonName));
            rowLayout.addView(cardView);
        });
    }




    private LinearLayout createNewRow() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        contentArea.addView(row);
        return row;
    }

    private void openLessonDetails(int lessonID, String lessonName) {
        Intent intent = new Intent(this, ExerciseCreate.class); // Replace with your actual activity class
        intent.putExtra("LessonID", lessonID);
        intent.putExtra("LessonName", lessonName); // Optional, if you want to pass the lesson name as well
        startActivity(intent);
    }
}
