package com.app.qlchitieu.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.qlchitieu.AddActivity;
import com.app.qlchitieu.FilterActivity;
import com.app.qlchitieu.db.DatabaseHelper;
import com.app.qlchitieu.R;
import com.app.qlchitieu.adapters.ThuChiAdapter;
import com.app.qlchitieu.UpdateActivity;
import com.app.qlchitieu.models.ThuChi;

import java.util.ArrayList;
import java.util.List;

public class ThuFragment extends Fragment {
    Button btnAdd, btnFilter, btnReload;
    DatabaseHelper myDb;
    String type = "thu";
    List<ThuChi> thuChiList;
    ThuChiAdapter adapter;
    private String selectedCategory, selectedDate;
    private TextView noDataTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thu, container, false);

        myDb = new DatabaseHelper(getActivity());
        thuChiList = new ArrayList<>();
        noDataTextView = view.findViewById(R.id.noDataTextView);
        displayData();

        adapter = new ThuChiAdapter(getActivity(), thuChiList, thuChi -> {
            Intent intent = new Intent(getActivity(), UpdateActivity.class);
            intent.putExtra("id", thuChi.getId());
            intent.putExtra("title", thuChi.getTitle());
            intent.putExtra("date", thuChi.getDate());
            intent.putExtra("payment", thuChi.getPayment());
            intent.putExtra("category", thuChi.getCategory());
            intent.putExtra("type", thuChi.getType());
            startActivity(intent);
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnAdd = view.findViewById(R.id.btnAddThu);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        btnFilter = view.findViewById(R.id.btnFilterThu);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.putExtra("selectedCategory", selectedCategory);
                intent.putExtra("selectedDate", selectedDate);
                startActivityForResult(intent, 1);
            }
        });

        btnReload = view.findViewById(R.id.btnReloadThu);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayData();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            selectedCategory = data.getStringExtra("selectedCategory");
            selectedDate = data.getStringExtra("selectedDate");
            filterData(selectedCategory, selectedDate);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    void displayData() {
        Cursor cursor = myDb.getAll(type);

        // Clear the list before loading new data
        thuChiList.clear();

        if (cursor == null || cursor.getCount() == 0) {
            // No data found, display the no data message
            noDataTextView.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Dữ liệu rỗng", Toast.LENGTH_SHORT).show();
        } else {
            // Data found, populate the list
            while (cursor.moveToNext()) {
                thuChiList.add(new ThuChi(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            }
            // Hide the "No Data" message and update RecyclerView
            noDataTextView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Dữ liệu đã được tải", Toast.LENGTH_SHORT).show();
        }

        // Notify the adapter that the data has changed
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        // Close the cursor after use
        cursor.close();
    }

    @SuppressLint("NotifyDataSetChanged")
    void filterData(String category, String date) {
        Cursor cursor = myDb.getByFilter(type, category, date);

        // Clear the list before filtering data
        thuChiList.clear();

        if (cursor == null || cursor.getCount() == 0) {
            // No data found after filtering, display the no data message
            Toast.makeText(getActivity(), "Không tìm thấy dữ liệu phù hợp", Toast.LENGTH_SHORT).show();
            noDataTextView.setVisibility(View.VISIBLE);
        } else {
            // Data found after filtering, populate the list
            while (cursor.moveToNext()) {
                thuChiList.add(new ThuChi(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            }
            // Hide the "No Data" message and update RecyclerView
            noDataTextView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Dữ liệu đã được lọc", Toast.LENGTH_SHORT).show();
        }

        // Notify the adapter that the data has changed
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        // Close the cursor after use
        cursor.close();
    }


}
