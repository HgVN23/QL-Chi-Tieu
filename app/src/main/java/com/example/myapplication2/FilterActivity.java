package com.example.myapplication2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication2.adapters.DanhMucAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    AutoCompleteTextView inputDanhMuc;
    EditText inputNgayThu;
    Button btnFilterConfirm, btnBackFilter, btnCancelFilter, btnDateFilter;
    private JSONArray danhMucArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        inputDanhMuc = findViewById(R.id.inputDanhMuc);
        inputNgayThu = findViewById(R.id.inputNgayThuChiFilter);
        btnDateFilter = findViewById(R.id.btnDateFilter);

        setupDanhMucDropdown();
        setupDatePicker();

        String selectedCategory = getIntent().getStringExtra("selectedCategory");
        if (selectedCategory != null) {
            inputDanhMuc.setText(selectedCategory);
        }
        String selectedDate = getIntent().getStringExtra("selectedDate");
        if (selectedDate != null) {
            inputNgayThu.setText(selectedDate);
        }

        btnFilterConfirm = findViewById(R.id.btnFilterConfirm);
        btnFilterConfirm.setOnClickListener(v -> {
            String chosenCategory = inputDanhMuc.getText().toString();
            String chosenDate = inputNgayThu.getText().toString();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedCategory", chosenCategory);
            resultIntent.putExtra("selectedDate", chosenDate);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

        btnBackFilter = findViewById(R.id.btnBackFilter);
        btnBackFilter.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        btnCancelFilter = findViewById(R.id.btnCancelFilter);
        btnCancelFilter.setOnClickListener(v -> {
            inputDanhMuc.setText("");
            inputNgayThu.setText("");
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedCategory", "");
            resultIntent.putExtra("selectedDate", "");
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    private void setupDatePicker() {
        btnDateFilter.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();

            String currentDate = inputNgayThu.getText().toString().trim();
            if (!currentDate.isEmpty()) {
                try {
                    String[] dateParts = currentDate.split("-");
                    int month = Integer.parseInt(dateParts[0]) - 1;
                    int year = Integer.parseInt(dateParts[1]);

                    calendar.set(year, month, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(FilterActivity.this, (datePicker, i, i1, i2) ->
                    inputNgayThu.setText(String.format("%02d-%04d", i1 + 1, i)), year, month, day);
            dialog.show();
        });
    }

    private void setupDanhMucDropdown() {
        JSONLoader jsonLoader = new JSONLoader(this);
        danhMucArray = jsonLoader.loadJsonFromRaw(R.raw.danh_muc_list);

        DanhMucAdapter adapter = new DanhMucAdapter(this, danhMucArray);
        inputDanhMuc.setAdapter(adapter);

        inputDanhMuc.setOnItemClickListener((parent, view, position, id) -> {
            JSONObject selectedItem = danhMucArray.optJSONObject(position);
            if (selectedItem != null) {
                String selectedText = selectedItem.optString("text");
                inputDanhMuc.setText(selectedText);
            }
        });
    }
}
