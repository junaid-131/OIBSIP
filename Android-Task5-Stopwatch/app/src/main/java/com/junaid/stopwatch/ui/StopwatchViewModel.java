package com.junaid.stopwatch.ui;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.junaid.stopwatch.model.Lap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StopwatchViewModel extends ViewModel {

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final MutableLiveData<Long> elapsed = new MutableLiveData<>(0L);
    private final MutableLiveData<Boolean> running = new MutableLiveData<>(false);
    private final MutableLiveData<List<Lap>> laps = new MutableLiveData<>(Collections.emptyList());

    private long startRealtime = 0;
    private long accumulated = 0;
    private boolean isRunning = false;

    private final Runnable ticker = new Runnable() {
        @Override public void run() {
            if (!isRunning) return;
            elapsed.setValue(accumulated + (SystemClock.elapsedRealtime() - startRealtime));
            handler.postDelayed(this, 30);
        }
    };

    public LiveData<Long> getElapsed() { return elapsed; }
    public LiveData<Boolean> getRunning() { return running; }
    public LiveData<List<Lap>> getLaps() { return laps; }

    public void start() {
        if (isRunning) return;
        isRunning = true;
        running.setValue(true);
        startRealtime = SystemClock.elapsedRealtime();
        handler.post(ticker);
    }

    public void pause() {
        if (!isRunning) return;
        isRunning = false;
        running.setValue(false);
        accumulated += SystemClock.elapsedRealtime() - startRealtime;
        handler.removeCallbacks(ticker);
        elapsed.setValue(accumulated);
    }

    public void reset() {
        isRunning = false;
        running.setValue(false);
        handler.removeCallbacks(ticker);
        accumulated = 0;
        startRealtime = 0;
        elapsed.setValue(0L);
        laps.setValue(Collections.emptyList());
    }

    public void lap() {
        if (!isRunning) return;
        long now = accumulated + (SystemClock.elapsedRealtime() - startRealtime);
        List<Lap> current = laps.getValue();
        if (current == null) current = new ArrayList<>();
        int number = current.size() + 1;
        long prevTotal = current.isEmpty() ? 0 : current.get(0).totalMs;
        long split = now - prevTotal;
        List<Lap> updated = new ArrayList<>(current);
        updated.add(0, new Lap(number, now, split));
        laps.setValue(updated);
    }


    public long currentElapsed() {
        Long v = elapsed.getValue();
        return v == null ? 0 : v;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacks(ticker);
    }
}