package com.app.qlchitieu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

public class UpdateActivity extends AppCompatActivity {
    private EditText inputTieuDe, inputNgayThu, inputKhoanTien;
    private TextView updateTitle;
    private AutoCompleteTextView inputDanhMuc;
    private Button btnUpdateConfirm, btnBack, btnDate, btnDelete;

    private int id;
    private String type;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);

        initializeViews();
        retrieveAndSetData();
        setupDatePicker();
        setupDanhMucDropdown();
        setupButtonListeners();
    }

    private void initializeViews() {
        updateTitle = findViewById(R.id.updateTitle);
        inputTieuDe = findViewById(R.id.inputTieuDeUpdate);
        inputNgayThu = findViewById(R.id.inputNgayThuChiUpdate);
        inputKhoanTien = findViewById(R.id.inputKhoanTienUpdate);
        inputDanhMuc = findViewById(R.id.inputDanhMucUpdate);
        btnUpdateConfirm = findViewById(R.id.btnUpdateConfirm);
        btnBack = findViewById(R.id.btnBackUpdate);
        btnDate = findViewById(R.id.btnDateUpdate);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void retrieveAndSetData() {
        Intent intent = getIntent();

        id = intent.getIntExtra("id", -1);
        type = intent.getStringExtra("type");

        updateTitle.setText(String.format("Cập nhật %s", type));
        inputTieuDe.setText(intent.getStringExtra("title"));
        inputNgayThu.setText(intent.getStringExtra("date"));
        inputKhoanTien.setText(String.valueOf(intent.getIntExtra("payment", 0)));
        inputDanhMuc.setText(intent.getStringExtra("category"));
    }

    private void setupDatePicker() {
        btnDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();

            String currentDate = inputNgayThu.getText().toString().trim();
            if (!currentDate.isEmpty()) {
                try {
                    String[] dateParts = currentDate.split("-");
                    int day = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]) - 1;
                    int year = Integer.parseInt(dateParts[2]);

                    calendar.set(year, month, day);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(UpdateActivity.this, (datePicker, i, i1, i2) ->
                    inputNgayThu.setText(String.format("%02d-%02d-%04d", i2, i1 + 1, i)), year, month, day);
            dialog.show();
        });
    }


    private void setupDanhMucDropdown() {
        JSONLoader jsonLoader = new JSONLoader(this);
        JSONArray danhMucArray = jsonLoader.loadJsonFromRaw(R.raw.danh_muc_list);

        DanhMucAdapter adapter = new DanhMucAdapter(this, danhMucArray);
        inputDanhMuc.setAdapter(adapter);

        inputDanhMuc.setOnItemClickListener((parent, view, position, id) -> {
            JSONObject selectedItem = danhMucArray.optJSONObject(position);
            if (selectedItem != null) {
                inputDanhMuc.setText(selectedItem.optString("text"));
            }
        });
    }

    private void setupButtonListeners() {
        btnUpdateConfirm.setOnClickListener(view -> {
            if (validateInputs()) {
                updateDataInDatabase();
            }
        });

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(UpdateActivity.this, MainActivity.class));
            finish();
        });

        btnDelete.setOnClickListener(view -> {
            deleteDataFromDatabase();
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

    private void updateDataInDatabase() {
        DatabaseHelper myDb = new DatabaseHelper(this);

        myDb.updateChiTieu(
                id,
                inputTieuDe.getText().toString().trim(),
                inputNgayThu.getText().toString().trim(),
                Integer.parseInt(inputKhoanTien.getText().toString().trim()),
                type,
                inputDanhMuc.getText().toString().trim()
        );

        Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void deleteDataFromDatabase() {
        DatabaseHelper myDb = new DatabaseHelper(this);


        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    myDb.deleteChiTieu(id);
                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
