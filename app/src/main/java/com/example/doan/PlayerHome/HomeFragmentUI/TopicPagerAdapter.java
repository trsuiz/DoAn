package com.example.doan.PlayerHome.HomeFragmentUI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.ExerciseRender.ExerciseHolder;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

public class TopicPagerAdapter extends RecyclerView.Adapter<TopicPagerAdapter.TopicViewHolder> {

    private Context context;
    private List<Integer> topicLayouts;

    public TopicPagerAdapter(Context context, List<Integer> topicLayouts) {
        this.context = context;
        this.topicLayouts = topicLayouts != null ? topicLayouts : new ArrayList<>();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ViewType can be used if you need different layouts for different positions
        int layoutId = topicLayouts.get(viewType); // Get layout ID based on viewType (position)
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        // For simplicity, setup buttons for each topic layout
        setupButton(holder.itemView, R.id.btnLevel1_1, "About yourself: Lesson 1");
        setupButton(holder.itemView, R.id.btnLevel2_1, "About yourself: Lesson 2");
        setupButton(holder.itemView, R.id.btnLevel3_1, "About yourself: Lesson 3");
        setupButton(holder.itemView, R.id.btnLevel4_1, "About yourself: Lesson 4");

        setupButton(holder.itemView, R.id.btnLevel1_2, "Daily routines: Lesson 1");
        setupButton(holder.itemView, R.id.btnLevel2_2, "Daily routines: Lesson 2");
        setupButton(holder.itemView, R.id.btnLevel3_2, "Daily routines: Lesson 3");
        setupButton(holder.itemView, R.id.btnLevel4_2, "Daily routines: Lesson 4");

        setupButton(holder.itemView, R.id.btnLevel1_3, "Food: Lesson 1");
        setupButton(holder.itemView, R.id.btnLevel2_3, "Food: Lesson 2");
        setupButton(holder.itemView, R.id.btnLevel3_3, "Food: Lesson 3");
        setupButton(holder.itemView, R.id.btnLevel4_3, "Food: Lesson 4");
    }

    @Override
    public int getItemCount() {
        return topicLayouts != null ? topicLayouts.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        // Returning position as viewType (for simplicity, no specific use yet)
        return position;  // You can customize it if you need different layouts for different positions
    }

    // ViewHolder for each page
    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        public TopicViewHolder(View itemView) {
            super(itemView);
            // Initialize views if needed
        }
    }

    // Helper method to set up the buttons
    private void setupButton(View view, int buttonId, String lessonName) {
        ImageButton button = view.findViewById(buttonId);

        if (button != null) {
            // Set the click listener for each button
            button.setOnClickListener(v -> {
                // Log the button click event
                Log.d("TopicPagerAdapter", "Button clicked: " + lessonName);

                // Start the ExerciseHolder activity when the button is clicked
                Intent intent = new Intent(context, ExerciseHolder.class);
                intent.putExtra("selected_lesson", lessonName);  // Pass the lesson name to the next activity
                context.startActivity(intent);
            });
        } else {
            // Log an error if the button is not found (optional)
            Log.e("TopicPagerAdapter", "Button with ID " + buttonId + " not found.");
        }
    }
}


