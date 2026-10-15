package com.junaid.stopwatch.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.junaid.stopwatch.R;
import com.junaid.stopwatch.model.Lap;
import com.junaid.stopwatch.util.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class LapAdapter extends RecyclerView.Adapter<LapAdapter.VH> {

    private final List<Lap> items = new ArrayList<>();
    private long bestSplit = Long.MAX_VALUE;
    private long worstSplit = Long.MIN_VALUE;

    public void submit(List<Lap> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        recompute();
        notifyDataSetChanged();
    }

    private void recompute() {
        bestSplit = Long.MAX_VALUE;
        worstSplit = Long.MIN_VALUE;
        if (items.size() < 2) return;
        for (Lap l : items) {
            if (l.splitMs < bestSplit) bestSplit = l.splitMs;
            if (l.splitMs > worstSplit) worstSplit = l.splitMs;
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lap, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Lap lap = items.get(position);
        h.tvNumber.setText("Lap " + lap.number);
        h.tvSplit.setText(TimeFormatter.formatShort(lap.splitMs));
        h.tvTotal.setText(TimeFormatter.formatShort(lap.totalMs));

        int color = h.tvSplit.getResources().getColor(R.color.primary, null);
        if (items.size() >= 2) {
            if (lap.splitMs == bestSplit) {
                color = h.tvSplit.getResources().getColor(R.color.green, null);
            } else if (lap.splitMs == worstSplit) {
                color = h.tvSplit.getResources().getColor(R.color.red, null);
            }
        }
        h.tvSplit.setTextColor(color);
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvNumber, tvSplit, tvTotal;
        VH(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvLapNumber);
            tvSplit = itemView.findViewById(R.id.tvLapSplit);
            tvTotal = itemView.findViewById(R.id.tvLapTotal);
        }
    }
}