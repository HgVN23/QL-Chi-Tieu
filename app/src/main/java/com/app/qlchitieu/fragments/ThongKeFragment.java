package com.app.qlchitieu.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.app.qlchitieu.R;
import com.app.qlchitieu.db.DatabaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ThongKeFragment extends Fragment {

    private TextView sumThu, sumChi, sumAll, dateNow, dateSoSanh, hanMuc;
    private Button btnDateNow, btnDateSS, btnSetup;
    private DatabaseHelper databaseHelper;
    private int CanhBao;
    private PieChart pieChartCateThu, pieChartCateChi;
    private ValueFormatter formatter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_ke, container, false);

        initializeViews(view);
        initializeDatabaseHelper();
        setupDefaultDates();
        setupHanMuc(getCurrentDate());
        displaySums(getCurrentDate());
        setupDatePickers();
        setupPieCharts();

        return view;
    }

    private void initializeViews(View view) {
        sumThu = view.findViewById(R.id.sumThu);
        sumChi = view.findViewById(R.id.sumChi);
        sumAll = view.findViewById(R.id.sumAll);
        btnDateNow = view.findViewById(R.id.btnDateNow);
        dateNow = view.findViewById(R.id.dateNow);
        btnDateSS = view.findViewById(R.id.btnDateSS);
        dateSoSanh = view.findViewById(R.id.dateSoSanh);
        btnSetup = view.findViewById(R.id.btnSetup);
        hanMuc = view.findViewById(R.id.hanMuc);
        pieChartCateThu = view.findViewById(R.id.pieChartCateThu);
        pieChartCateChi = view.findViewById(R.id.pieChartCateChi);
        formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return Math.round(value) + "%";
            }
        };
    }

    private void initializeDatabaseHelper() {
        databaseHelper = new DatabaseHelper(getContext());
    }

    private void setupDefaultDates() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy", Locale.getDefault());
        dateNow.setText(dateFormat.format(calendar.getTime()));

        calendar.add(Calendar.MONTH, -1);
        dateSoSanh.setText(dateFormat.format(calendar.getTime()));
    }

    private String getCurrentDate() {
        return dateNow.getText().toString().trim();
    }

    private void displaySums(String date) {
        int thu = databaseHelper.getSumByType("thu", date);
        int chi = databaseHelper.getSumByType("chi", date);
        int phanDu = thu - chi;

        DecimalFormat decimalFormat = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.getDefault()));
        decimalFormat.getDecimalFormatSymbols().setGroupingSeparator('.');

        sumThu.setText(String.format("Tổng thu: %s VND", decimalFormat.format(thu)));
        sumChi.setText(String.format("Tổng chi: %s VND", decimalFormat.format(chi)));
        sumAll.setText(String.format("Phần dư: %s VND", decimalFormat.format(phanDu)));
        hanMuc.setText(String.format("Cảnh báo hạn mức: %s", (CanhBao + phanDu > 0 ? "Ổn" : "Vượt mức")));
    }

    private void setupDatePickers() {
        setupDatePicker(btnDateNow, dateNow);
        setupDatePicker(btnDateSS, dateSoSanh);
    }

    @SuppressLint("DefaultLocale")
    private void setupDatePicker(Button button, TextView textView) {
        button.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            String currentDate = textView.getText().toString().trim();
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
            new DatePickerDialog(getActivity(), (datePicker, selectedYear, selectedMonth, day) -> {
                textView.setText(String.format("%02d-%04d", selectedMonth + 1, selectedYear));
                displaySums(textView.getText().toString().trim());
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                CanhBao = cursor.getInt(0);
                editHanMuc.setText(String.valueOf(CanhBao));
                cursor.close();
            }

            btnSave.setOnClickListener(v -> {
                String newHanMucStr = editHanMuc.getText().toString().trim();
                if (!newHanMucStr.isEmpty()) {
                    CanhBao = Integer.parseInt(newHanMucStr);
                    databaseHelper.updateHanMuc(CanhBao);
                    displaySums(date);
                    bottomSheetDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Không để trống hạn mức", Toast.LENGTH_SHORT).show();
                }
            });

            btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
            bottomSheetDialog.show();
        });
    }

    private void setupPieCharts() {
        setupPieChartCate(pieChartCateThu, "thu", getCurrentDate());
        setupPieChartCate(pieChartCateChi, "chi", getCurrentDate());
    }

    private void setupPieChartCate(PieChart pieChart, String type, String date) {
        Cursor cursor = databaseHelper.getSumByCategoryForMonth(type, date);
        List<PieEntry> entries = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("DanhMuc"));
                @SuppressLint("Range") int totalPayment = cursor.getInt(cursor.getColumnIndex("totalPayment"));
                entries.add(new PieEntry(totalPayment, category));
            } while (cursor.moveToNext());
            cursor.close();
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{
                Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                Color.CYAN, Color.MAGENTA, Color.GRAY, Color.DKGRAY, Color.LTGRAY,
                Color.parseColor("#FF2352"),
                Color.parseColor("#DD1244"),
                Color.parseColor("#FFA500"),
                Color.parseColor("#800080"),
                Color.parseColor("#00FF7F")
        });
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(14f);
        dataSet.setValueFormatter(formatter);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.setCenterText(String.format("Thống kê\ndanh mục\n%s", type));
        pieChart.setCenterTextSize(16f);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);

        Legend legend = pieChart.getLegend();
        legend.setTextSize(14f);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setWordWrapEnabled(true);

        pieChart.invalidate();
    }
}
