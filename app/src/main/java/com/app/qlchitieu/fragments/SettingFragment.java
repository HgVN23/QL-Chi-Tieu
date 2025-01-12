package com.app.qlchitieu.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.app.qlchitieu.MainActivity;
import com.app.qlchitieu.R;
import com.app.qlchitieu.SplashScreenActivity;
import com.app.qlchitieu.UpdatePinActivity;
import com.app.qlchitieu.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment {
    private Button btnUpdatePIN, btnCleanData, btnCleanAll, btnTest;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        btnUpdatePIN = view.findViewById(R.id.btnUpdatePIN);
        btnUpdatePIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdatePinActivity.class);
                startActivity(intent);
            }
        });

        btnCleanData = view.findViewById(R.id.btnCleanData);
        btnCleanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Xóa Dữ Liệu")
                        .setMessage("Bạn có chắc chắn muốn xóa tất cả dữ liệu trong Quản lý?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseHelper.recreateThuChi();
                                Toast.makeText(getContext(), "Dữ liệu đã được xóa!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);

                                if (getActivity() != null) {
                                    getActivity().finish();
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        btnCleanAll = view.findViewById(R.id.btnCleanAll);
        btnCleanAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Lưu ý")
                        .setMessage("Nếu tiếp tục, toàn bộ dữ liệu đã trong Quản lý cũng như mã PIN sẽ bị xóa và ứng dụng sẽ đặt lại. Bạn có chắc chắn không?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                databaseHelper.onUpgrade(db, 0, 0);

                                Intent intent = new Intent(getContext(), SplashScreenActivity.class);
                                startActivity(intent);

                                if (getActivity() != null) {
                                    getActivity().finish();
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        btnTest = view.findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Tạo dữ liệu mẫu")
                        .setMessage("Đây là chức năng test, nếu ấn sẽ xóa toàn bộ dữ liệu và sử dụng dữ liệu mẫu")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseHelper.recreateThuChi();

                                List<String> categories = new ArrayList<>();
                                categories.add("Công việc");
                                categories.add("Tiết kiệm");
                                categories.add("Nước");
                                categories.add("Điện");
                                categories.add("Khác");

                                int payment = 50000;
                                for (String category : categories) {
                                    databaseHelper.addChiTieu("Test", "01-11-2024", payment, "thu", category);
                                    payment += 20000;
                                }

                                payment = 55000;
                                for (String category : categories) {
                                    databaseHelper.addChiTieu("Test", "01-12-2024", payment, "thu", category);
                                    payment += 30000;
                                }

                                payment = 100000;
                                for (String category : categories) {
                                    databaseHelper.addChiTieu("Test", "01-01-2025", payment, "thu", category);
                                    payment += 50000;
                                }

                                payment = 5000;
                                for (String category : categories) {
                                    databaseHelper.addChiTieu("Test", "01-11-2024", payment, "chi", category);
                                    payment += 3000;
                                }

                                payment = 10000;
                                for (String category : categories) {
                                    databaseHelper.addChiTieu("Test", "01-12-2024", payment, "chi", category);
                                    payment += 5000;
                                }

                                payment = 20000;
                                for (String category : categories) {
                                    databaseHelper.addChiTieu("Test", "01-01-2025", payment, "chi", category);
                                    payment += 4000;
                                }

                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);

                                if (getActivity() != null) {
                                    getActivity().finish();
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        return view;
    }
}
