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
import java.util.Date;

public class StreakFragment extends Fragment {

    private CalendarView calendarView;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String LAST_LOGIN_DATE = "lastLoginDate";
    private static final String STREAK_COUNT = "streakCount";
    private static final String START_STREAK_DATE = "startStreakDate";

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
        String startStreakDateString = sharedPreferences.getString(START_STREAK_DATE, "");
        int streakCount = sharedPreferences.getInt(STREAK_COUNT, 1);  // Default to 1 if no streak found
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Get today's date as String (yyyy-MM-dd format)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = sdf.format(new Date());
        boolean isNewDay = !todayDate.equals(lastLoginDateString);
        // Check if the streak should be updated
        if (lastLoginDateString.isEmpty()) {
            // 👶 Lần đầu đăng nhập
            streakCount = 1;
            editor.putString(LAST_LOGIN_DATE, todayDate);
            editor.putString(START_STREAK_DATE, todayDate);
            editor.putInt(STREAK_COUNT, 1);
            editor.apply();

        } else if (isNewDay) {
            // 🆕 Ngày mới → tăng streak
            streakCount++;
            editor.putString(LAST_LOGIN_DATE, todayDate);
            editor.putInt(STREAK_COUNT, streakCount);

            if (startStreakDateString.isEmpty()) {
                editor.putString(START_STREAK_DATE, todayDate);
            }

            editor.apply();


            editor.putString(LAST_LOGIN_DATE, todayDate);
            editor.putInt(STREAK_COUNT, streakCount);

            // ✅ Nếu chưa có ngày bắt đầu streak thì lưu lại
            if (startStreakDateString.isEmpty()) {
                editor.putString(START_STREAK_DATE, lastLoginDateString);
            }

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
