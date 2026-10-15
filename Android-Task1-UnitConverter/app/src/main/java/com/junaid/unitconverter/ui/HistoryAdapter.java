package com.junaid.unitconverter.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.junaid.unitconverter.R;
import com.junaid.unitconverter.model.ConversionRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VH> {

    private final List<ConversionRecord> items = new ArrayList<>();
    private final SimpleDateFormat fmt =
            new SimpleDateFormat("MMM d, HH:mm", Locale.US);

    public void submit(List<ConversionRecord> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        ConversionRecord item = items.get(position);
        h.tvFrom.setText(item.fromText);
        h.tvTo.setText("= " + item.toText);
        if (h.tvTime != null) {
            h.tvTime.setText(fmt.format(new Date(item.timestamp)));
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvFrom, tvTo, tvTime;
        VH(@NonNull View itemView) {
            super(itemView);
            tvFrom = itemView.findViewById(R.id.tvHistFrom);
            tvTo = itemView.findViewById(R.id.tvHistTo);
            tvTime = itemView.findViewById(R.id.tvHistTime);
        }
    }
}