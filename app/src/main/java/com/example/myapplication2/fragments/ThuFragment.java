package com.example.myapplication2.fragments;

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
import android.widget.Toast;

import com.example.myapplication2.AddActivity;
import com.example.myapplication2.db.DatabaseHelper;
import com.example.myapplication2.R;
import com.example.myapplication2.adapters.ThuChiAdapter;
import com.example.myapplication2.UpdateActivity;
import com.example.myapplication2.models.ThuChi;

import java.util.ArrayList;
import java.util.List;

public class ThuFragment extends Fragment {
    Button btnAdd;
    DatabaseHelper myDb;
    String type = "thu";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thu, container, false);

        myDb = new DatabaseHelper(getActivity());
        List<ThuChi> thuChiList = new ArrayList<>();
        displayData(thuChiList);

        ThuChiAdapter adapter = new ThuChiAdapter(getActivity(), thuChiList, thuChi -> {
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

        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        return view;
    }

    void displayData(List<ThuChi> thuChiList) {
        Cursor cursor = myDb.getAll(type);

        if (cursor == null || cursor.getCount() == 0) {
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

        Toast.makeText(getActivity(), "Dữ liệu đã được tải", Toast.LENGTH_SHORT).show();

        cursor.close();
    }
}
