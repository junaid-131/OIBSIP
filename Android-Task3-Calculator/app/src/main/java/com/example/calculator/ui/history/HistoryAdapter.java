package com.example.calculator.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculator.R;
import com.example.calculator.data.local.HistoryEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter
        extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    public interface OnHistoryActionListener {

        void onDeleteHistory(
                HistoryEntity history
        );

        void onLoadHistory(
                HistoryEntity history
        );
    }

    private final OnHistoryActionListener listener;

    private final List<HistoryEntity> historyList =
            new ArrayList<>();

    public HistoryAdapter(
            OnHistoryActionListener listener
    ) {
        this.listener = listener;
    }

    public void setHistory(
            List<HistoryEntity> history
    ) {

        historyList.clear();

        if (history != null) {
            historyList.addAll(history);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_history,
                        parent,
                        false
                );

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull HistoryViewHolder holder,
            int position
    ) {

        HistoryEntity history =
                historyList.get(position);

        holder.expressionText.setText(
                history.getExpression()
        );

        holder.resultText.setText(
                "= " + history.getResult()
        );

        String formattedTime =
                formatTimestamp(
                        history.getTimestamp()
                );

        holder.timestampText.setText(
                formattedTime
        );

        holder.deleteButton.setOnClickListener(v ->
                listener.onDeleteHistory(history)
        );
        holder.itemView.setOnClickListener(v ->
                listener.onLoadHistory(history)
        );
    }

    @Override
    public int getItemCount() {

        return historyList.size();
    }

    private String formatTimestamp(
            long timestamp
    ) {

        SimpleDateFormat formatter =
                new SimpleDateFormat(
                        "dd MMM yyyy, hh:mm a",
                        Locale.getDefault()
                );

        return formatter.format(
                new Date(timestamp)
        );
    }

    static class HistoryViewHolder
            extends RecyclerView.ViewHolder {

        TextView expressionText;

        TextView resultText;

        TextView timestampText;

        ImageButton deleteButton;

        public HistoryViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            expressionText =
                    itemView.findViewById(
                            R.id.historyExpression
                    );

            resultText =
                    itemView.findViewById(
                            R.id.historyResult
                    );

            timestampText =
                    itemView.findViewById(
                            R.id.historyTimestamp
                    );

            deleteButton =
                    itemView.findViewById(
                            R.id.deleteHistoryButton
                    );
        }
    }
}