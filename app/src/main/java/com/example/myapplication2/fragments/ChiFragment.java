package com.example.myapplication2.fragments;

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

import com.example.myapplication2.AddActivity;
import com.example.myapplication2.FilterActivity;
import com.example.myapplication2.R;
import com.example.myapplication2.UpdateActivity;
import com.example.myapplication2.adapters.ThuChiAdapter;
import com.example.myapplication2.db.DatabaseHelper;
import com.example.myapplication2.models.ThuChi;

import java.util.ArrayList;
import java.util.List;

public class ChiFragment extends Fragment {
    Button btnAdd, btnFilter;
    DatabaseHelper myDb;
    String type = "chi";
    List<ThuChi> thuChiList;
    ThuChiAdapter adapter;
    private String selectedCategory, selectedDate;
    private TextView noDataTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi, container, false);

        myDb = new DatabaseHelper(getActivity());
        thuChiList = new ArrayList<>();
        noDataTextView = view.findViewById(R.id.noDataTextViewChi);
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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewChi);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnAdd = view.findViewById(R.id.btnAddChi);
        btnAdd.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
        });

        btnFilter = view.findViewById(R.id.btnFilterChi);
        btnFilter.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), FilterActivity.class);
            intent.putExtra("selectedCategory", selectedCategory);
            intent.putExtra("selectedDate", selectedDate);
            startActivityForResult(intent, 1);
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

        if (cursor == null || cursor.getCount() == 0) {
            thuChiList.clear();
            noDataTextView.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Dữ liệu rỗng", Toast.LENGTH_SHORT).show();
            return;
        }

        thuChiList.clear();

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

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        noDataTextView.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Dữ liệu đã được tải", Toast.LENGTH_SHORT).show();

        cursor.close();
    }

    @SuppressLint("NotifyDataSetChanged")
    void filterData(String category, String date) {
        Cursor cursor = myDb.getByFilter(type, category, date);

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "Không tìm thấy dữ liệu phù hợp", Toast.LENGTH_SHORT).show();
            thuChiList.clear();
            adapter.notifyDataSetChanged();
            noDataTextView.setVisibility(View.VISIBLE);
            return;
        }

        thuChiList.clear();

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

        adapter.notifyDataSetChanged();
        noDataTextView.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Dữ liệu đã được lọc", Toast.LENGTH_SHORT).show();

        cursor.close();
    }
}
