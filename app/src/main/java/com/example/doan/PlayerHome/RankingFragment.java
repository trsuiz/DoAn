package com.example.doan.PlayerHome;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.doan.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RankingFragment() {
        // Required empty public constructor
    }

    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);

        // Initialize the player names and scores arrays
        String[] playerNames = new String[8];
        int[] playerScores = new int[8];

        // Initialize player names and scores
        playerNames[0] = "Bimmykhoa";
        playerScores[0] = getStoredScore();  // Assuming the stored score is already valid (non-negative)

// Set other player names and scores
        playerNames[1] = "Alice Johnson";
        playerScores[1] = Math.max(playerScores[0] - 10, 0);  // Prevent negative score

        playerNames[2] = "Bob Smith";
        playerScores[2] = Math.max(playerScores[0] - 20, 0);  // Prevent negative score

        playerNames[3] = "Charlie Brown";
        playerScores[3] = Math.max(playerScores[0] + 30, 0);  // Prevent negative score

        playerNames[4] = "David White";
        playerScores[4] = Math.max(playerScores[0] + 40, 0);  // Prevent negative score

        playerNames[5] = "Emily Davis";
        playerScores[5] = Math.max(playerScores[0] - 15, 0);  // Prevent negative score

        playerNames[6] = "Frank Wilson";
        playerScores[6] = Math.max(playerScores[0] - 50, 0);  // Prevent negative score

        playerNames[7] = "Grace Lee";
        playerScores[7] = Math.max(playerScores[0] + 10, 0);  // Prevent negative score


        // Sort the players by score in descending order
        // Create a custom class to hold both name and score, then sort the array
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerNames.length; i++) {
            players.add(new Player(playerNames[i], playerScores[i]));
        }

        // Sort players based on their scores in descending order
        Collections.sort(players, (p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        // Set the player names and scores in the respective TextViews based on rank
        for (int i = 0; i < players.size(); i++) {
            String rankTextViewId = "RankerName" + (i + 1); // dynamic TextView ID for names
            String scoreTextViewId = "RankerScore" + (i + 1); // dynamic TextView ID for scores

            int nameResId = getResources().getIdentifier(rankTextViewId, "id", getActivity().getPackageName());
            int scoreResId = getResources().getIdentifier(scoreTextViewId, "id", getActivity().getPackageName());

            TextView nameTextView = rootView.findViewById(nameResId);
            TextView scoreTextView = rootView.findViewById(scoreResId);

            if (nameTextView != null) {
                nameTextView.setText(players.get(i).getName());
            } else {
                Log.e("RankingFragment", "TextView with ID " + rankTextViewId + " not found");
            }

            if (scoreTextView != null) {
                scoreTextView.setText(String.valueOf(players.get(i).getScore()));
            } else {
                Log.e("RankingFragment", "TextView with ID " + scoreTextViewId + " not found");
            }
        }

        return rootView;
    }

    // Custom class to hold player data (name and score)
    private static class Player {
        private String name;
        private int score;

        public Player(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }

    // Method to retrieve the score from SharedPreferences
    private int getStoredScore() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_score", getActivity().MODE_PRIVATE);
        return sharedPreferences.getInt("score", 0);  // Default value is 0 if no score is stored
    }
}
