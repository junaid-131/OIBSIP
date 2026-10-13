package com.example.calculator.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.calculator.data.local.HistoryEntity;
import com.example.calculator.data.repository.HistoryRepository;
import com.example.calculator.domain.CalculatorEngine;

import java.util.List;

public class CalculatorViewModel extends AndroidViewModel {

    private final CalculatorEngine calculatorEngine;
    private final HistoryRepository historyRepository;

    private final MutableLiveData<String> expression =
            new MutableLiveData<>("");

    private final MutableLiveData<String> result =
            new MutableLiveData<>("0");

    private final MutableLiveData<String> error =
            new MutableLiveData<>(null);

    private final MutableLiveData<Boolean> justCalculated =
            new MutableLiveData<>(false);

    public CalculatorViewModel(
            @NonNull Application application
    ) {
        super(application);

        calculatorEngine =
                new CalculatorEngine();

        historyRepository =
                new HistoryRepository(application);
    }

    public LiveData<String> getExpression() {
        return expression;
    }

    public LiveData<String> getResult() {
        return result;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getJustCalculated() {
        return justCalculated;
    }

    public LiveData<List<HistoryEntity>> getHistory() {
        return historyRepository.getAllHistory();
    }

    // ---------------------------------------------------------
    // NUMBER INPUT
    // ---------------------------------------------------------

    public void pressNumber(String number) {

        clearError();

        String current =
                getExpressionValue();

        if (Boolean.TRUE.equals(
                justCalculated.getValue()
        )) {

            current = "";

            justCalculated.setValue(false);
        }


        if (endsWithClosingParenthesis(current)) {

            current += "×";
        }

        if (current.equals("0")) {

            current = "";
        }

        expression.setValue(
                current + number
        );

        updatePreview();
    }


    // DECIMAL


    public void pressDecimal() {

        clearError();

        String current =
                getExpressionValue();

        if (Boolean.TRUE.equals(
                justCalculated.getValue()
        )) {

            current = "";

            justCalculated.setValue(false);
        }

        String currentNumber =
                getLastNumber(current);

        if (currentNumber.contains(".")) {
            return;
        }

        if (current.isEmpty()
                || endsWithOperator(current)
                || current.endsWith("(")) {

            current += "0.";

        } else {

            current += ".";
        }

        expression.setValue(current);

        updatePreview();
    }


    // OPERATORS


    public void pressOperator(String operator) {

        clearError();

        String current =
                getExpressionValue();

        if (current.isEmpty()) {
            return;
        }

        // Don't add another operator immediately
        if (endsWithOperator(current)) {

            current =
                    current.substring(
                            0,
                            current.length() - 1
                    );
        }


        if (current.endsWith("(")) {
            return;
        }


        if (Boolean.TRUE.equals(
                justCalculated.getValue()
        )) {

            String lastResult =
                    result.getValue();

            if (lastResult == null
                    || lastResult.equals("Error")) {

                return;
            }

            current = lastResult;

            expression.setValue(current);

            justCalculated.setValue(false);
        }

        expression.setValue(
                current + operator
        );

        justCalculated.setValue(false);

        updatePreview();
    }

    // OPEN PARENTHESIS

    public void pressOpenParenthesis() {

        clearError();

        String current =
                getExpressionValue();

        if (Boolean.TRUE.equals(
                justCalculated.getValue()
        )) {

            current = "";

            justCalculated.setValue(false);
        }

        if (!current.isEmpty()
                && (
                endsWithNumber(current)
                        || current.endsWith(")")
        )) {

            current += "×";
        }

        current += "(";

        expression.setValue(current);

        updatePreview();
    }

    // CLOSE PARENTHESIS

    public void pressCloseParenthesis() {

        clearError();

        String current =
                getExpressionValue();

        if (current.isEmpty()) {
            return;
        }

        if (endsWithOperator(current)
                || current.endsWith("(")) {

            return;
        }

        if (countCharacter(current, '(')
                <= countCharacter(current, ')')) {

            return;
        }

        current += ")";

        expression.setValue(current);

        updatePreview();
    }

    // PERCENTAGE

    public void pressPercent() {

        clearError();

        String current =
                getExpressionValue();

        if (current.isEmpty()
                || endsWithOperator(current)
                || current.endsWith("(")
                || current.endsWith("%")) {

            return;
        }

        current += "%";

        expression.setValue(current);

        updatePreview();
    }

    // SQUARE ROOT

    public void pressSquareRoot() {

        clearError();

        String current =
                getExpressionValue();

        if (Boolean.TRUE.equals(
                justCalculated.getValue()
        )) {

            current = "";

            justCalculated.setValue(false);
        }

        if (!current.isEmpty()
                && (
                endsWithNumber(current)
                        || current.endsWith(")")
        )) {

            current += "×";
        }

        current += "√(";

        expression.setValue(current);

        updatePreview();
    }

    // SQUARE

    public void pressSquare() {

        clearError();

        String current =
                getExpressionValue();

        if (current.isEmpty()) {
            return;
        }

        String operand =
                getLastOperand(current);

        if (operand.isEmpty()) {
            return;
        }

        int start =
                current.length()
                        - operand.length();

        String before =
                current.substring(0, start);

        String squared =
                "("
                        + operand
                        + ")×("
                        + operand
                        + ")";

        expression.setValue(
                before + squared
        );

        justCalculated.setValue(false);

        updatePreview();
    }

    // RECIPROCAL

    public void pressReciprocal() {

        clearError();

        String current =
                getExpressionValue();

        if (current.isEmpty()) {
            return;
        }

        String operand =
                getLastOperand(current);

        if (operand.isEmpty()) {
            return;
        }

        int start =
                current.length()
                        - operand.length();

        String before =
                current.substring(0, start);

        String reciprocal =
                "(1/("
                        + operand
                        + "))";

        expression.setValue(
                before + reciprocal
        );

        justCalculated.setValue(false);

        updatePreview();
    }

    // SIGN CHANGE

    public void pressSign() {

        clearError();

        String current =
                getExpressionValue();

        if (current.isEmpty()) {
            return;
        }

        String operand =
                getLastOperand(current);

        if (operand.isEmpty()) {
            return;
        }

        int start =
                current.length()
                        - operand.length();

        String before =
                current.substring(0, start);

        String changed =
                "(-"
                        + operand
                        + ")";

        expression.setValue(
                before + changed
        );

        justCalculated.setValue(false);

        updatePreview();
    }

    // CALCULATE

    public void calculate() {

        clearError();

        String current =
                getExpressionValue();

        if (current.isEmpty()) {
            return;
        }


        if (Boolean.TRUE.equals(
                justCalculated.getValue()
        )) {
            return;
        }

        if (endsWithOperator(current)
                || current.endsWith("(")) {

            return;
        }

        while (countCharacter(current, '(')
                > countCharacter(current, ')')) {

            current += ")";
        }

        String evaluationExpression =
                current;

        if (isStandalonePercentage(
                evaluationExpression
        )) {

            String number =
                    evaluationExpression.substring(
                            0,
                            evaluationExpression.length() - 1
                    );

            evaluationExpression =
                    number + "/100";
        }

        try {

            String calculatedResult =
                    calculatorEngine.evaluate(
                            evaluationExpression
                    );

            expression.setValue(calculatedResult);

            result.setValue(calculatedResult);

            justCalculated.setValue(true);

            historyRepository.insert(
                    current,
                    calculatedResult
            );

        } catch (
                CalculatorEngine.CalculatorException exception
        ) {

            result.setValue("Error");

            error.setValue(
                    exception.getMessage()
            );

            justCalculated.setValue(false);
        }
    }

    // CLEAR

    public void clear() {

        expression.setValue("");

        result.setValue("0");

        error.setValue(null);

        justCalculated.setValue(false);
    }

    // BACKSPACE

    public void backspace() {

        clearError();

        String current =
                getExpressionValue();

        if (current.isEmpty()) {
            return;
        }

        if (current.endsWith("√(")) {

            current =
                    current.substring(
                            0,
                            current.length() - 2
                    );

        } else {

            current =
                    current.substring(
                            0,
                            current.length() - 1
                    );
        }

        expression.setValue(current);

        justCalculated.setValue(false);

        if (current.isEmpty()) {

            result.setValue("0");

        } else {

            updatePreview();
        }
    }

    // HISTORY

    public void deleteHistory(
            HistoryEntity history
    ) {

        historyRepository.delete(history);
    }

    public void clearHistory() {

        historyRepository.deleteAll();
    }
    public void loadHistoryExpression(
            HistoryEntity history
    ) {

        if (history == null
                || history.getExpression() == null
                || history.getExpression().isEmpty()) {

            return;
        }

        expression.setValue(
                history.getExpression()
        );

        result.setValue(
                history.getResult()
        );

        error.setValue(null);

        justCalculated.setValue(false);
    }

    // PREVIEW

    private void updatePreview() {

        String current =
                getExpressionValue();

        if (current.isEmpty()) {

            result.setValue("0");

            return;
        }

        if (endsWithOperator(current)
                || current.endsWith("(")
                || current.endsWith("%")) {

            return;
        }

        String evaluationExpression =
                current;

        while (countCharacter(
                evaluationExpression,
                '('
        ) > countCharacter(
                evaluationExpression,
                ')'
        )) {

            evaluationExpression += ")";
        }

        if (isStandalonePercentage(
                evaluationExpression
        )) {

            String number =
                    evaluationExpression.substring(
                            0,
                            evaluationExpression.length() - 1
                    );

            evaluationExpression =
                    number + "/100";
        }

        try {

            String preview =
                    calculatorEngine.evaluate(
                            evaluationExpression
                    );

            result.setValue(preview);

        } catch (
                CalculatorEngine.CalculatorException ignored
        ) {


        }
    }

    // GET EXPRESSION

    private String getExpressionValue() {

        String value =
                expression.getValue();

        return value == null
                ? ""
                : value;
    }

    // GET LAST NUMBER

    private String getLastNumber(
            String value
    ) {

        if (value == null
                || value.isEmpty()) {

            return "";
        }

        int index =
                value.length() - 1;

        StringBuilder number =
                new StringBuilder();

        while (index >= 0) {

            char character =
                    value.charAt(index);

            if (Character.isDigit(character)
                    || character == '.') {

                number.insert(
                        0,
                        character
                );

                index--;

            } else {

                break;
            }
        }

        return number.toString();
    }

    // GET LAST OPERAND

    private String getLastOperand(
            String value
    ) {

        if (value == null
                || value.isEmpty()) {

            return "";
        }

        // If expression ends with a parenthesis,
        // find the complete matching parenthesis.
        if (value.endsWith(")")) {

            int depth = 0;

            for (int i = value.length() - 1;
                 i >= 0;
                 i--) {

                char character =
                        value.charAt(i);

                if (character == ')') {

                    depth++;

                } else if (character == '(') {

                    depth--;

                    if (depth == 0) {

                        return value.substring(i);
                    }
                }
            }
        }

        return getLastNumber(value);
    }

    // OPERATOR CHECK

    private boolean endsWithOperator(
            String value
    ) {

        if (value == null
                || value.isEmpty()) {

            return false;
        }

        char last =
                value.charAt(
                        value.length() - 1
                );

        return last == '+'
                || last == '-'
                || last == '−'
                || last == '×'
                || last == '÷'
                || last == '*'
                || last == '/'
                || last == '%';
    }

    // NUMBER CHECK

    private boolean endsWithNumber(
            String value
    ) {

        if (value == null
                || value.isEmpty()) {

            return false;
        }

        char last =
                value.charAt(
                        value.length() - 1
                );

        return Character.isDigit(last)
                || last == '.';
    }

    // CLOSING PARENTHESIS

    private boolean endsWithClosingParenthesis(
            String value
    ) {

        return value != null
                && value.endsWith(")");
    }

    // COUNT CHARACTERS

    private int countCharacter(
            String value,
            char target
    ) {

        int count = 0;

        for (int i = 0;
             i < value.length();
             i++) {

            if (value.charAt(i) == target) {

                count++;
            }
        }

        return count;
    }

    // STANDALONE PERCENTAGE

    private boolean isStandalonePercentage(
            String value
    ) {

        if (value == null
                || value.length() < 2
                || !value.endsWith("%")) {

            return false;
        }

        String number =
                value.substring(
                        0,
                        value.length() - 1
                );

        try {

            Double.parseDouble(number);

            return true;

        } catch (
                NumberFormatException exception
        ) {

            return false;
        }
    }

    // CLEAR ERROR

    private void clearError() {

        error.setValue(null);
    }

    // CLEANUP

    @Override
    protected void onCleared() {

        super.onCleared();

        historyRepository.shutdown();
    }
}
