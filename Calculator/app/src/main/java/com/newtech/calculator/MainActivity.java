package com.newtech.calculator;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText inputValue;
    private String expression = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure your XML file is named activity_main.xml

        inputValue = findViewById(R.id.inputValue);

        int[] numberIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9
        };

        int[] operatorIds = {
                R.id.buttonAdd, R.id.buttonSubtract,
                R.id.buttonMultiply, R.id.buttonDivide,
                R.id.buttonDot
        };

        View.OnClickListener numberClickListener = view -> {
            Button b = (Button) view;
            expression += b.getText().toString();
            inputValue.setText(expression);
        };

        for (int id : numberIds) findViewById(id).setOnClickListener(numberClickListener);
        for (int id : operatorIds) findViewById(id).setOnClickListener(numberClickListener);

        findViewById(R.id.buttonClear).setOnClickListener(v -> {
            expression = "";
            inputValue.setText("");
        });

        findViewById(R.id.buttonDelete).setOnClickListener(v -> {
            if (!expression.isEmpty()) {
                expression = expression.substring(0, expression.length() - 1);
                inputValue.setText(expression);
            }
        });

        findViewById(R.id.buttonSquare).setOnClickListener(v -> {
            if (!expression.isEmpty()) {
                try {
                    double value = Double.parseDouble(expression);
                    expression = String.valueOf(value * value);
                    inputValue.setText(expression);
                } catch (Exception e) {
                    inputValue.setText("Error");
                }
            }
        });

        findViewById(R.id.buttonSqrt).setOnClickListener(v -> {
            if (!expression.isEmpty()) {
                try {
                    double value = Double.parseDouble(expression);
                    if (value >= 0) {
                        expression = String.valueOf(Math.sqrt(value));
                        inputValue.setText(expression);
                    } else {
                        inputValue.setText("Invalid");
                    }
                } catch (Exception e) {
                    inputValue.setText("Error");
                }
            }
        });

        findViewById(R.id.buttonEquals).setOnClickListener(v -> {
            try {
                double result = evaluate(expression);
                expression = String.valueOf(result);
                inputValue.setText(expression);
            } catch (Exception e) {
                inputValue.setText("Error");
            }
        });
    }

    private double evaluate(String expr) {
        return new org.mozilla.javascript.ContextFactory().call(cx -> {
            cx.setOptimizationLevel(-1);
            return ((Number) cx.evaluateString(cx.initStandardObjects(), expr, "JavaScript", 1, null)).doubleValue();
        });
    }
}

