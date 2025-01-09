package com.example.myapplication2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication2.db.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    EditText inputPIN1, inputPIN2, inputPIN3, inputPIN4, inputPIN5, inputPIN6;
    Button btnLogin, btnShowPIN;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        if (!isSettingAvailable()) {
            Intent intent = new Intent(LoginActivity.this, CreatePinActivity.class);
            startActivity(intent);
            finish();
        }

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

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = inputPIN1.getText().toString()
                        + inputPIN2.getText().toString()
                        + inputPIN3.getText().toString()
                        + inputPIN4.getText().toString()
                        + inputPIN5.getText().toString()
                        + inputPIN6.getText().toString();

                if (pin.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Mã PIN phải có đủ 6 số", Toast.LENGTH_SHORT).show();
                } else if (isValidPIN(pin)) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Mã PIN không chính xác", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShowPIN = findViewById(R.id.btnShowPIN);
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

    }

    private boolean isSettingAvailable() {
        Cursor cursor = dbHelper.getSetting();
        boolean hasData = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return hasData;
    }

    private boolean isValidPIN(String pin) {
        Cursor cursor = dbHelper.getPIN(pin);
        boolean isValid = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return isValid;
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
