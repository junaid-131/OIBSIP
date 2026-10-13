package com.example.calculator.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculator.R;
import com.example.calculator.data.local.HistoryEntity;
import com.example.calculator.viewmodel.CalculatorViewModel;

public class HistoryActivity extends AppCompatActivity
        implements HistoryAdapter.OnHistoryActionListener {

    private CalculatorViewModel viewModel;

    private HistoryAdapter historyAdapter;

    private RecyclerView historyRecyclerView;

    private TextView emptyHistoryText;

    private Button clearHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);

        initializeViews();
        initializeViewModel();
        setupRecyclerView();
        setupObservers();
        setupButtons();
    }

    // INITIALIZE VIEWS


    private void initializeViews() {

        historyRecyclerView =
                findViewById(R.id.historyRecyclerView);

        emptyHistoryText =
                findViewById(R.id.emptyHistoryText);

        clearHistoryButton =
                findViewById(R.id.clearHistoryButton);
    }


    // VIEWMODEL


    private void initializeViewModel() {

        viewModel = new ViewModelProvider(this)
                .get(CalculatorViewModel.class);
    }


    // RECYCLER VIEW


    private void setupRecyclerView() {

        historyAdapter = new HistoryAdapter(this);

        historyRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        historyRecyclerView.setAdapter(historyAdapter);
    }


    // OBSERVE HISTORY


    private void setupObservers() {

        viewModel.getHistory().observe(this, historyList -> {

            historyAdapter.setHistory(historyList);

            if (historyList == null || historyList.isEmpty()) {

                emptyHistoryText.setVisibility(TextView.VISIBLE);

                historyRecyclerView.setVisibility(
                        RecyclerView.GONE
                );

                clearHistoryButton.setEnabled(false);

            } else {

                emptyHistoryText.setVisibility(TextView.GONE);

                historyRecyclerView.setVisibility(
                        RecyclerView.VISIBLE
                );

                clearHistoryButton.setEnabled(true);
            }
        });
    }


    // BUTTONS


    private void setupButtons() {

        clearHistoryButton.setOnClickListener(v ->
                viewModel.clearHistory()
        );
    }


    // DELETE HISTORY ITEM


    @Override
    public void onDeleteHistory(HistoryEntity history) {

        if (history != null) {
            viewModel.deleteHistory(history);
        }
    }


    // LOAD SELECTED HISTORY INTO CALCULATOR


    @Override
    public void onLoadHistory(HistoryEntity history) {

        if (history == null
                || history.getExpression() == null
                || history.getExpression().trim().isEmpty()) {
            return;
        }

        Intent intent = new Intent();

        intent.putExtra(
                "history_expression",
                history.getExpression()
        );

        intent.putExtra(
                "history_result",
                history.getResult()
        );

        setResult(RESULT_OK, intent);

        finish();
    }
}