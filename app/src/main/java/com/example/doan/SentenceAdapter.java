package com.example.doan;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.SentenceViewHolder> {

    private List<String> sentenceList;

    public SentenceAdapter(List<String> sentenceList) {
        this.sentenceList = sentenceList;
    }

    @NonNull
    @Override
    public SentenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setTextSize(18);
        textView.setPadding(20, 20, 20, 20);
        return new SentenceViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull SentenceViewHolder holder, int position) {
        holder.sentenceTextView.setText(sentenceList.get(position));
    }

    @Override
    public int getItemCount() {
        return sentenceList.size();
    }

    public static class SentenceViewHolder extends RecyclerView.ViewHolder {
        TextView sentenceTextView;

        public SentenceViewHolder(@NonNull TextView itemView) {
            super(itemView);
            sentenceTextView = itemView;
        }
    }
}
