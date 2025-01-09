package com.app.qlchitieu;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.app.qlchitieu.adapters.DanhMucAdapter;
import com.app.qlchitieu.db.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private EditText inputTieuDe, inputNgayThu, inputKhoanTien;
    private Button btnAddConfirm, btnBack, btnDate;
    private TextView addTitle;
    private AutoCompleteTextView inputDanhMuc;
    private JSONArray danhMucArray;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);

        initializeViews();
        setupTitle();
        setupDatePicker();
        setupDanhMucDropdown();
        setupButtonListeners();
    }

    private void initializeViews() {
        addTitle = findViewById(R.id.addTitle);
        inputTieuDe = findViewById(R.id.inputTieuDe);
        inputNgayThu = findViewById(R.id.inputNgayThuChi);
        inputKhoanTien = findViewById(R.id.inputKhoanTien);
        inputDanhMuc = findViewById(R.id.inputDanhMuc);
        btnAddConfirm = findViewById(R.id.btnAddConfirm);
        btnBack = findViewById(R.id.btnBack);
        btnDate = findViewById(R.id.btnDate);
    }

    private void setupTitle() {
        String type = getIntent().getStringExtra("type");
        addTitle.setText(String.format("Thêm %s", type));
    }

    private void setupDatePicker() {
        btnDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            @SuppressLint("DefaultLocale") DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, (datePicker, i, i1, i2) ->
                    inputNgayThu.setText(String.format("%02d-%02d-%d", i2, i1 + 1, i)), year, month, day);
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

    private void setupButtonListeners() {
        btnAddConfirm.setOnClickListener(view -> {
            if (validateInputs()) {
                addDataToDatabase();
                startActivity(new Intent(AddActivity.this, MainActivity.class));
                finish();
            }
        });

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(AddActivity.this, MainActivity.class));
            finish();
        });
    }

    private boolean validateInputs() {
        if (inputTieuDe.getText().toString().trim().isEmpty() ||
                inputNgayThu.getText().toString().trim().isEmpty() ||
                inputKhoanTien.getText().toString().trim().isEmpty() ||
                inputDanhMuc.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Integer.parseInt(inputKhoanTien.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Khoản tiền phải là số", Toast.LENGTH_SHORT).show();
            return false;
        }

        String date = inputNgayThu.getText().toString().trim();
        if (!date.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            Toast.makeText(this, "Ngày thu chi phải đúng định dạng dd-MM-yyyy", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addDataToDatabase() {
        DatabaseHelper myDb = new DatabaseHelper(AddActivity.this);

        String type = getIntent().getStringExtra("type");
        myDb.addChiTieu(
                inputTieuDe.getText().toString().trim(),
                inputNgayThu.getText().toString().trim(),
                Integer.parseInt(inputKhoanTien.getText().toString().trim()),
                type,
                inputDanhMuc.getText().toString().trim()
        );

        Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
