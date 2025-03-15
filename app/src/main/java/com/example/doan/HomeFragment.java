package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        // Find the button by its ID
        Button btnGoToFood1 = view.findViewById(R.id.btnFood1);

        // Set an OnClickListener for the button
        btnGoToFood1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to go to the Food1 activity
                Intent intent = new Intent(getActivity(), Food1.class);

                // Start the Food1 activity
                startActivity(intent);
            }
        });
        return view;
    }
}
