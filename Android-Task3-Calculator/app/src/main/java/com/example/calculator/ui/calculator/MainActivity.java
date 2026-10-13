package com.example.calculator.ui.calculator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.calculator.R;
import com.example.calculator.data.local.HistoryEntity;
import com.example.calculator.ui.history.HistoryActivity;
import com.example.calculator.viewmodel.CalculatorViewModel;

public class MainActivity extends AppCompatActivity {

    private static final int HISTORY_REQUEST_CODE = 100;

    private static final String EXTRA_HISTORY_EXPRESSION =
            "history_expression";

    private static final String EXTRA_HISTORY_RESULT =
            "history_result";

    private CalculatorViewModel viewModel;

    private TextView expressionText;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initializeViews();
        initializeViewModel();
        setupObservers();
        setupButtons();
    }


    // INITIALIZE VIEWS


    private void initializeViews() {
        expressionText = findViewById(R.id.expressionText);
        resultText = findViewById(R.id.resultText);
    }


    // VIEWMODEL


    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this)
                .get(CalculatorViewModel.class);
    }


    // OBSERVERS


    private void setupObservers() {

        viewModel.getExpression().observe(this, expression -> {
            expressionText.setText(
                    expression == null ? "" : expression
            );
        });

        viewModel.getResult().observe(this, result -> {
            resultText.setText(
                    result == null || result.isEmpty()
                            ? "0"
                            : result
            );
        });

        viewModel.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(
                        MainActivity.this,
                        error,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    // BUTTONS


    private void setupButtons() {

        // Numbers
        findViewById(R.id.button0)
                .setOnClickListener(v -> viewModel.pressNumber("0"));

        findViewById(R.id.button1)
                .setOnClickListener(v -> viewModel.pressNumber("1"));

        findViewById(R.id.button2)
                .setOnClickListener(v -> viewModel.pressNumber("2"));

        findViewById(R.id.button3)
                .setOnClickListener(v -> viewModel.pressNumber("3"));

        findViewById(R.id.button4)
                .setOnClickListener(v -> viewModel.pressNumber("4"));

        findViewById(R.id.button5)
                .setOnClickListener(v -> viewModel.pressNumber("5"));

        findViewById(R.id.button6)
                .setOnClickListener(v -> viewModel.pressNumber("6"));

        findViewById(R.id.button7)
                .setOnClickListener(v -> viewModel.pressNumber("7"));

        findViewById(R.id.button8)
                .setOnClickListener(v -> viewModel.pressNumber("8"));

        findViewById(R.id.button9)
                .setOnClickListener(v -> viewModel.pressNumber("9"));

        // Decimal
        findViewById(R.id.buttonDecimal)
                .setOnClickListener(v -> viewModel.pressDecimal());

        // Operators
        findViewById(R.id.buttonAdd)
                .setOnClickListener(v -> viewModel.pressOperator("+"));

        findViewById(R.id.buttonSubtract)
                .setOnClickListener(v -> viewModel.pressOperator("−"));

        findViewById(R.id.buttonMultiply)
                .setOnClickListener(v -> viewModel.pressOperator("×"));

        findViewById(R.id.buttonDivide)
                .setOnClickListener(v -> viewModel.pressOperator("÷"));

        // Calculate
        findViewById(R.id.buttonEquals)
                .setOnClickListener(v -> viewModel.calculate());

        // Clear
        findViewById(R.id.buttonClear)
                .setOnClickListener(v -> viewModel.clear());

        // Backspace
        findViewById(R.id.buttonBackspace)
                .setOnClickListener(v -> viewModel.backspace());

        // Parentheses
        findViewById(R.id.buttonOpenParenthesis)
                .setOnClickListener(v ->
                        viewModel.pressOpenParenthesis());

        findViewById(R.id.buttonCloseParenthesis)
                .setOnClickListener(v ->
                        viewModel.pressCloseParenthesis());

        // Scientific functions
        findViewById(R.id.buttonSqrt)
                .setOnClickListener(v -> viewModel.pressSquareRoot());

        findViewById(R.id.buttonSquare)
                .setOnClickListener(v -> viewModel.pressSquare());

        findViewById(R.id.buttonReciprocal)
                .setOnClickListener(v -> viewModel.pressReciprocal());

        findViewById(R.id.buttonPercent)
                .setOnClickListener(v -> viewModel.pressPercent());

        // Positive / negative
        findViewById(R.id.buttonSign)
                .setOnClickListener(v -> viewModel.pressSign());

        // History
        findViewById(R.id.buttonHistory)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(
                            MainActivity.this,
                            HistoryActivity.class
                    );

                    startActivityForResult(
                            intent,
                            HISTORY_REQUEST_CODE
                    );
                });

        // Copy result button
        findViewById(R.id.buttonCopy)
                .setOnClickListener(v -> copyResultToClipboard());

        // Long-press result to copy
        resultText.setOnLongClickListener(v -> {
            copyResultToClipboard();
            return true;
        });
    }


    // RECEIVE SELECTED HISTORY ITEM


    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != HISTORY_REQUEST_CODE
                || resultCode != RESULT_OK
                || data == null) {
            return;
        }

        String historyExpression =
                data.getStringExtra(EXTRA_HISTORY_EXPRESSION);

        String historyResult =
                data.getStringExtra(EXTRA_HISTORY_RESULT);

        if (historyExpression == null
                || historyExpression.trim().isEmpty()) {
            return;
        }

        HistoryEntity selectedHistory = new HistoryEntity(
                historyExpression,
                historyResult == null ? "0" : historyResult,
                System.currentTimeMillis()
        );

        viewModel.loadHistoryExpression(selectedHistory);
    }


    // COPY RESULT
    private void copyResultToClipboard() {

        String result = resultText.getText().toString();

        if (result.isEmpty()) {
            return;
        }

        ClipboardManager clipboard =
                (ClipboardManager) getSystemService(
                        Context.CLIPBOARD_SERVICE
                );

        if (clipboard == null) {
            Toast.makeText(
                    this,
                    "Clipboard unavailable",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        ClipData clip = ClipData.newPlainText(
                "Calculator Result",
                result
        );

        clipboard.setPrimaryClip(clip);

        Toast.makeText(
                this,
                "Result copied",
                Toast.LENGTH_SHORT
        ).show();
    }
}
