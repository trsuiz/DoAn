package com.example.doan;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lesson_database.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL("CREATE TABLE Topics (topic_id INTEGER PRIMARY KEY AUTOINCREMENT, topic_name TEXT NOT NULL);");
        db.execSQL("CREATE TABLE Lessons (lesson_id INTEGER PRIMARY KEY AUTOINCREMENT, lesson_name TEXT NOT NULL, topic_id INTEGER, FOREIGN KEY (topic_id) REFERENCES Topics(topic_id));");
        db.execSQL("CREATE TABLE Words (word_id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT NOT NULL, lesson_id INTEGER, FOREIGN KEY (lesson_id) REFERENCES Lessons(lesson_id));");
        db.execSQL("CREATE TABLE Exercises (exercise_id INTEGER PRIMARY KEY AUTOINCREMENT, exercise_text TEXT NOT NULL, lesson_id INTEGER, FOREIGN KEY (lesson_id) REFERENCES Lessons(lesson_id));");
        db.execSQL("CREATE TABLE Answers (answer_id INTEGER PRIMARY KEY AUTOINCREMENT, answer_text TEXT NOT NULL, exercise_id INTEGER, is_correct INTEGER, FOREIGN KEY (exercise_id) REFERENCES Exercises(exercise_id));"); //1 correct 0 incorrect
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Exercises");
        db.execSQL("DROP TABLE IF EXISTS Words");
        db.execSQL("DROP TABLE IF EXISTS Lessons");
        db.execSQL("DROP TABLE IF EXISTS Topics");
        onCreate(db);
    }

    // Method to drop tables (called manually before inserting data)
    public void dropTables(SQLiteDatabase db) {
        // Drop all tables to reset the database
        db.execSQL("DROP TABLE IF EXISTS Exercises");
        db.execSQL("DROP TABLE IF EXISTS Words");
        db.execSQL("DROP TABLE IF EXISTS Lessons");
        db.execSQL("DROP TABLE IF EXISTS Topics");
        db.execSQL("DROP TABLE IF EXISTS Answers");

        // Recreate the tables manually
        db.execSQL("CREATE TABLE Topics (topic_id INTEGER PRIMARY KEY AUTOINCREMENT, topic_name TEXT NOT NULL);");
        db.execSQL("CREATE TABLE Lessons (lesson_id INTEGER PRIMARY KEY AUTOINCREMENT, lesson_name TEXT NOT NULL, topic_id INTEGER, FOREIGN KEY (topic_id) REFERENCES Topics(topic_id));");
        db.execSQL("CREATE TABLE Words (word_id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT NOT NULL, lesson_id INTEGER, FOREIGN KEY (lesson_id) REFERENCES Lessons(lesson_id));");
        db.execSQL("CREATE TABLE Exercises (exercise_id INTEGER PRIMARY KEY AUTOINCREMENT, exercise_text TEXT NOT NULL, lesson_id INTEGER, FOREIGN KEY (lesson_id) REFERENCES Lessons(lesson_id));");
        db.execSQL("CREATE TABLE Answers (answer_id INTEGER PRIMARY KEY AUTOINCREMENT, answer_text TEXT NOT NULL, exercise_id INTEGER, is_correct INTEGER, FOREIGN KEY (exercise_id) REFERENCES Exercises(exercise_id));");
    }

    public void insertTopic(String topicName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("topic_name", topicName);
        db.insert("Topics", null, values);
    }

    public void insertLesson(String lessonName, int topicId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lesson_name", lessonName);
        values.put("topic_id", topicId);
        db.insert("Lessons", null, values);
    }

    public void insertWord(String word, int lessonId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("lesson_id", lessonId);
        db.insert("Words", null, values);
    }

    public void insertExerciseWithAnswers(String exerciseText, int lessonId, String[] answers, int correctAnswerIndex) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Step 1: Insert the exercise into the Exercises table
        ContentValues exerciseValues = new ContentValues();
        exerciseValues.put("exercise_text", exerciseText);
        exerciseValues.put("lesson_id", lessonId);
        long exerciseId = db.insert("Exercises", null, exerciseValues);  // Insert and get the exercise_id

        // Step 2: Insert the answers into the Answers table
        for (int i = 0; i < answers.length; i++) {
            ContentValues answerValues = new ContentValues();
            answerValues.put("answer_text", answers[i]);
            answerValues.put("exercise_id", exerciseId);
            answerValues.put("is_correct", (i == correctAnswerIndex) ? 1 : 0);  // Mark the correct answer

            // Insert each answer
            db.insert("Answers", null, answerValues);
        }
    }



    public int getTopicId(String topicName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT topic_id FROM Topics WHERE topic_name = ?", new String[]{topicName});

        // Check if the cursor has data
        if (cursor != null && cursor.moveToFirst()) {
            // Get the column index for "topic_id"
            int columnIndex = cursor.getColumnIndex("topic_id");

            // Check if the column index is valid
            if (columnIndex >= 0) {
                return cursor.getInt(columnIndex);  // Return the topic_id
            }
        }

        cursor.close();  // Make sure to close the cursor
        return -1;  // Return -1 if the topic is not found
    }

    public int getLessonId(String lessonName, int topicId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT lesson_id FROM Lessons WHERE lesson_name = ? AND topic_id = ?",
                new String[]{lessonName, String.valueOf(topicId)});

        // Check if the cursor has data
        if (cursor != null && cursor.moveToFirst()) {
            // Get the column index for "lesson_id"
            int columnIndex = cursor.getColumnIndex("lesson_id");

            // Check if the column index is valid
            if (columnIndex >= 0) {
                return cursor.getInt(columnIndex);  // Return the lesson_id
            }
        }

        cursor.close();  // Make sure to close the cursor
        return -1;  // Return -1 if the lesson is not found
    }


    public Cursor getAllTopics() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Topics", null);
    }

    public Cursor getLessonsForTopic(int topicId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Lessons WHERE topic_id = ?", new String[]{String.valueOf(topicId)});
    }

    public void displayAllTablesAndData() {
        SQLiteDatabase db = this.getReadableDatabase();

        // List of tables in the database
        String[] tables = {"Topics", "Lessons", "Words", "Exercises", "Answers"};

        // Iterate through each table and display its data
        for (String table : tables) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);

            // Check if the table has any rows
            if (cursor != null && cursor.moveToFirst()) {
                // Get the column names dynamically
                String[] columnNames = cursor.getColumnNames();
                StringBuilder logMessage = new StringBuilder();

                logMessage.append("Table: ").append(table).append("\n");

                // Iterate through each row in the table
                do {
                    // Iterate through each column in the row
                    for (String columnName : columnNames) {
                        int columnIndex = cursor.getColumnIndex(columnName);
                        if (columnIndex != -1) {
                            String columnValue = cursor.getString(columnIndex);
                            logMessage.append(columnName).append(": ").append(columnValue).append(" | ");
                        }
                    }
                    logMessage.append("\n");  // Add a new line after each row
                } while (cursor.moveToNext());

                // Log the table data
                Log.d("Database", logMessage.toString());
            } else {
                Log.d("Database", "No data found in " + table);
            }

            // Close the cursor to prevent memory leaks
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    // Insert sample data method
    public void insertLessonData() {
        // Insert topic
        insertTopic("Food");

        // Get topicId for the "Math" topic
        int topicId_Food = getTopicId("Food");

        // Insert lessons for that topic
        insertLesson("Food: Lesson 1", topicId_Food);
        insertLesson("Food: Lesson 2", topicId_Food);
        insertLesson("Food: Lesson 3", topicId_Food);
        insertLesson("Food: Lesson 4", topicId_Food);

        // Get lessonId for "Lesson 1: Addition"
        int lessonId1_Food = getLessonId("Food: Lesson 1", topicId_Food);

        // Insert words for "Lesson 1: Addition"
        insertWord("apple", lessonId1_Food);
        insertWord("bread", lessonId1_Food);
        insertWord("rice", lessonId1_Food);
        insertWord("chicken", lessonId1_Food);
        insertWord("pizza", lessonId1_Food);

        // Insert exercises for "Lesson 1: Addition"
        insertExerciseWithAnswers("I eat an ___ every morning.", lessonId1_Food, new String[]{"apple","chicken","soup","bread"},0);
        insertExerciseWithAnswers("She loves making ___ pies.", lessonId1_Food, new String[]{"apple","chicken","soup","bread"},0);
        insertExerciseWithAnswers("I want a slice of ___ with butter.", lessonId1_Food, new String[]{"apple","chicken","soup","bread"}, 3);
        insertExerciseWithAnswers("He eats ___ with jam for breakfast.", lessonId1_Food, new String[]{"apple","chicken","soup","bread"}, 3);
        insertExerciseWithAnswers("We cook ___ with vegetables for dinner.", lessonId1_Food, new String[]{"apple","chicken","soup","rice"}, 3);
        insertExerciseWithAnswers("___ is a common food in many countries.", lessonId1_Food, new String[]{"apple","chicken","soup","rice"}, 3);
        insertExerciseWithAnswers("My mom is cooking ___ for dinner tonight.", lessonId1_Food, new String[]{"apple","chicken","soup","rice"}, 1);
        insertExerciseWithAnswers("I like to eat ___ with rice.", lessonId1_Food, new String[]{"apple","chicken","soup","rice"}, 1);
        insertExerciseWithAnswers("I want to order a large ___ for the party.", lessonId1_Food, new String[]{"pizza","banana","soup","rice"}, 0);
        insertExerciseWithAnswers("___ is my favorite food", lessonId1_Food, new String[]{"pizza","banana","soup","rice"}, 0);

        //Xuat ra man hinh cac bang database
        displayAllTablesAndData();
    }

    // Get exercises for a given lesson ID
    public Cursor getExercisesForLesson(int lessonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Exercises WHERE lesson_id = ?", new String[]{String.valueOf(lessonId)});
    }

    // Get answers for a given exercise ID
    public Cursor getAnswersForExercise(long exerciseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Answers WHERE exercise_id = ?", new String[]{String.valueOf(exerciseId)});
    }

    public Cursor getExerciseForLessonAndId(int lessonId, int exerciseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Exercises WHERE lesson_id = ? AND exercise_id = ?",
                new String[]{String.valueOf(lessonId), String.valueOf(exerciseId)});
    }
}
