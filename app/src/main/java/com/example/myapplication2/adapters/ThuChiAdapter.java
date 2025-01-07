package com.example.myapplication2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication2.R;
import com.example.myapplication2.models.ThuChi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;

public class ThuChiAdapter extends RecyclerView.Adapter<ThuChiAdapter.MyViewHolder> {

    private final Context context;
    private final List<ThuChi> thuChiList;
    private final HashMap<String, Integer> categoryIconMap;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ThuChi thuChi);
    }

    public ThuChiAdapter(Context context, List<ThuChi> thuChiList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.thuChiList = thuChiList;
        this.onItemClickListener = onItemClickListener;
        this.categoryIconMap = loadCategoryIcons();
    }

    @NonNull
    @Override
    public ThuChiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_item_list_thu_chi, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThuChiAdapter.MyViewHolder holder, int position) {
        String sign;
        int icon;
        ThuChi thuChi = thuChiList.get(position);

        holder.tvTitle.setText(thuChi.getTitle());
        holder.tvDate.setText(thuChi.getDate());

        String category = thuChi.getCategory();
        Integer iconResId = categoryIconMap.get(category);

        if (iconResId != null) {
            holder.iconCategory.setImageResource(iconResId);
        } else {
            holder.iconCategory.setImageResource(R.drawable.ic_thu);
        }

        if(thuChi.getType().equals("thu")) {
            sign = "+";
            icon = R.drawable.ic_tien_thu;
        } else {
            sign = "-";
            icon = R.drawable.ic_tien_chi;
        }

        holder.iconPayment.setImageResource(icon);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        String formattedPayment = decimalFormat.format(thuChi.getPayment());
        holder.tvPayment.setText(String.format("%s%s VND", sign, formattedPayment));

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(thuChi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return thuChiList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvPayment;
        ImageView iconCategory, iconPayment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPayment = itemView.findViewById(R.id.tvPayment);
            iconCategory = itemView.findViewById(R.id.icon_category);
            iconPayment = itemView.findViewById(R.id.icon_payment);
        }
    }

    private HashMap<String, Integer> loadCategoryIcons() {
        HashMap<String, Integer> map = new HashMap<>();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.danh_muc_list);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray jsonArray = new JSONArray(jsonString.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String category = jsonObject.getString("text");
                String iconName = jsonObject.getString("icon");

                int iconResId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
                map.put(category, iconResId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
