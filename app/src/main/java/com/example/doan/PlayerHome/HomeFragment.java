package com.example.doan.PlayerHome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doan.ExerciseRender.ExerciseHolder;
import com.example.doan.PlayerHome.HomeFragmentUI.TopicPagerAdapter;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView txtHeart, txtStreak;
    private ImageButton btnHeart, btnStreak, btnShop, btnSettings, btnLevel1, btnLevel2, btnLevel3, btnLevel4;
    private ViewPager2 viewPager;
    private TopicPagerAdapter adapter;
    private List<Integer> topicLayouts;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the ViewPager2
        viewPager = view.findViewById(R.id.viewPager);  // Use 'view' to access elements

        // Create a list of topic layouts
        topicLayouts = new ArrayList<>();
        topicLayouts.add(R.layout.topic_layout_1);
        topicLayouts.add(R.layout.topic_layout_2);
        topicLayouts.add(R.layout.topic_layout_3);  // Add more topics as needed

        // Set up the adapter
        adapter = new TopicPagerAdapter(getActivity(), topicLayouts);
        viewPager.setAdapter(adapter);

        // Register the page change callback
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("TopicLayouts", "Selected page: " + position);
            }
        });

        // Bind other UI components
        txtHeart = view.findViewById(R.id.txtHeart);
        txtStreak = view.findViewById(R.id.txtStreak);
        btnHeart = view.findViewById(R.id.btnHeart);
        btnStreak = view.findViewById(R.id.btnStreak);
        btnShop = view.findViewById(R.id.btnShop);
        btnSettings = view.findViewById(R.id.btnSettings);

        // Handle button clicks
        btnHeart.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Bạn có " + txtHeart.getText() + " máu!", Toast.LENGTH_SHORT).show();
        });

        btnStreak.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Streak hiện tại: " + txtStreak.getText(), Toast.LENGTH_SHORT).show();
        });

        btnShop.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Mở cửa hàng!", Toast.LENGTH_SHORT).show();
            // Add code to open the ShopActivity
        });

        btnSettings.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Mở cài đặt!", Toast.LENGTH_SHORT).show();
            // Add code to open the SettingsActivity
        });

        return view;
    }
}
