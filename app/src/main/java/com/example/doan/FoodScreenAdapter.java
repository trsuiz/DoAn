package com.example.doan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FoodScreenAdapter extends FragmentStateAdapter {

    public FoodScreenAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        FoodScreenFragment fragment = new FoodScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pageIndex", position); // Pass the page index to the fragment
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 10;  // Total number of screens (10)
    }
}
