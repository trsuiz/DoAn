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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

public class TopicCreate extends AppCompatActivity {

    private static final String TAG = "TopicCreate";
    private LinearLayout contentArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_topic_create);

        // Hien thi cac topic duoc render tu database
        contentArea = findViewById(R.id.content_area);
        setupEdgeToEdge();
        displayTopics();

        // Nut quay ve activity truoc
        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // menu de them, xoa, sua topic
        FloatingActionButton fab = findViewById(R.id.fab_add_topic);
        fab.setOnClickListener(v -> showPopupMenu(v));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contentArea.removeAllViews(); // Clear views when activity is destroyed
    }

    private void setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.topic_create), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    ////////////////////////////////
    // Hien thi menu them, xoa, sua
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.topic_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            switch (title) {
                case "Thêm chủ đề":
                    openAddTopicDialog();
                    return true;
                case "Xóa chủ đề":
                    openDeleteTopicDialog();
                    return true;
                case "Sửa chủ đề":
                    openEditTopicDialog();
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }




    //////////////////////////////////////////
    //Them chu de
    private void openAddTopicDialog() {
        // Create AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm chủ đề");

        // Inflate custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_topic, null);
        builder.setView(dialogView);

        EditText editTopicName = dialogView.findViewById(R.id.edit_topic_name);
        EditText editDescription = dialogView.findViewById(R.id.edit_description);
        Button btnAdd = dialogView.findViewById(R.id.btn_add_topic);

        AlertDialog dialog = builder.create();

        btnAdd.setOnClickListener(v -> {
            String topicName = editTopicName.getText().toString().trim();
            String description = editDescription.getText().toString().trim();

            if (!topicName.isEmpty()) {
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                dbHelper.insertTopic(topicName, description);
                Toast.makeText(this, "Chủ đề đã được thêm!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                displayTopics();  // Refresh the topics list
            } else {
                editTopicName.setError("Tên chủ đề không được để trống");
            }
        });

        dialog.show();
    }







    //////////////////////////////////////////
    //Xoa chu de
    private void openDeleteTopicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xoá chủ đề");

        // Create layout container
        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(32, 32, 32, 32);

        // Fetch topics from database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Integer> topicIDs = new ArrayList<>();
        List<String> topicNames = new ArrayList<>();
        List<CheckBox> checkBoxes = new ArrayList<>();

        try (Cursor cursor = db.rawQuery("SELECT TopicID, TopicName FROM Topics", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int topicID = cursor.getInt(cursor.getColumnIndexOrThrow("TopicID"));
                    String topicName = cursor.getString(cursor.getColumnIndexOrThrow("TopicName"));
                    topicIDs.add(topicID);
                    topicNames.add(topicName);

                    // Layout for each topic row
                    LinearLayout topicRow = new LinearLayout(this);
                    topicRow.setOrientation(LinearLayout.HORIZONTAL);
                    topicRow.setPadding(0, 8, 0, 8);

                    TextView topicText = new TextView(this);
                    topicText.setText(topicName);
                    topicText.setLayoutParams(new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

                    CheckBox checkBox = new CheckBox(this);
                    checkBoxes.add(checkBox);

                    topicRow.addView(topicText);
                    topicRow.addView(checkBox);
                    dialogLayout.addView(topicRow);

                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "Không có chủ đề nào để xóa.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching topics: ", e);
        } finally {
            db.close();
        }

        // Delete Button
        Button deleteButton = new Button(this);
        deleteButton.setText("Xoá");
        deleteButton.setOnClickListener(v -> {
            // Collect selected topics
            List<Integer> topicsToDelete = new ArrayList<>();
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isChecked()) {
                    topicsToDelete.add(topicIDs.get(i));
                }
            }

            if (topicsToDelete.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một chủ đề.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Show confirmation dialog
            showDeleteConfirmationDialog(topicsToDelete);
        });

        dialogLayout.addView(deleteButton);
        builder.setView(dialogLayout);
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteConfirmationDialog(List<Integer> topicIDs) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setTitle("Bạn có muốn xoá?");

        LinearLayout confirmLayout = new LinearLayout(this);
        confirmLayout.setOrientation(LinearLayout.VERTICAL);
        confirmLayout.setPadding(32, 32, 32, 32);

        // Checkbox row
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

        // Buttons layout
        LinearLayout buttonsLayout = new LinearLayout(this);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setGravity(Gravity.END);
        buttonsLayout.setPadding(0, 16, 0, 0);

        Button cancelButton = new Button(this);
        cancelButton.setText("Cancel");
        cancelButton.setOnClickListener(v -> confirmBuilder.create().dismiss());

        Button confirmDeleteButton = new Button(this);
        confirmDeleteButton.setText("Delete");
        confirmDeleteButton.setOnClickListener(v -> {
            boolean allChecked = confirmBoxes.stream().allMatch(CheckBox::isChecked);
            if (allChecked) {
                deleteSelectedTopics(topicIDs);
                confirmBuilder.create().dismiss();
            } else {
                Toast.makeText(this, "Vui lòng chọn tất cả các ô để xác nhận.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonsLayout.addView(cancelButton);
        buttonsLayout.addView(confirmDeleteButton);

        confirmLayout.addView(buttonsLayout);
        confirmBuilder.setView(confirmLayout);
        confirmBuilder.show();

    }

    private void deleteSelectedTopics(List<Integer> topicIDs) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            for (int topicID : topicIDs) {
                int rowsDeleted = db.delete("Topics", "TopicID = ?", new String[]{String.valueOf(topicID)});
                Log.d(TAG, "Deleting TopicID " + topicID + ", rowsDeleted = " + rowsDeleted);
            }
            db.setTransactionSuccessful();
            Toast.makeText(this, "Chủ đề đã được xóa thành công.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error deleting topics: ", e);
            Toast.makeText(this, "Lỗi khi xóa chủ đề.", Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
            db.close();
            displayTopics(); // Refresh UI
        }
    }






    //////////////////////////////////////////
    //Sua chu de
    private void openEditTopicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa chủ đề");

        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(32, 32, 32, 32);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Integer> topicIDs = new ArrayList<>();
        List<CheckBox> checkBoxes = new ArrayList<>();
        EditText editTopicName = new EditText(this);
        EditText editDescription = new EditText(this);
        editTopicName.setHint("Tên chủ đề");
        editDescription.setHint("Mô tả");

        try (Cursor cursor = db.rawQuery("SELECT TopicID, TopicName, Description FROM Topics", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int topicID = cursor.getInt(cursor.getColumnIndexOrThrow("TopicID"));
                    String topicName = cursor.getString(cursor.getColumnIndexOrThrow("TopicName"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
                    topicIDs.add(topicID);

                    CheckBox checkBox = new CheckBox(this);
                    checkBox.setText(topicName);
                    checkBoxes.add(checkBox);
                    dialogLayout.addView(checkBox);

                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            for (CheckBox cb : checkBoxes) {
                                if (cb != checkBox) cb.setChecked(false);
                            }
                            editTopicName.setText(topicName);
                            editDescription.setText(description);
                        } else {
                            editTopicName.setText("");
                            editDescription.setText("");
                        }
                    });

                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "Không có chủ đề để sửa.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching topics for edit: ", e);
        } finally {
            db.close();
        }

        dialogLayout.addView(editTopicName);
        dialogLayout.addView(editDescription);

        Button btnEdit = new Button(this);
        btnEdit.setText("Sửa chủ đề");
        btnEdit.setOnClickListener(v -> {
            int selectedIndex = -1;
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isChecked()) {
                    selectedIndex = i;
                    break;
                }
            }

            if (selectedIndex != -1) {
                String newName = editTopicName.getText().toString().trim();
                String newDesc = editDescription.getText().toString().trim();

                if (!newName.isEmpty()) {
                    updateTopic(topicIDs.get(selectedIndex), newName, newDesc);
                    Toast.makeText(this, "Chủ đề đã được sửa!", Toast.LENGTH_SHORT).show();
                    displayTopics();
                } else {
                    editTopicName.setError("Tên chủ đề không được để trống.");
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn một chủ đề.", Toast.LENGTH_SHORT).show();
            }
        });

        dialogLayout.addView(btnEdit);
        builder.setView(dialogLayout);
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateTopic(int topicID, String newName, String newDescription) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("TopicName", newName);
        values.put("Description", newDescription);

        db.update("Topics", values, "TopicID = ?", new String[]{String.valueOf(topicID)});
        db.close();
    }




    //////////////////////////////////////////////////////
    //Lay tu database va hien thi topic duoi nhung o vuong
    private void displayTopics() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        contentArea.removeAllViews(); // Clear any existing views

        try (Cursor cursor = db.rawQuery("SELECT * FROM Topics", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int topicID = cursor.getInt(cursor.getColumnIndexOrThrow("TopicID"));
                    String topicName = cursor.getString(cursor.getColumnIndexOrThrow("TopicName"));
                    addTopicCard(topicID, topicName);
                } while (cursor.moveToNext());
            } else {
                Log.e(TAG, "No topics found in database.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Database query failed: ", e);
        } finally {
            db.close();
        }
    }

    private void addTopicCard(int topicID, String topicName) {
        runOnUiThread(() -> {
            if (!isFinishing() && !isDestroyed()) {
                LinearLayout contentArea = findViewById(R.id.content_area);

                LinearLayout rowLayout;

                // Check if the last row has less than 2 cards; if so, use it
                int childCount = contentArea.getChildCount();
                if (childCount > 0) {
                    View lastChild = contentArea.getChildAt(childCount - 1);
                    if (lastChild instanceof LinearLayout) {
                        rowLayout = (LinearLayout) lastChild;
                        if (rowLayout.getChildCount() >= 2) {
                            // Last row is full, create a new row
                            rowLayout = createNewRow();
                            contentArea.addView(rowLayout);
                        }
                    } else {
                        rowLayout = createNewRow();
                        contentArea.addView(rowLayout);
                    }
                } else {
                    rowLayout = createNewRow();
                    contentArea.addView(rowLayout);
                }

                // Define 7 bright primary colors
                int[] colors = {
                        Color.parseColor("#F44336"), // Red
                        Color.parseColor("#FF9800"), // Orange
                        Color.parseColor("#FFEB3B"), // Yellow
                        Color.parseColor("#4CAF50"), // Green
                        Color.parseColor("#2196F3"), // Blue
                        Color.parseColor("#9C27B0"), // Purple
                        Color.parseColor("#00BCD4")  // Cyan
                };

                // Determine the color based on the topicID to ensure variety
                int colorIndex = topicID % colors.length;
                int cardColor = colors[colorIndex];

                // Create CardView
                CardView cardView = new CardView(this);
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1 // Equal weight for two cards per row
                );
                cardParams.setMargins(8, 8, 8, 8);
                cardView.setLayoutParams(cardParams);
                cardView.setCardElevation(8);
                cardView.setRadius(20);
                cardView.setUseCompatPadding(true);
                cardView.setCardBackgroundColor(cardColor);

                cardView.setClickable(true);
                cardView.setFocusable(true);

                // Inner layout for vertical orientation
                LinearLayout innerLayout = new LinearLayout(this);
                innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                innerLayout.setOrientation(LinearLayout.VERTICAL);
                innerLayout.setGravity(Gravity.CENTER);
                innerLayout.setPadding(16, 16, 16, 16);
                innerLayout.setMinimumHeight(0);

                // Icon (occupies one row)
                ImageView icon = new ImageView(this);
                LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(200, 200);
                iconParams.gravity = Gravity.CENTER;
                icon.setLayoutParams(iconParams);
                icon.setImageResource(R.drawable.ic_lesson);
                icon.setColorFilter(Color.parseColor("#000000"));

                // Topic name (next row)
                TextView textView = new TextView(this);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                textParams.gravity = Gravity.CENTER;
                textParams.setMargins(0, 16, 0, 0);
                textView.setLayoutParams(textParams);
                textView.setText(topicName);
                textView.setTextSize(32);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setGravity(Gravity.CENTER);

                // Assemble views
                innerLayout.addView(icon);  // Icon on top row
                innerLayout.addView(textView);  // Text on bottom row
                cardView.addView(innerLayout);

                // Set click listener to open lesson creation
                cardView.setOnClickListener(v -> openLessonCreate(topicID, topicName));

                // Add card to the row
                rowLayout.addView(cardView);



            }
        });
    }

    private LinearLayout createNewRow() {
        LinearLayout rowLayout = new LinearLayout(this);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowLayout.setLayoutParams(rowParams);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setGravity(Gravity.CENTER);
        return rowLayout;
    }

    private void openLessonCreate(int topicID, String topicName) {
        Intent intent = new Intent(TopicCreate.this, LessonCreate.class);
        intent.putExtra("TopicID", topicID);
        intent.putExtra("TopicName", topicName);
        startActivity(intent);
    }
}
