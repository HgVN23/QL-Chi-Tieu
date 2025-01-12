package com.app.qlchitieu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.qlchitieu.db.DatabaseHelper;

public class UpdatePinActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    EditText inputOPIN1, inputOPIN2, inputOPIN3, inputOPIN4, inputOPIN5, inputOPIN6;
    EditText inputPIN1, inputPIN2, inputPIN3, inputPIN4, inputPIN5, inputPIN6;
    EditText inputCPIN1, inputCPIN2, inputCPIN3, inputCPIN4, inputCPIN5, inputCPIN6;
    Button btnConfirmPIN, btnShowPIN, btnShowCPIN, btnShowOPIN, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_pin);

        dbHelper = new DatabaseHelper(this);

        inputOPIN1 = findViewById(R.id.inputOPIN1);
        inputOPIN2 = findViewById(R.id.inputOPIN2);
        inputOPIN3 = findViewById(R.id.inputOPIN3);
        inputOPIN4 = findViewById(R.id.inputOPIN4);
        inputOPIN5 = findViewById(R.id.inputOPIN5);
        inputOPIN6 = findViewById(R.id.inputOPIN6);
        inputOPIN1.addTextChangedListener(createTextWatcher(inputOPIN2, inputOPIN1));
        inputOPIN2.addTextChangedListener(createTextWatcher(inputOPIN3, inputOPIN1));
        inputOPIN3.addTextChangedListener(createTextWatcher(inputOPIN4, inputOPIN2));
        inputOPIN4.addTextChangedListener(createTextWatcher(inputOPIN5, inputOPIN3));
        inputOPIN5.addTextChangedListener(createTextWatcher(inputOPIN6, inputOPIN4));
        inputOPIN6.addTextChangedListener(createTextWatcher(inputOPIN6, inputOPIN5));

        inputPIN1 = findViewById(R.id.inputPIN1);
        inputPIN2 = findViewById(R.id.inputPIN2);
        inputPIN3 = findViewById(R.id.inputPIN3);
        inputPIN4 = findViewById(R.id.inputPIN4);
        inputPIN5 = findViewById(R.id.inputPIN5);
        inputPIN6 = findViewById(R.id.inputPIN6);
        inputPIN1.addTextChangedListener(createTextWatcher(inputPIN2, inputPIN1));
        inputPIN2.addTextChangedListener(createTextWatcher(inputPIN3, inputPIN1));
        inputPIN3.addTextChangedListener(createTextWatcher(inputPIN4, inputPIN2));
        inputPIN4.addTextChangedListener(createTextWatcher(inputPIN5, inputPIN3));
        inputPIN5.addTextChangedListener(createTextWatcher(inputPIN6, inputPIN4));
        inputPIN6.addTextChangedListener(createTextWatcher(inputPIN6, inputPIN5));

        inputCPIN1 = findViewById(R.id.inputCPIN1);
        inputCPIN2 = findViewById(R.id.inputCPIN2);
        inputCPIN3 = findViewById(R.id.inputCPIN3);
        inputCPIN4 = findViewById(R.id.inputCPIN4);
        inputCPIN5 = findViewById(R.id.inputCPIN5);
        inputCPIN6 = findViewById(R.id.inputCPIN6);
        inputCPIN1.addTextChangedListener(createTextWatcher(inputCPIN2, inputCPIN1));
        inputCPIN2.addTextChangedListener(createTextWatcher(inputCPIN3, inputCPIN1));
        inputCPIN3.addTextChangedListener(createTextWatcher(inputCPIN4, inputCPIN2));
        inputCPIN4.addTextChangedListener(createTextWatcher(inputCPIN5, inputCPIN3));
        inputCPIN5.addTextChangedListener(createTextWatcher(inputCPIN6, inputCPIN4));
        inputCPIN6.addTextChangedListener(createTextWatcher(inputCPIN6, inputCPIN5));

        btnBack = findViewById(R.id.btnBackP);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnShowPIN = findViewById(R.id.btnShowPIN2);
        btnShowCPIN = findViewById(R.id.btnShowPIN3);
        btnShowOPIN = findViewById(R.id.btnShowPIN4);

        setupToggleVisibilityButton(btnShowPIN, false, R.drawable.show, R.drawable.hide,
                inputPIN1, inputPIN2, inputPIN3, inputPIN4, inputPIN5, inputPIN6);
        setupToggleVisibilityButton(btnShowCPIN, false, R.drawable.show, R.drawable.hide,
                inputCPIN1, inputCPIN2, inputCPIN3, inputCPIN4, inputCPIN5, inputCPIN6);
        setupToggleVisibilityButton(btnShowOPIN, false, R.drawable.show, R.drawable.hide,
                inputOPIN1, inputOPIN2, inputOPIN3, inputOPIN4, inputOPIN5, inputOPIN6);

        btnConfirmPIN = findViewById(R.id.btnConfirmPIN);
        btnConfirmPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPin = inputOPIN1.getText().toString()
                        + inputOPIN2.getText().toString()
                        + inputOPIN3.getText().toString()
                        + inputOPIN4.getText().toString()
                        + inputOPIN5.getText().toString()
                        + inputOPIN6.getText().toString();
                String pin = inputPIN1.getText().toString()
                        + inputPIN2.getText().toString()
                        + inputPIN3.getText().toString()
                        + inputPIN4.getText().toString()
                        + inputPIN5.getText().toString()
                        + inputPIN6.getText().toString();
                String confirmPin = inputCPIN1.getText().toString()
                        + inputCPIN2.getText().toString()
                        + inputCPIN3.getText().toString()
                        + inputCPIN4.getText().toString()
                        + inputCPIN5.getText().toString()
                        + inputCPIN6.getText().toString();

                // Validate inputs
                if (pin.length() < 6 || confirmPin.length() < 6) {
                    Toast.makeText(UpdatePinActivity.this, "Mã PIN phải có đủ 6 số", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pin.equals(confirmPin)) {
                    Toast.makeText(UpdatePinActivity.this, "Mã PIN không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = dbHelper.getPIN(oldPin);
                if (cursor != null && cursor.moveToFirst()) {
                    dbHelper.updateSetting(pin);
                    Toast.makeText(UpdatePinActivity.this, "Mã PIN được cập nhật thành công", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(UpdatePinActivity.this, "Mã PIN cũ không đúng", Toast.LENGTH_SHORT).show();
                }

                if (cursor != null) {
                    cursor.close();
                }
            }
        });
    }

    private android.text.TextWatcher createTextWatcher(final EditText nextEditText, final EditText prevEditText) {
        return new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() == 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nextEditText.requestFocus();
                        }
                    }, 1);
                } else if (charSequence.length() == 0 && before == 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            prevEditText.requestFocus();
                        }
                    }, 1);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        };
    }

    private void togglePinVisibility(boolean isVisible, View... pinInputs) {
        for (View input : pinInputs) {
            if (input instanceof android.widget.EditText) {
                ((android.widget.EditText) input).setTransformationMethod(
                        isVisible ? null : new android.text.method.PasswordTransformationMethod()
                );
            }
        }
    }

    private void setupToggleVisibilityButton(Button button, boolean initialVisibility, int showIcon, int hideIcon, View... pinInputs) {
        button.setOnClickListener(new View.OnClickListener() {
            private boolean isPinVisible = initialVisibility;

            @Override
            public void onClick(View view) {
                isPinVisible = !isPinVisible;
                togglePinVisibility(isPinVisible, pinInputs);
                button.setBackgroundResource(isPinVisible ? hideIcon : showIcon);
            }
        });
    }
}