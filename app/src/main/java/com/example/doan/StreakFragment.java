package com.example.doan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StreakFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StreakFragment extends Fragment {

    private CalendarView calendarView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StreakFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StreakFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StreakFragment newInstance(String param1, String param2) {
        StreakFragment fragment = new StreakFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_streak, container, false);

        // Access the CalendarView
        calendarView = rootView.findViewById(R.id.calendarView);

        // Get the current date (today) and set it in the CalendarView
        Calendar calendar = Calendar.getInstance();
        long todayInMillis = calendar.getTimeInMillis();

        // Set the CalendarView to today's date
        calendarView.setDate(todayInMillis, true, true);

        return rootView;
    }
}