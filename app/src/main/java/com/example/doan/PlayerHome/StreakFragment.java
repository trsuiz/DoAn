package com.example.doan.PlayerHome;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import com.example.doan.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.widget.TextView;

public class StreakFragment extends Fragment {

    private CalendarView calendarView;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String LAST_LOGIN_DATE = "lastLoginDate";
    private static final String STREAK_COUNT = "streakCount";

    public StreakFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_streak, container, false);

        // Access the CalendarView
        calendarView = rootView.findViewById(R.id.calendarView);

        // Set the CalendarView to today's date
        Calendar calendar = Calendar.getInstance();
        long todayInMillis = calendar.getTimeInMillis();
        calendarView.setDate(todayInMillis, true, true);

        // Retrieve the last login date and streak count from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Lấy giá trị ngày tháng dưới dạng String (dùng định dạng yyyy-MM-dd)
        String lastLoginDateString = sharedPreferences.getString(LAST_LOGIN_DATE, "");
        int streakCount = sharedPreferences.getInt(STREAK_COUNT, 1);  // Default to 1 if no streak found

        // Get today's date as String (yyyy-MM-dd format)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = sdf.format(calendar.getTime());

        // Check if the streak should be updated
        if (lastLoginDateString.isEmpty() || !lastLoginDateString.equals(todayDate)) {
            // If the last login was not today or it's the first time logging in, reset or increment streak
            if (!lastLoginDateString.equals(todayDate)) {
                streakCount++;  // Increase streak count if it's a new day
            }

            // Update SharedPreferences with the new streak count and today's date
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LAST_LOGIN_DATE, todayDate);  // Save today's date as last login date (as String)
            editor.putInt(STREAK_COUNT, streakCount);       // Update streak count
            editor.apply();
        } else {
            // If logged in today already, don't change streak
            Log.d("STREAK_DEBUG", "Already logged in today. Streak remains the same.");
        }

        // Update the TextView to display the current streak count
        TextView streakTextView = rootView.findViewById(R.id.txtStreak);
        streakTextView.setText(String.valueOf(streakCount));

        return rootView;
    }

    // Function to check if streak is broken by time gap
    private boolean isStreakBroken(long lastStreakResetMillis, long todayInMillis) {
        long differenceInMillis = todayInMillis - lastStreakResetMillis;
        long oneDayInMillis = 24 * 60 * 60 * 1000;  // 1 day in milliseconds
        return differenceInMillis > oneDayInMillis;
    }
}
