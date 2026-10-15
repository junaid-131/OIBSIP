package com.junaid.unitconverter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.junaid.unitconverter.databinding.ActivityMainBinding;
import com.junaid.unitconverter.model.Category;
import com.junaid.unitconverter.model.Unit;
import com.junaid.unitconverter.ui.ConverterViewModel;
import com.junaid.unitconverter.ui.HistoryAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private ConverterViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean dark = getSharedPreferences("uc_prefs", MODE_PRIVATE)
                .getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(dark
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        vm = new ViewModelProvider(this).get(ConverterViewModel.class);

        setupChips();
        setupSpinners();
        setupKeypad();
        setupTopBar();
        observe();
    }

    private void setupTopBar() {
        b.btnHistory.setOnClickListener(v -> {
            haptic(v);
            showHistory();
        });
        b.btnTheme.setOnClickListener(v -> {
            haptic(v);
            toggleTheme();
        });
    }

    private void setupChips() {
        b.chipGroupCategory.setOnCheckedStateChangeListener(
                (ChipGroup group, java.util.List<Integer> checkedIds) -> {
                    if (checkedIds.isEmpty()) return;
                    int id = checkedIds.get(0);
                    Category c;
                    if (id == R.id.chipLength) c = Category.LENGTH;
                    else if (id == R.id.chipWeight) c = Category.WEIGHT;
                    else if (id == R.id.chipTemp) c = Category.TEMPERATURE;
                    else if (id == R.id.chipVolume) c = Category.VOLUME;
                    else if (id == R.id.chipArea) c = Category.AREA;
                    else if (id == R.id.chipSpeed) c = Category.SPEED;
                    else if (id == R.id.chipTime) c = Category.TIME;
                    else if (id == R.id.chipData) c = Category.DATA;
                    else return;
                    vm.setCategory(c);
                });
    }

    private void setupSpinners() {
        vm.getUnits().observe(this, units -> {
            if (units == null) return;
            ArrayAdapter<Unit> a1 = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, units);
            a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ArrayAdapter<Unit> a2 = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, units);
            a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            b.spFrom.setAdapter(a1);
            b.spTo.setAdapter(a2);

            Unit f = vm.getFromUnit().getValue();
            Unit t = vm.getToUnit().getValue();
            if (f != null) b.spFrom.setSelection(Math.max(0, units.indexOf(f)));
            if (t != null) b.spTo.setSelection(Math.max(0, units.indexOf(t)));
        });

        b.spFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                Unit u = (Unit) p.getItemAtPosition(pos);
                if (u != null && u != vm.getFromUnit().getValue()) vm.setFromUnit(u);
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });

        b.spTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                Unit u = (Unit) p.getItemAtPosition(pos);
                if (u != null && u != vm.getToUnit().getValue()) vm.setToUnit(u);
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });
    }

    private void setupKeypad() {
        int[] digits = { R.id.key0, R.id.key1, R.id.key2, R.id.key3, R.id.key4,
                R.id.key5, R.id.key6, R.id.key7, R.id.key8, R.id.key9 };
        for (int id : digits) {
            findViewById(id).setOnClickListener(v -> {
                haptic(v);
                vm.appendKey(((TextView) v).getText().toString());
            });
        }
        findViewById(R.id.keyDot).setOnClickListener(v -> { haptic(v); vm.appendKey("."); });
        findViewById(R.id.keyBack).setOnClickListener(v -> { haptic(v); vm.appendKey("⌫"); });
        findViewById(R.id.keyClear).setOnClickListener(v -> { haptic(v); vm.clearInput(); });
        findViewById(R.id.btnSwap).setOnClickListener(v -> { haptic(v); vm.swapUnits(); });

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            haptic(v);
            String cur = b.tvInput.getText().toString().trim();
            if (cur.isEmpty() || cur.equals("0")) {
                Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show();
                return;
            }
            vm.recordConversion();
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
        });
    }

    private void observe() {
        vm.getInput().observe(this, s -> b.tvInput.setText(s == null || s.isEmpty() ? "0" : s));
        vm.getResult().observe(this, s -> b.tvResult.setText(s == null ? "" : s));
        vm.getErrorEvent().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                vm.clearErrorEvent();
            }
        });

        b.tvResult.setOnLongClickListener(v -> {
            String t = b.tvResult.getText().toString();
            if (!t.isEmpty()) copyToClipboard(t);
            return true;
        });
        b.tvInput.setOnLongClickListener(v -> {
            String t = b.tvInput.getText().toString();
            if (!t.isEmpty()) copyToClipboard(t);
            return true;
        });
    }

    private void showHistory() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_history, null);
        RecyclerView rv = dialogView.findViewById(R.id.rvHistory);
        TextView empty = dialogView.findViewById(R.id.tvEmpty);

        rv.setLayoutManager(new LinearLayoutManager(this));
        HistoryAdapter adapter = new HistoryAdapter();
        rv.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.history_title)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, null)
                .create();

        vm.getHistory().observe(this, items -> {
            adapter.submit(items);
            boolean isEmpty = items == null || items.isEmpty();
            empty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            rv.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        });

        dialogView.findViewById(R.id.btnClearHistory)
                .setOnClickListener(x -> vm.clearHistory());
        dialog.show();
    }

    private void toggleTheme() {
        boolean isDark = (getResources().getConfiguration().uiMode
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        AppCompatDelegate.setDefaultNightMode(isDark
                ? AppCompatDelegate.MODE_NIGHT_NO
                : AppCompatDelegate.MODE_NIGHT_YES);
        getSharedPreferences("uc_prefs", MODE_PRIVATE).edit()
                .putBoolean("dark_mode", !isDark).apply();
    }

    private void copyToClipboard(String text) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("value", text));
            Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT).show();
        }
    }

    private void haptic(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
    }
}