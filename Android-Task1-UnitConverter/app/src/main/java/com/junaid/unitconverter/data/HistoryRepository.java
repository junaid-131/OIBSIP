package com.junaid.unitconverter.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.junaid.unitconverter.model.ConversionRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryRepository {

    private static final String PREFS = "uc_history";
    private static final String KEY = "records";
    private static final int MAX = 50;

    private final SharedPreferences prefs;
    private final MutableLiveData<List<ConversionRecord>> history =
            new MutableLiveData<>(Collections.emptyList());

    public HistoryRepository(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        history.setValue(load());
    }

    public LiveData<List<ConversionRecord>> getHistory() { return history; }

    public void add(String fromText, String toText) {
        List<ConversionRecord> cur = new ArrayList<>(history.getValue() == null
                ? Collections.emptyList() : history.getValue());
        cur.add(0, new ConversionRecord(fromText, toText, System.currentTimeMillis()));
        if (cur.size() > MAX) cur = cur.subList(0, MAX);
        history.setValue(cur);
        save(cur);
    }

    public void clear() {
        history.setValue(Collections.emptyList());
        prefs.edit().remove(KEY).apply();
    }

    private List<ConversionRecord> load() {
        List<ConversionRecord> list = new ArrayList<>();
        String raw = prefs.getString(KEY, "");
        if (raw == null || raw.isEmpty()) return list;
        for (String line : raw.split("\n")) {
            String[] p = line.split("\\|", 3);
            if (p.length == 3) {
                try {
                    list.add(new ConversionRecord(p[0], p[1], Long.parseLong(p[2])));
                } catch (NumberFormatException ignored) {}
            }
        }
        return list;
    }

    private void save(List<ConversionRecord> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            ConversionRecord r = list.get(i);
            sb.append(r.fromText).append("|").append(r.toText).append("|").append(r.timestamp);
            if (i < list.size() - 1) sb.append("\n");
        }
        prefs.edit().putString(KEY, sb.toString()).apply();
    }
}