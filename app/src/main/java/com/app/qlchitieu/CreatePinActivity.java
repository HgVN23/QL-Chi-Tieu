package com.app.qlchitieu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.app.qlchitieu.db.DatabaseHelper;

public class CreatePinActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    EditText inputPIN1, inputPIN2, inputPIN3, inputPIN4, inputPIN5, inputPIN6;
    EditText inputCPIN1, inputCPIN2, inputCPIN3, inputCPIN4, inputCPIN5, inputCPIN6;
    Button btnConfirmPIN, btnShowPIN, btnShowCPIN;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_pin);

        dbHelper = new DatabaseHelper(this);

        inputPIN1 = findViewById(R.id.inputPIN1);
        inputPIN2 = findViewById(R.id.inputPIN2);
        inputPIN3 = findViewById(R.id.inputPIN3);
        inputPIN4 = findViewById(R.id.inputPIN4);
        inputPIN5 = findViewById(R.id.inputPIN5);
        inputPIN6 = findViewById(R.id.inputPIN6);
        inputPIN1.addTextChangedListener(createTextWatcher(inputPIN2));
        inputPIN2.addTextChangedListener(createTextWatcher(inputPIN3));
        inputPIN3.addTextChangedListener(createTextWatcher(inputPIN4));
        inputPIN4.addTextChangedListener(createTextWatcher(inputPIN5));
        inputPIN5.addTextChangedListener(createTextWatcher(inputPIN6));

        inputCPIN1 = findViewById(R.id.inputCPIN1);
        inputCPIN2 = findViewById(R.id.inputCPIN2);
        inputCPIN3 = findViewById(R.id.inputCPIN3);
        inputCPIN4 = findViewById(R.id.inputCPIN4);
        inputCPIN5 = findViewById(R.id.inputCPIN5);
        inputCPIN6 = findViewById(R.id.inputCPIN6);
        inputCPIN1.addTextChangedListener(createTextWatcher(inputCPIN2));
        inputCPIN2.addTextChangedListener(createTextWatcher(inputCPIN3));
        inputCPIN3.addTextChangedListener(createTextWatcher(inputCPIN4));
        inputCPIN4.addTextChangedListener(createTextWatcher(inputCPIN5));
        inputCPIN5.addTextChangedListener(createTextWatcher(inputCPIN6));

        btnConfirmPIN = findViewById(R.id.btnConfirmPIN);
        btnConfirmPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                if (pin.length() < 6 || confirmPin.length() < 6) {
                    Toast.makeText(CreatePinActivity.this, "Mã PIN phải có đủ 6 số", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pin.equals(confirmPin)) {
                    Toast.makeText(CreatePinActivity.this, "Mã PIN không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.addSetting(pin);
                Toast.makeText(CreatePinActivity.this, "Mã PIN được lưu thành công", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CreatePinActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnShowPIN = findViewById(R.id.btnShowPIN2);
        btnShowPIN.setOnClickListener(new View.OnClickListener() {
            private boolean isPinVisible = false;

            @Override
            public void onClick(View view) {
                isPinVisible = !isPinVisible;

                if (isPinVisible) {
                    inputPIN1.setTransformationMethod(null);
                    inputPIN2.setTransformationMethod(null);
                    inputPIN3.setTransformationMethod(null);
                    inputPIN4.setTransformationMethod(null);
                    inputPIN5.setTransformationMethod(null);
                    inputPIN6.setTransformationMethod(null);
                    btnShowPIN.setBackgroundResource(R.drawable.hide);
                } else {
                    inputPIN1.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    inputPIN2.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    inputPIN3.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    inputPIN4.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    inputPIN5.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    inputPIN6.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    btnShowPIN.setBackgroundResource(R.drawable.show);
                }
            }
        });

        btnShowCPIN = findViewById(R.id.btnShowPIN3);
        btnShowCPIN.setOnClickListener(new View.OnClickListener() {
            private boolean isPinVisible = false;

            @Override
            public void onClick(View view) {
                isPinVisible = !isPinVisible;

                if (isPinVisible) {
                    inputCPIN1.setTransformationMethod(null);
                    inputCPIN2.setTransformationMethod(null);
                    inputCPIN3.setTransformationMethod(null);
                    inputCPIN4.setTransformationMethod(null);
                    inputCPIN5.setTransformationMethod(null);
                    inputCPIN6.setTransformationMethod(null);
                    btnShowCPIN.setBackgroundResource(R.drawable.hide);
                } else {
                    inputCPIN1.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    inputCPIN2.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    inputCPIN3.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    inputCPIN4.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    inputCPIN5.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    inputCPIN6.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    btnShowCPIN.setBackgroundResource(R.drawable.show);
                }
            }
        });
    }

    private android.text.TextWatcher createTextWatcher(final EditText nextEditText) {
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
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        };
    }
}
