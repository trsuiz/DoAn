package com.example.doan.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.doan.Login;
import com.example.doan.R;
import com.google.android.material.navigation.NavigationView;
import android.widget.Toast;

public class Admin_panel extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    ImageView menuIcon;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_panel);

        // Khi an vao nut menu, hien thi danh sach ben trai
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        menuIcon = findViewById(R.id.menu_icon);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        menuIcon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            Toast.makeText(Admin_panel.this, "Selected: " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawer(GravityCompat.START);  // Close drawer first

            String title = menuItem.getTitle().toString();  // Get the title of the selected item

            switch (title) {
                case "Dashboard":
                    Toast.makeText(Admin_panel.this, "Dashboard selected", Toast.LENGTH_SHORT).show();
                    // Handle Dashboard action here
                    return true;

                case "Manage Users":
                    Toast.makeText(Admin_panel.this, "Manage Users selected", Toast.LENGTH_SHORT).show();
                    // Handle Manage Users action here
                    return true;

                case "Settings":
                    Toast.makeText(Admin_panel.this, "Settings selected", Toast.LENGTH_SHORT).show();
                    // Handle Settings action here
                    return true;

                case "Logout":
                    logoutUser();  // Handle logout
                    return true;

                default:
                    return false;
            }
        });

        // Chuyen tu Adminpanel sang TopicCreate
        CardView lessonManagementCard = findViewById(R.id.card_lesson_management); // Add this ID to your XML

        // Set click listener
        lessonManagementCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start TopicCreate activity
                Intent intent = new Intent(Admin_panel.this, TopicCreate.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void logoutUser() {
        // Clear session or authentication data if used
        // For example, if using SharedPreferences:
        getSharedPreferences("UserSession", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Navigate back to Login screen
        Intent intent = new Intent(Admin_panel.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear back stack
        startActivity(intent);
        finish();  // Close Admin panel
    }

}