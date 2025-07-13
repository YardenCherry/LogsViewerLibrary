package com.example.logviewerlibrary;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private final List<String> logs;

    public LogAdapter(List<String> logs) {
        this.logs = logs;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        String log = logs.get(position);
        holder.logText.setText(log);
        holder.logText.setTextSize(13);
        holder.logText.setTextColor(Color.WHITE);
        holder.logText.setBackgroundColor(Color.parseColor("#222222"));
        holder.logText.setPadding(16, 12, 16, 12);
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView logText;

        public LogViewHolder(View itemView) {
            super(itemView);
            logText = itemView.findViewById(android.R.id.text1);
        }
    }
}
