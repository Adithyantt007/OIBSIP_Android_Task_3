package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private boolean isDegreeMode = true;
    private StringBuilder input = new StringBuilder();
    TextView tx ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.display);

        if (savedInstanceState != null) {
            input.append(savedInstanceState.getString("val", ""));
            display.setText(input.length() == 0 ? "0" : input.toString());
            isDegreeMode = savedInstanceState.getBoolean("mode", true);
        }
    }


    public void onModeClick(View v) {
        tx=findViewById(R.id.modeIndicator);
        String cmd = ((Button) v).getText().toString();
        if (cmd.equalsIgnoreCase("DEG")) {
            isDegreeMode = true;
            tx.setText("DEG");

        } else if (cmd.equalsIgnoreCase("RAD")) {
            isDegreeMode = false;
            tx.setText("RAD");

        }

    }

    public void onDelete(View v) {
        if (input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
            display.setText(input.length() == 0 ? "0" : input.toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("val", input.toString());
        outState.putBoolean("mode", isDegreeMode);
    }

    private long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Number must be non-negative.");
        long result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }

    Operator factorialOperator = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {
        @Override
        public double apply(double... args) {
            final int arg = (int) args[0];
            if ((double) arg != args[0]) {
                throw new IllegalArgumentException("Operand for factorial must be an integer.");
            }
            return factorial(arg);
        }
    };

    public void onBtnClick(View v) {
        String cmd = ((Button) v).getText().toString();

        if (cmd.equals(".") && input.toString().contains(".")) {
            String[] parts = input.toString().split("[+\\-*/]");
            if (parts.length > 0 && parts[parts.length - 1].contains(".")) return;
        }

        if (cmd.equals("MOD")) {
            input.append("%");
        } else if (cmd.matches("sin|cos|tan|log|ln|sqrt")) {
            input.append(cmd).append("(");
        } else {
            input.append(cmd);
        }
        display.setText(input.toString());
    }

    public void onClear(View v) {
        input.setLength(0);
        display.setText("0");
    }

    public void onEqual(View v) {
        if (input.length() == 0) return;

        try {
            String originalExpr = input.toString();
            boolean isTrig = originalExpr.matches(".*(sin|cos|tan).*");
            boolean wasPercentageUsed = originalExpr.contains("%");

            String expr = originalExpr;
            if (expr.contains("log")) {
                expr = expr.replace("log", "log10");
            }



            // 1. Percentage Logic
            expr = expr.replaceAll("([0-9.]+%)", "($1)").replace("%", "/100");

            // 2. Trig Conversion
            if (isDegreeMode) {
                expr = expr.replace("sin(", "sin((pi/180)*");
                expr = expr.replace("cos(", "cos((pi/180)*");
                expr = expr.replace("tan(", "tan((pi/180)*");
            }

            Expression e = new ExpressionBuilder(expr)
                    .operator(factorialOperator)
                    .build();

            double res = e.evaluate();
            input.setLength(0);

            // 3. Smart Formatting
            if (res == (long) res) {
                // It's a whole number (e.g. 5.0 -> 5)
                input.append((long) res);
            } else if (isTrig || wasPercentageUsed) {
                // Limit to 2 decimals for Trig and Percent as requested
                input.append(String.format("%.2f", res));
            } else {
                // For normal division (e.g. 10/4), show only necessary decimals
                // This prevents "2.500000" and shows "2.5" instead.
                String formatted = String.format("%.8f", res)
                        .replaceAll("0*$", "")     // Remove trailing zeros
                        .replaceAll("\\.$", "");   // Remove trailing dot if no decimals left
                input.append(formatted);
            }

            display.setText(input.toString());

        } catch (Exception e) {
            display.setText("Error");
            input.setLength(0);
        }
    }
}