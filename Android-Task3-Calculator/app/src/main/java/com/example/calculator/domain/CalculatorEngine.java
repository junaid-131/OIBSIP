package com.example.calculator.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CalculatorEngine {

    private static final MathContext MATH_CONTEXT =
            new MathContext(15, RoundingMode.HALF_UP);


    public String evaluate(String expression) throws CalculatorException {

        if (expression == null || expression.trim().isEmpty()) {
            throw new CalculatorException("Empty expression");
        }

        String normalized = expression
                .replace("×", "*")
                .replace("÷", "/")
                .replace("−", "-")
                .replace("√", "sqrt");

        try {
            List<String> tokens = tokenize(normalized);
            List<String> postfix = convertToPostfix(tokens);

            BigDecimal result = evaluatePostfix(postfix);

            return formatResult(result);

        } catch (CalculatorException exception) {
            throw exception;

        } catch (Exception exception) {
            throw new CalculatorException("Invalid expression");
        }
    }


    // TOKENIZER


    private List<String> tokenize(String expression)
            throws CalculatorException {

        List<String> tokens = new ArrayList<>();

        int index = 0;

        while (index < expression.length()) {

            char current = expression.charAt(index);

            // Ignore spaces
            if (Character.isWhitespace(current)) {
                index++;
                continue;
            }


            // NUMBER


            if (Character.isDigit(current) || current == '.') {

                StringBuilder number = new StringBuilder();

                boolean hasDecimal = false;

                while (index < expression.length()) {

                    char character = expression.charAt(index);

                    if (Character.isDigit(character)) {

                        number.append(character);
                        index++;

                    } else if (character == '.') {

                        if (hasDecimal) {
                            throw new CalculatorException(
                                    "Invalid number"
                            );
                        }

                        hasDecimal = true;
                        number.append(character);
                        index++;

                    } else {
                        break;
                    }
                }

                if (number.toString().equals(".")) {
                    throw new CalculatorException(
                            "Invalid number"
                    );
                }

                tokens.add(number.toString());

                continue;
            }


            // SQUARE ROOT


            if (expression.startsWith("sqrt", index)) {

                tokens.add("sqrt");

                index += 4;

                continue;
            }


            // OPERATORS


            if (current == '+'
                    || current == '-'
                    || current == '*'
                    || current == '/'
                    || current == '%') {



                if (current == '-'
                        && (
                        tokens.isEmpty()
                                || isOperator(
                                tokens.get(tokens.size() - 1)
                        )
                                || tokens.get(tokens.size() - 1)
                                .equals("(")
                )) {

                    tokens.add("u-");

                } else {

                    tokens.add(String.valueOf(current));
                }

                index++;

                continue;
            }


            // PARENTHESES


            if (current == '(' || current == ')') {

                tokens.add(String.valueOf(current));

                index++;

                continue;
            }

            throw new CalculatorException(
                    "Invalid character"
            );
        }

        return tokens;
    }



    private List<String> convertToPostfix(List<String> tokens)
            throws CalculatorException {

        List<String> output = new ArrayList<>();

        Stack<String> operators = new Stack<>();

        for (String token : tokens) {


            // NUMBER


            if (isNumber(token)) {

                output.add(token);

            }


            // FUNCTION


            else if (token.equals("sqrt")) {

                operators.push(token);
            }


            // OPEN PARENTHESIS


            else if (token.equals("(")) {

                operators.push(token);
            }


            // CLOSE PARENTHESIS


            else if (token.equals(")")) {

                while (!operators.isEmpty()
                        && !operators.peek().equals("(")) {

                    output.add(
                            operators.pop()
                    );
                }

                if (operators.isEmpty()) {

                    throw new CalculatorException(
                            "Mismatched parentheses"
                    );
                }

                // Remove "("
                operators.pop();

                // If sqrt is immediately before "("
                if (!operators.isEmpty()
                        && operators.peek().equals("sqrt")) {

                    output.add(
                            operators.pop()
                    );
                }
            }


            // OPERATOR


            else if (isOperator(token)) {

                while (!operators.isEmpty()
                        && isOperator(operators.peek())
                        && (
                        precedence(operators.peek())
                                > precedence(token)

                                ||

                                (
                                        precedence(operators.peek())
                                                == precedence(token)
                                                && !isRightAssociative(token)
                                )
                )) {

                    output.add(
                            operators.pop()
                    );
                }

                operators.push(token);
            }
        }

        // Empty remaining operators
        while (!operators.isEmpty()) {

            String operator = operators.pop();

            if (operator.equals("(")
                    || operator.equals(")")) {

                throw new CalculatorException(
                        "Mismatched parentheses"
                );
            }

            output.add(operator);
        }

        return output;
    }


    // POSTFIX EVALUATION


    private BigDecimal evaluatePostfix(List<String> postfix)
            throws CalculatorException {

        Stack<BigDecimal> values = new Stack<>();

        for (String token : postfix) {


            // NUMBER

            if (isNumber(token)) {

                values.push(
                        new BigDecimal(
                                token,
                                MATH_CONTEXT
                        )
                );

                continue;
            }

            // SQUARE ROOT


            if (token.equals("sqrt")) {

                if (values.isEmpty()) {

                    throw new CalculatorException(
                            "Invalid square root"
                    );
                }

                BigDecimal value = values.pop();

                if (value.compareTo(BigDecimal.ZERO) < 0) {

                    throw new CalculatorException(
                            "Invalid square root"
                    );
                }

                double squareRoot =
                        Math.sqrt(value.doubleValue());

                values.push(
                        new BigDecimal(
                                Double.toString(squareRoot),
                                MATH_CONTEXT
                        )
                );

                continue;
            }

            // UNARY MINUS


            if (token.equals("u-")) {

                if (values.isEmpty()) {

                    throw new CalculatorException(
                            "Invalid expression"
                    );
                }

                BigDecimal value = values.pop();

                values.push(
                        value.negate(MATH_CONTEXT)
                );

                continue;
            }

            // BINARY OPERATORS


            if (values.size() < 2) {

                throw new CalculatorException(
                        "Invalid expression"
                );
            }

            BigDecimal right = values.pop();

            BigDecimal left = values.pop();

            BigDecimal result;

            switch (token) {

                case "+":

                    result = left.add(
                            right,
                            MATH_CONTEXT
                    );

                    break;

                case "-":

                    result = left.subtract(
                            right,
                            MATH_CONTEXT
                    );

                    break;

                case "*":

                    result = left.multiply(
                            right,
                            MATH_CONTEXT
                    );

                    break;

                case "/":

                    if (right.compareTo(
                            BigDecimal.ZERO
                    ) == 0) {

                        throw new CalculatorException(
                                "Division by zero"
                        );
                    }

                    result = left.divide(
                            right,
                            MATH_CONTEXT
                    );

                    break;

                case "%":

                    result = left
                            .multiply(
                                    right,
                                    MATH_CONTEXT
                            )
                            .divide(
                                    new BigDecimal("100"),
                                    MATH_CONTEXT
                            );

                    break;

                default:

                    throw new CalculatorException(
                            "Unknown operator"
                    );
            }

            values.push(result);
        }

        // Exactly one value must remain
        if (values.size() != 1) {

            throw new CalculatorException(
                    "Invalid expression"
            );
        }

        return values.pop();
    }


    // HELPER METHODS

    private boolean isNumber(String token) {

        try {

            new BigDecimal(token);

            return true;

        } catch (Exception exception) {

            return false;
        }
    }

    private boolean isOperator(String token) {

        return token.equals("+")
                || token.equals("-")
                || token.equals("*")
                || token.equals("/")
                || token.equals("%")
                || token.equals("u-");
    }

    private int precedence(String operator) {

        switch (operator) {

            case "+":
            case "-":
                return 1;

            case "*":
            case "/":
            case "%":
                return 2;

            case "u-":
                return 3;

            default:
                return 0;
        }
    }

    private boolean isRightAssociative(String operator) {

        return operator.equals("u-");
    }

    // RESULT FORMATTING


    private String formatResult(BigDecimal result) {

        result = result.stripTrailingZeros();

        if (result.scale() < 0) {

            result = result.setScale(0);
        }

        return result.toPlainString();
    }


    // CUSTOM EXCEPTION

    public static class CalculatorException
            extends Exception {

        public CalculatorException(String message) {

            super(message);
        }
    }
}