package com.example.doan.PlayerHome;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.doan.DatabaseHelper;
import com.example.doan.PlayerHome.StreakFragment;
import com.example.doan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private DatabaseHelper db;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //KHANG THEM
        String fullName = getIntent().getStringExtra("FULL_NAME");

        // Lưu vào Bundle để gửi sang Fragment
        Bundle bundle = new Bundle();
        bundle.putString("FULL_NAME", fullName);

        // Truyền Bundle sang ProfileFragment
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment) // Thay đúng ID của container fragment
                .commit();
        //KHANG THEM hhihihi

        /*db = new DatabaseHelper(this);
        database = db.getWritableDatabase();
        db.dropTables(database);
        db.displayAllTablesAndData();*/
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
            } else if (item.getItemId() == R.id.nav_voice) {
                selectedFragment = new VoiceFragment();
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
