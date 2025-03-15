package com.example.doan;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CalendarView;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseHelper = new DatabaseHelper(this);

/*// Open the database in writable mode
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

// Drop the tables and recreate them
        databaseHelper.dropTables(db); // This will drop the tables and recreate them using onCreate()

// Now you can insert new data
        databaseHelper.insertLessonData();*/



        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set up listener for navigation item clicks using if-else (instead of switch-case)
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Use if-else instead of switch-case
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_streak) {
                selectedFragment = new StreakFragment();
            } else if (item.getItemId() == R.id.nav_ranking) {
                selectedFragment = new RankingFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            // Replace current fragment with the selected fragment
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)  // Ensure this container exists in layout
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }

            return true;
        });

        // Optionally, set default fragment if needed
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);  // Set default tab to Home
        }
    }
}
