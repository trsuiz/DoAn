package com.example.doan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.doan.ExerciseRender.Exercise;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LearningApp.db";
    private static final int DATABASE_VERSION = 2;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (" +
                "UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Username TEXT NOT NULL UNIQUE, " +
                "PasswordHash TEXT NOT NULL, " +
                "Email TEXT NOT NULL UNIQUE, " +
                "FullName TEXT, " +
                "Role TEXT NOT NULL, " +
                "CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE Topics (" +
                "TopicID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TopicName TEXT NOT NULL, " +
                "Description TEXT, " +
                "CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE Lessons (" +
                "LessonID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TopicID INTEGER NOT NULL, " +
                "LessonName TEXT NOT NULL, " +
                "Content TEXT NOT NULL, " +
                "CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (TopicID) REFERENCES Topics(TopicID) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE Exercises (" +
                "ExerciseID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "LessonID INTEGER NOT NULL, " +
                "Question TEXT NOT NULL, " +
                "OptionA TEXT, " +
                "OptionB TEXT, " +
                "OptionC TEXT, " +
                "OptionD TEXT, " +
                "CorrectAnswer TEXT NOT NULL, " +
                "CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (LessonID) REFERENCES Lessons(LessonID) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE UserProgress (" +
                "ProgressID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER NOT NULL, " +
                "LessonID INTEGER NOT NULL, " +
                "ExerciseID INTEGER, " +
                "CompletedAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE, " +
                "FOREIGN KEY (LessonID) REFERENCES Lessons(LessonID) ON DELETE CASCADE, " +
                "FOREIGN KEY (ExerciseID) REFERENCES Exercises(ExerciseID) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE Scores (" +
                "ScoreID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER NOT NULL, " +
                "ExerciseID INTEGER NOT NULL, " +
                "Score INTEGER NOT NULL, " +
                "CompletedAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE, " +
                "FOREIGN KEY (ExerciseID) REFERENCES Exercises(ExerciseID) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE LearningSessions (" +
                "SessionID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER NOT NULL, " +
                "LessonID INTEGER, " +
                "ExerciseID INTEGER, " +
                "SessionDate DATE NOT NULL, " +
                "FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE, " +
                "FOREIGN KEY (LessonID) REFERENCES Lessons(LessonID) ON DELETE CASCADE, " +
                "FOREIGN KEY (ExerciseID) REFERENCES Exercises(ExerciseID) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE Streaks (" +
                "StreakID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER NOT NULL, " +
                "StartDate DATE NOT NULL, " +
                "EndDate DATE, " +
                "CurrentStreak INTEGER DEFAULT 0, " +
                "LongestStreak INTEGER DEFAULT 0, " +
                "FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE Roles (" +
                "RoleID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "RoleName TEXT NOT NULL UNIQUE)");

        db.execSQL("CREATE TABLE UserRoles (" +
                "UserRoleID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UserID INTEGER NOT NULL, " +
                "RoleID INTEGER NOT NULL, " +
                "FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE, " +
                "FOREIGN KEY (RoleID) REFERENCES Roles(RoleID) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS UserRoles");
        db.execSQL("DROP TABLE IF EXISTS Roles");
        db.execSQL("DROP TABLE IF EXISTS Streaks");
        db.execSQL("DROP TABLE IF EXISTS LearningSessions");
        db.execSQL("DROP TABLE IF EXISTS Scores");
        db.execSQL("DROP TABLE IF EXISTS UserProgress");
        db.execSQL("DROP TABLE IF EXISTS Exercises");
        db.execSQL("DROP TABLE IF EXISTS Lessons");
        db.execSQL("DROP TABLE IF EXISTS Topics");
        db.execSQL("DROP TABLE IF EXISTS Users");

        onCreate(db);
    }


    public void insertUser(String username, String passwordHash, String email, String fullName, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Username", username);
        values.put("PasswordHash", passwordHash);
        values.put("Email", email);
        values.put("FullName", fullName);
        values.put("Role", role);

        db.insert("Users", null, values);
    }


    public void insertTopic(String topicName, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TopicName", topicName);
        values.put("Description", description);

        db.insert("Topics", null, values);
    }
    public void insertStreak(int userID, String startDate, String endDate, int currentStreak, int longestStreak) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserID", userID);
        values.put("StartDate", startDate);
        values.put("EndDate", endDate);
        values.put("CurrentStreak", currentStreak);
        values.put("LongestStreak", longestStreak);

        db.insert("Streaks", null, values);
    }
    public void updateStreak(int userID, int newStreak, int longestStreak, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CurrentStreak", newStreak);
        values.put("LongestStreak", longestStreak);
        values.put("EndDate", endDate);

        db.update("Streaks", values, "UserID = ?", new String[]{String.valueOf(userID)});
    }

    public void insertLesson(int topicID, String lessonName, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TopicID", topicID);
        values.put("LessonName", lessonName);
        values.put("Content", content);

        db.insert("Lessons", null, values);
    }

    public void insertExercise(int lessonID, String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("LessonID", lessonID);
        values.put("Question", question);
        values.put("OptionA", optionA);
        values.put("OptionB", optionB);
        values.put("OptionC", optionC);
        values.put("OptionD", optionD);
        values.put("CorrectAnswer", correctAnswer);

        db.insert("Exercises", null, values);
    }

    public void insertUserProgress(int userID, int lessonID, Integer exerciseID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserID", userID);
        values.put("LessonID", lessonID);
        if (exerciseID != null) values.put("ExerciseID", exerciseID);

        db.insert("UserProgress", null, values);
    }

    public void insertScore(int userID, int exerciseID, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserID", userID);
        values.put("ExerciseID", exerciseID);
        values.put("Score", score);

        db.insert("Scores", null, values);
    }

    public void insertLearningSession(int userID, Integer lessonID, Integer exerciseID, String sessionDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserID", userID);
        values.put("SessionDate", sessionDate);
        if (lessonID != null) values.put("LessonID", lessonID);
        if (exerciseID != null) values.put("ExerciseID", exerciseID);

        db.insert("LearningSessions", null, values);
    }



    public void insertRole(String roleName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("RoleName", roleName);

        db.insert("Roles", null, values);
    }

    public void insertUserRole(int userID, int roleID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserID", userID);
        values.put("RoleID", roleID);

        db.insert("UserRoles", null, values);
    }

    public void logAllDatabaseData() {
        SQLiteDatabase db = this.getReadableDatabase();

        logTableData(db, "Users");
        logTableData(db, "Topics");
        logTableData(db, "Lessons");
        logTableData(db, "Exercises");
        logTableData(db, "UserProgress");
        logTableData(db, "Scores");
        logTableData(db, "LearningSessions");
        logTableData(db, "Streaks");
        logTableData(db, "Roles");
        logTableData(db, "UserRoles");

        db.close();
    }

    private void logTableData(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);

        Log.d("DatabaseLog", "----- Data from table: " + tableName + " -----");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    StringBuilder rowData = new StringBuilder();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        String columnName = cursor.getColumnName(i);
                        String columnValue = cursor.getString(i);
                        rowData.append(columnName).append(": ").append(columnValue).append(" | ");
                    }
                    Log.d("DatabaseLog", rowData.toString());
                } while (cursor.moveToNext());
            } else {
                Log.d("DatabaseLog", "No data in " + tableName);
            }
            cursor.close();
        } else {
            Log.e("DatabaseLog", "Failed to query table " + tableName);
        }
    }

    public void clearAllData() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            // Delete all data from tables
            db.delete("UserRoles", null, null);
            db.delete("Roles", null, null);
            db.delete("Streaks", null, null);
            db.delete("LearningSessions", null, null);
            db.delete("Scores", null, null);
            db.delete("UserProgress", null, null);
            db.delete("Exercises", null, null);
            db.delete("Lessons", null, null);
            db.delete("Topics", null, null);
            db.delete("Users", null, null);

            // Reset AUTOINCREMENT counters for all tables
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='UserRoles';");
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='Roles';");
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='Streaks';");
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='LearningSessions';");
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='Scores';");
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='UserProgress';");
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='Exercises';");
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='Lessons';");
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='Topics';");
            db.execSQL("DELETE FROM sqlite_sequence WHERE name='Users';");

            db.setTransactionSuccessful();
            Log.d("DatabaseClear", "All data cleared and AUTOINCREMENT counters reset.");
        } catch (Exception e) {
            Log.e("DatabaseClear", "Error clearing database: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public Exercise getExercise(int exerciseID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Exercise exercise = null;

        Cursor cursor = db.query("Exercises", null, "ExerciseID = ?",
                new String[]{String.valueOf(exerciseID)},
                null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exercise = new Exercise(
                        cursor.getInt(cursor.getColumnIndex("ExerciseID")),
                        cursor.getString(cursor.getColumnIndex("Question")),
                        cursor.getString(cursor.getColumnIndex("OptionA")),
                        cursor.getString(cursor.getColumnIndex("OptionB")),
                        cursor.getString(cursor.getColumnIndex("OptionC")),
                        cursor.getString(cursor.getColumnIndex("OptionD")),
                        cursor.getString(cursor.getColumnIndex("CorrectAnswer"))
                );
            }
            cursor.close();
        }
        db.close();
        return exercise;
    }

    public int getLessonID(String topicName, String lessonName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Lessons.LessonID FROM Lessons " +
                "JOIN Topics ON Lessons.TopicID = Topics.TopicID " +
                "WHERE Topics.TopicName = ? AND Lessons.LessonName = ?";

        Cursor cursor = db.rawQuery(query, new String[]{topicName, lessonName});
        if (cursor != null && cursor.moveToFirst()) {
            int lessonID = cursor.getInt(cursor.getColumnIndexOrThrow("LessonID"));
            cursor.close();
            return lessonID;
        } else {
            if (cursor != null) cursor.close();
            return -1;  // Invalid lesson ID
        }
    }

    public List<Exercise> getExercisesForLesson(int lessonID) {
        List<Exercise> exerciseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Exercises WHERE LessonID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(lessonID)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ExerciseID"));
                String question = cursor.getString(cursor.getColumnIndexOrThrow("Question"));
                String optionA = cursor.getString(cursor.getColumnIndexOrThrow("OptionA"));
                String optionB = cursor.getString(cursor.getColumnIndexOrThrow("OptionB"));
                String optionC = cursor.getString(cursor.getColumnIndexOrThrow("OptionC"));
                String optionD = cursor.getString(cursor.getColumnIndexOrThrow("OptionD"));
                String correctAnswer = cursor.getString(cursor.getColumnIndexOrThrow("CorrectAnswer"));

                exerciseList.add(new Exercise(id, question, optionA, optionB, optionC, optionD, correctAnswer));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return exerciseList;
    }




    public void insertSampleData() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert sample Users
       // insertUser("admin", "admin", "admin@gmail.com", "Helen", "admin");
        //insertUser("player", "player", "player@gmail.com", "Jane", "player");

        // Insert sample Topics
        insertTopic("About yourself", "Learn about you.");
        insertTopic("Daily routines", "Learn about things you do everyday.");
        insertTopic("Food", "Learn all food in general.");

        // Insert sample Lessons
        //Food
        insertLesson(1, "About yourself: Lesson1", "About_yourself_1");
        insertLesson(1, "About yourself: Lesson2", "About_yourself_2");
        insertLesson(1, "About yourself: Lesson3", "About_yourself_3");
        insertLesson(1, "About yourself: Lesson4", "About_yourself_4");
        insertLesson(2, "Daily routines: Lesson1", "Daily_routines_1");
        insertLesson(2, "Daily routines: Lesson2", "Daily_routines_2");
        insertLesson(2, "Daily routines: Lesson3", "Daily_routines_3");
        insertLesson(2, "Daily routines: Lesson4", "Daily_routines_4");
        insertLesson(3, "Food: Lesson1", "Food_1");
        insertLesson(3, "Food: Lesson2", "Food_2");
        insertLesson(3, "Food: Lesson3", "Food_3");
        insertLesson(3, "Food: Lesson4", "Food_4");




        // Exercises 1-10 (Number 5)
        // About yourself
        insertExercise(1, "My ______ is Anna.", "name", "age", "hobby", "job", "C");
        insertExercise(1, "Can you ______ your name, please?", "tell", "say", "write", "read", "B");
        insertExercise(1, "I am ______ years old.", "twenty", "thirty", "forty", "fifty", "D");
        insertExercise(1, "My birthday is on ______ 5th.", "May", "June", "July", "August", "A");
        insertExercise(1, "I come from ______, a small town near the sea.", "Hanoi", "Danang", "Vung Tau", "Ha Long", "C");
        insertExercise(1, "My hometown is famous for its beautiful ______.", "beaches", "mountains", "food", "festivals", "B");
        insertExercise(1, "I am ______. I was born in Japan.", "Japanese", "Vietnamese", "Korean", "Chinese", "A");
        insertExercise(1, "My ______ is Vietnamese, but I live in Canada.", "nationality", "language", "culture", "name", "D");
        insertExercise(1, "I work as a ______ in a big company.", "developer", "teacher", "doctor", "driver", "C");
        insertExercise(1, "My father’s ______ is a doctor.", "job", "position", "role", "career", "B");

// Exercises 11-20 (Number 6)
        insertExercise(2, "My dream ______ is to become a doctor.", "job", "career", "position", "goal", "D");
        insertExercise(2, "What ______ do you want to do in the future?", "job", "goal", "skill", "hobby", "A");
        insertExercise(2, "He works for a big ______ in the city.", "company", "school", "hospital", "store", "C");
        insertExercise(2, "This ______ has over 500 employees.", "company", "team", "organization", "department", "A");
        insertExercise(2, "I am a ______ at Hanoi University.", "student", "teacher", "lecturer", "staff", "B");
        insertExercise(2, "The ______ is studying for his final exams.", "student", "teacher", "lecturer", "staff", "C");
        insertExercise(2, "My favorite ______ is reading books.", "hobby", "subject", "skill", "job", "D");
        insertExercise(2, "In my free time, I enjoy my ______, which is painting.", "hobby", "job", "skill", "work", "A");
        insertExercise(2, "She has a strong ______ in learning new languages.", "interest", "goal", "skill", "hobby", "B");
        insertExercise(2, "My ______ in music started when I was a child.", "interest", "hobby", "goal", "dream", "C");

// Exercises 21-30 (Number 7)
        insertExercise(3, "I have a small ______ with four members.", "family", "team", "group", "class", "A");
        insertExercise(3, "My ______ always supports me in everything I do.", "family", "team", "friends", "school", "D");
        insertExercise(3, "He has five years of working ______ in marketing.", "experience", "skill", "job", "career", "B");
        insertExercise(3, "Traveling gives us a lot of new ______.", "experiences", "skills", "friends", "memories", "C");
        insertExercise(3, "Communication ______ is very important in the workplace.", "skill", "job", "goal", "interest", "A");
        insertExercise(3, "She is learning a new ______: programming.", "skill", "language", "subject", "hobby", "B");
        insertExercise(3, "______ is the key to success in the future.", "Education", "Skill", "Experience", "Goal", "D");
        insertExercise(3, "He received his higher ______ from a famous university.", "education", "degree", "skill", "experience", "B");
        insertExercise(3, "She has a friendly and outgoing ______.", "personality", "skill", "job", "interest", "C");
        insertExercise(3, "His ______ makes him a great leader.", "personality", "skill", "goal", "experience", "A");

// Exercises 31-40 (Number 8)
        insertExercise(4, "She is very ______ and always helps others.", "friendly", "hard-working", "polite", "kind", "B");
        insertExercise(4, "A ______ smile can make someone’s day better.", "friendly", "happy", "bright", "sweet", "D");
        insertExercise(4, "He is a ______ student who studies day and night.", "hard-working", "smart", "intelligent", "diligent", "A");
        insertExercise(4, "To be successful, you need to be both smart and ______.", "hard-working", "lucky", "polite", "creative", "C");
        insertExercise(4, "My main ______ this year is to improve my English.", "goal", "dream", "skill", "job", "B");
        insertExercise(4, "Setting a clear ______ helps us stay focused.", "goal", "job", "skill", "plan", "D");
        insertExercise(4, "My biggest ______ is to become a doctor.", "dream", "goal", "skill", "job", "C");
        insertExercise(4, "Never stop chasing your ______, no matter how difficult it is.", "dream", "goal", "skill", "plan", "A");
        insertExercise(4, "Let me ______ myself before we start the meeting.", "introduce", "tell", "say", "write", "B");
        insertExercise(4, "Can you ______ your friend to me?", "introduce", "tell", "show", "mention", "C");




        insertExercise(5, "I ______ at 7 a.m.", "wake up", "get up", "brush my teeth", "wash my face", "A");
        insertExercise(5, "She ______ early.", "wakes up", "gets up", "brushes her teeth", "washes her face", "A");
        insertExercise(5, "I ______ every morning.", "wake up", "get up", "brush my teeth", "wash my face", "C");
        insertExercise(5, "She ______ before bed.", "wakes up", "gets up", "brushes her teeth", "washes her face", "C");
        insertExercise(5, "I ______ with water.", "wake up", "get up", "brush my teeth", "wash my face", "D");
        insertExercise(5, "He ______ after waking up.", "wakes up", "gets up", "brushes his teeth", "washes his face", "D");
        insertExercise(5, "I ______ in the morning.", "wake up", "get up", "take a shower", "wash my face", "C");
        insertExercise(5, "She ______ before going to work.", "wakes up", "gets up", "takes a shower", "washes her face", "C");
        insertExercise(5, "I ______ after showering.", "wake up", "get up", "get dressed", "wash my face", "C");
        insertExercise(5, "He ______ quickly.", "wakes up", "gets up", "gets dressed", "washes his face", "C");

        insertExercise(6, "I ______ at 7:30 a.m.", "have breakfast", "go to work", "take a nap", "cook dinner", "A");
        insertExercise(6, "She ______ with her family.", "has breakfast", "goes to work", "takes a nap", "cooks dinner", "A");
        insertExercise(6, "I ______ by bus.", "have breakfast", "go to work", "take a nap", "cook dinner", "B");
        insertExercise(6, "He ______ at 8 a.m.", "has breakfast", "goes to work", "takes a nap", "cooks dinner", "B");
        insertExercise(6, "I ______ every day.", "have lunch", "go to school", "take a nap", "cook dinner", "B");
        insertExercise(6, "She ______ by bike.", "has lunch", "goes to school", "takes a nap", "cooks dinner", "B");
        insertExercise(6, "I ______ at 12 p.m.", "have lunch", "go to work", "take a nap", "cook dinner", "A");
        insertExercise(6, "He ______ at home.", "has lunch", "goes to work", "takes a nap", "cooks dinner", "A");
        insertExercise(6, "I ______ after lunch.", "have a nap", "go to work", "take a nap", "cook dinner", "C");
        insertExercise(6, "She ______ for 20 minutes.", "takes a nap", "goes to work", "takes a nap", "cooks dinner", "C");

        insertExercise(7, "I ______ in the morning.", "exercise", "read", "watch TV", "cook dinner", "A");
        insertExercise(7, "He ______ at the gym.", "exercises", "reads", "watches TV", "cooks dinner", "A");
        insertExercise(7, "I ______ before bed.", "exercise", "read", "watch TV", "cook dinner", "B");
        insertExercise(7, "She ______ every day.", "exercises", "reads", "watches TV", "cooks dinner", "B");
        insertExercise(7, "I ______ in the evening.", "exercise", "read", "watch TV", "cook dinner", "C");
        insertExercise(7, "He ______ after dinner.", "exercises", "reads", "watches TV", "cooks dinner", "C");
        insertExercise(7, "I ______ with my mom.", "cook dinner", "read", "watch TV", "exercise", "D");
        insertExercise(7, "She ______ at 6 p.m.", "cooks dinner", "reads", "watches TV", "exercises", "D");
        insertExercise(7, "I ______ at 7 p.m.", "have dinner", "go to work", "take a nap", "cook dinner", "A");
        insertExercise(7, "He ______ with his family.", "has dinner", "goes to work", "takes a nap", "cooks dinner", "A");

        insertExercise(8, "I ______ on weekends.", "clean my room", "wash the kitchen", "read a book", "relax", "A");
        insertExercise(8, "She ______ every day.", "cleans the kitchen", "washes the dishes", "reads a book", "relaxes", "A");
        insertExercise(8, "I ______ at 10 p.m.", "go to bed", "have dinner", "take a nap", "cook dinner", "A");
        insertExercise(8, "He ______ early.", "goes to bed", "has dinner", "takes a nap", "cooks dinner", "A");
        insertExercise(8, "I ______ for 8 hours.", "sleep", "exercise", "read", "relax", "C");
        insertExercise(8, "She ______ well at night.", "sleeps", "exercises", "reads", "relaxes", "C");
        insertExercise(8, "I ______ by listening to music.", "relax", "read", "watch TV", "cook dinner", "D");
        insertExercise(8, "He ______ on the sofa.", "relaxes", "reads", "watches TV", "cooks dinner", "D");

        // Lesson 1
        insertExercise(9, "I eat an ______ every morning.", "apple", "bread", "rice", "chicken", "A");
        insertExercise(9, "She loves making ______ pies.", "bread", "apple", "chicken", "rice", "B");
        insertExercise(9, "I want a slice of ______ with butter.", "rice", "chicken", "apple", "bread", "D");
        insertExercise(9, "He eats ______ with jam for breakfast.", "chicken", "rice", "bread", "apple", "C");
        insertExercise(9, "We cook ______ with vegetables for dinner.", "apple", "bread", "rice", "chicken", "C");
        insertExercise(9, "______ is a common food in many countries.", "chicken", "rice", "bread", "apple", "B");
        insertExercise(9, "My mom is cooking ______ for dinner tonight.", "bread", "apple", "pizza", "chicken", "D");
        insertExercise(9, "I like to eat ______ with rice.", "chicken", "rice", "bread", "pizza", "A");
        insertExercise(9, "I want to order a large ______ for the party.", "bread", "pizza", "rice", "apple", "B");
        insertExercise(9, "______ is my favorite food.", "bread", "pizza", "rice", "apple", "B");

// Lesson 2
        insertExercise(10, "He drinks hot ______ when he’s sick.", "soup", "milk", "juice", "tea", "A");
        insertExercise(10, "She makes delicious ______ with chicken and vegetables.", "soup", "cake", "salad", "pasta", "A");
        insertExercise(10, "I have a ______ every day for a snack.", "banana", "apple", "carrot", "egg", "A");
        insertExercise(10, "______ is yellow and sweet.", "banana", "carrot", "apple", "orange", "A");
        insertExercise(10, "I like to eat ______ with toast in the morning.", "egg", "bread", "cheese", "milk", "A");
        insertExercise(10, "She cooked ______ for breakfast.", "egg", "bread", "cheese", "soup", "A");
        insertExercise(10, "I drink a glass of ______ with my cereal.", "milk", "juice", "water", "coffee", "A");
        insertExercise(10, "He likes his coffee with a little bit of ______.", "milk", "juice", "water", "cream", "A");
        insertExercise(10, "She prepares a fresh ______ for lunch.", "salad", "sandwich", "pizza", "soup", "A");
        insertExercise(10, "______ is a healthy choice for dinner.", "salad", "cheese", "chicken", "pasta", "A");

// Lesson 3
        insertExercise(11, "I put ______ on my sandwich.", "cheese", "butter", "jam", "mayonnaise", "A");
        insertExercise(11, "______ goes well with crackers.", "cheese", "tomato", "apple", "banana", "A");
        insertExercise(11, "I am eating grilled ______ for dinner.", "fish", "chicken", "beef", "pork", "A");
        insertExercise(11, "______ is good for your health.", "fish", "chicken", "cheese", "soup", "A");
        insertExercise(11, "I drink ______ every morning.", "juice", "milk", "water", "coffee", "A");
        insertExercise(11, "Orange ______ is my favorite.", "juice", "milk", "smoothie", "tea", "A");
        insertExercise(11, "I put ______ in my salad.", "carrot", "tomato", "cucumber", "onion", "A");
        insertExercise(11, "______ is orange and crunchy.", "carrot", "cucumber", "apple", "lettuce", "A");
        insertExercise(11, "He loves eating a big ______ for dinner.", "steak", "burger", "pizza", "fish", "A");
        insertExercise(11, "______ is made from beef.", "steak", "chicken", "pork", "tofu", "A");

// Lesson 4
        insertExercise(12, "She baked a delicious ______ for my birthday.", "cake", "pie", "cookies", "cupcakes", "A");
        insertExercise(12, "I eat a slice of ______ with coffee.", "cake", "bread", "toast", "muffin", "A");
        insertExercise(12, "We eat mashed ______ with our meal.", "potato", "carrot", "rice", "soup", "A");
        insertExercise(12, "______ is a very common vegetable.", "potato", "tomato", "onion", "cucumber", "A");
        insertExercise(12, "I put ______ in my sandwich.", "tomato", "lettuce", "cheese", "cucumber", "A");
        insertExercise(12, "______ is used to make ketchup.", "tomato", "potato", "carrot", "onion", "A");
        insertExercise(12, "I like to eat ______ after dinner.", "chocolate", "cake", "fruit", "cookies", "A");
        insertExercise(12, "______ is sweet and tasty.", "chocolate", "bread", "rice", "cheese", "A");
        insertExercise(12, "She cooked ______ with tomato sauce.", "pasta", "rice", "noodles", "bread", "A");
        insertExercise(12, "______ is a traditional Italian food.", "pasta", "pizza", "soup", "cake", "A");


        // Insert sample UserProgress
        insertUserProgress(1, 1, 1);
        insertUserProgress(1, 2, 2);
        insertUserProgress(2, 3, 3);
        insertUserProgress(2, 4, 4);

        // Insert sample Scores
        insertScore(1, 1, 100);
        insertScore(1, 2, 90);
        insertScore(2, 3, 85);
        insertScore(2, 4, 95);

        // Insert sample LearningSessions
        insertLearningSession(1, 1, 1, "2025-03-14");
        insertLearningSession(2, 3, 3, "2025-03-14");

        // Insert sample Streaks
        insertStreak(1, "2025-03-01", null, 14, 14);
        insertStreak(2, "2025-02-20", "2025-03-10", 0, 19);

        // Insert sample Roles
        insertRole("Admin");
        insertRole("Student");
        insertRole("Instructor");

        // Insert sample UserRoles
        insertUserRole(1, 1); // John as Admin
        insertUserRole(2, 2); // Jane as Student

        db.close();
    }


    public void updateUserName(String userId, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FullName", newName);

        db.update("Users", values, "UserID=?", new String[]{userId});
        db.close();
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fullName = null;

        Cursor cursor = db.rawQuery("SELECT FullName FROM Users WHERE Email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            fullName = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return fullName;
    }





}


