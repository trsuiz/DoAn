package com.example.doan;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class Food1 extends AppCompatActivity {

    private ViewPager2 viewPager2;
    public int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food1);

        // Set up the ViewPager2 and Adapter
        ViewPager2 viewPager2 = findViewById(R.id.viewPager2); // Make sure this ID exists in the layout
        FoodScreenAdapter adapter = new FoodScreenAdapter(this);
        viewPager2.setAdapter(adapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Update currentPage in Food1 when the page is changed
                currentPage = position;
                // Notify fragment to update the content
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + position);
                if (fragment != null && fragment instanceof FoodScreenFragment) {
                    ((FoodScreenFragment) fragment).loadExerciseData(position);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewPager2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewPager2.setCurrentItem(0, true); // Set the current page to 0 (first exercise)

    }
}