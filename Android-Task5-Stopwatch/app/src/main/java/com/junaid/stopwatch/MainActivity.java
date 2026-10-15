package com.junaid.stopwatch;

import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.junaid.stopwatch.databinding.ActivityMainBinding;
import com.junaid.stopwatch.ui.LapAdapter;
import com.junaid.stopwatch.ui.StopwatchViewModel;
import com.junaid.stopwatch.util.TimeFormatter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private StopwatchViewModel vm;
    private LapAdapter lapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean dark = getSharedPreferences("sw_prefs", MODE_PRIVATE)
                .getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(dark
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        vm = new ViewModelProvider(this).get(StopwatchViewModel.class);

        setupLapsList();
        setupButtons();
        observe();
    }

    private void setupLapsList() {
        lapAdapter = new LapAdapter();
        b.rvLaps.setLayoutManager(new LinearLayoutManager(this));
        b.rvLaps.setAdapter(lapAdapter);
    }

    private void setupButtons() {
        b.btnStart.setOnClickListener(v -> {
            haptic(v);
            Boolean running = vm.getRunning().getValue();
            if (running != null && running) vm.pause();
            else vm.start();
        });

        b.btnReset.setOnClickListener(v -> {
            haptic(v);
            new AlertDialog.Builder(this)
                    .setMessage(R.string.reset_confirm)
                    .setPositiveButton(R.string.yes, (d, w) -> vm.reset())
                    .setNegativeButton(R.string.no, null)
                    .show();
        });

        b.btnLap.setOnClickListener(v -> {
            haptic(v);
            Boolean running = vm.getRunning().getValue();
            if (running == null || !running) {
                Toast.makeText(this, R.string.lap_start_hint,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            vm.lap();
        });

        b.btnTheme.setOnClickListener(v -> { haptic(v); toggleTheme(); });
    }

    private void observe() {
        vm.getElapsed().observe(this, ms -> {
            long v = ms == null ? 0 : ms;
            b.tvTimeMain.setText(TimeFormatter.formatMain(v));
            b.tvTimeMillis.setText(TimeFormatter.formatMillis(v));
        });

        vm.getRunning().observe(this, running -> {
            boolean r = running != null && running;
            if (r) {
                b.btnStart.setText(R.string.pause);
                b.btnStart.setBackgroundTintList(
                        ContextCompat.getColorStateList(this, R.color.amber));
                b.tvStatus.setText(R.string.status_running);
                b.btnLap.setEnabled(true);
                b.btnLap.setAlpha(1f);
            } else {
                b.btnStart.setText(R.string.start);
                b.btnStart.setBackgroundTintList(
                        ContextCompat.getColorStateList(this, R.color.green));
                b.tvStatus.setText(vm.currentElapsed() > 0
                        ? R.string.status_paused : R.string.status_ready);
                b.btnLap.setEnabled(false);
                b.btnLap.setAlpha(0.4f);
            }
        });

        vm.getLaps().observe(this, laps -> {
            lapAdapter.submit(laps);
            boolean isEmpty = laps == null || laps.isEmpty();
            b.tvLapsEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            b.rvLaps.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        });
    }

    private void toggleTheme() {
        boolean isDark = (getResources().getConfiguration().uiMode
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        AppCompatDelegate.setDefaultNightMode(isDark
                ? AppCompatDelegate.MODE_NIGHT_NO
                : AppCompatDelegate.MODE_NIGHT_YES);
        getSharedPreferences("sw_prefs", MODE_PRIVATE).edit()
                .putBoolean("dark_mode", !isDark).apply();
    }

    private void haptic(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
    }
}