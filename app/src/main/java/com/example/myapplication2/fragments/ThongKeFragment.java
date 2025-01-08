package com.example.myapplication2.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication2.R;
import com.example.myapplication2.db.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ThongKeFragment extends Fragment {
    private TextView sumThu, sumChi, sumAll, dateNow, dateSoSanh, hanMuc;
    private Button btnDateNow, btnDateSS, btnSetup;
    private DatabaseHelper databaseHelper;
    private int CanhBao;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_ke, container, false);

        sumThu = view.findViewById(R.id.sumThu);
        sumChi = view.findViewById(R.id.sumChi);
        sumAll = view.findViewById(R.id.sumAll);
        btnDateNow = view.findViewById(R.id.btnDateNow);
        dateNow = view.findViewById(R.id.dateNow);
        btnDateSS = view.findViewById(R.id.btnDateSS);
        dateSoSanh = view.findViewById(R.id.dateSoSanh);
        btnSetup = view.findViewById(R.id.btnSetup);
        hanMuc = view.findViewById(R.id.hanMuc);

        databaseHelper = new DatabaseHelper(getContext());

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy", Locale.getDefault());

        String currentDate = dateFormat.format(calendar.getTime());
        dateNow.setText(currentDate);

        calendar.add(Calendar.MONTH, -1);
        String previousDate = dateFormat.format(calendar.getTime());
        dateSoSanh.setText(previousDate);

        setupHanMuc(currentDate);
        displaySums(currentDate);
        setupDatePicker();
        setupDatePickerForSoSanh();

        return view;
    }

    private void displaySums(String date) {
        int thu = databaseHelper.getSumByType("thu", date);
        int chi = databaseHelper.getSumByType("chi", date);
        int phanDu = thu - chi;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);

        String formattedThu = decimalFormat.format(thu);
        String formattedChi = decimalFormat.format(chi);
        String formattedAll = decimalFormat.format(phanDu);

        sumThu.setText(String.format("Tổng thu: %s VND", formattedThu));
        sumChi.setText(String.format("Tổng chi: %s VND", formattedChi));
        sumAll.setText(String.format("Phần dư: %s VND", formattedAll));
        hanMuc.setText(String.format("Cảnh báo hạn mức: %s", ((CanhBao + phanDu) > 0 ? "Ổn" : "Vượt mức")));
    }

    private void setupDatePicker() {
        btnDateNow.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();

            String currentDate = dateNow.getText().toString().trim();
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

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, day) -> {
                @SuppressLint("DefaultLocale")
                String selectedDate = String.format("%02d-%04d", selectedMonth + 1, selectedYear);
                dateNow.setText(selectedDate);

                displaySums(selectedDate);
            }, year, month, calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
    }

    private void setupDatePickerForSoSanh() {
        btnDateSS.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();

            String currentDate = dateSoSanh.getText().toString().trim();
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

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, day) -> {
                @SuppressLint("DefaultLocale")
                String selectedDate = String.format("%02d-%04d", selectedMonth + 1, selectedYear);
                dateSoSanh.setText(selectedDate);

                displaySums(selectedDate);
            }, year, month, calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
    }

    private void setupHanMuc(String date) {
        btnSetup.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_han_muc, null);
            bottomSheetDialog.setContentView(dialogView);

            EditText editHanMuc = dialogView.findViewById(R.id.editHanMuc);
            Button btnSave = dialogView.findViewById(R.id.btnSave);
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);

            Cursor cursor = databaseHelper.getHanMuc();
            if (cursor != null && cursor.moveToFirst()) {
                int hanMuc = cursor.getInt(0);
                CanhBao = hanMuc;
                editHanMuc.setText(String.valueOf(hanMuc));
                cursor.close();
            }

            btnSave.setOnClickListener(v -> {
                String newHanMucStr = editHanMuc.getText().toString().trim();
                if (!newHanMucStr.isEmpty()) {
                    int newHanMuc = Integer.parseInt(newHanMucStr);
                    CanhBao = newHanMuc;
                    displaySums(date);
                    databaseHelper.updateHanMuc(newHanMuc);
                    bottomSheetDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Không để trống hạn mức", Toast.LENGTH_SHORT).show();
                }
            });

            btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

            bottomSheetDialog.show();
        });

    }
}
