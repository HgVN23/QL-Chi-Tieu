package com.app.qlchitieu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.qlchitieu.R;

public class DanhMucAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private JSONArray danhMucList;

    public DanhMucAdapter(@NonNull Context context, @NonNull JSONArray danhMucList) {
        super(context, 0);
        this.context = context;
        this.danhMucList = danhMucList;
    }

    @Override
    public int getCount() {
        return danhMucList.length();
    }

    @Nullable
    @Override
    public JSONObject getItem(int position) {
        return danhMucList.optJSONObject(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_item_list_danh_muc, parent, false);
        }

        ImageView iconView = convertView.findViewById(R.id.item_icon);
        TextView textView = convertView.findViewById(R.id.item_text);

        JSONObject danhMuc = getItem(position);
        if (danhMuc != null) {
            String iconName = danhMuc.optString("icon");
            String text = danhMuc.optString("text");

            int iconResId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
            iconView.setImageResource(iconResId);
            textView.setText(text);
        }

        return convertView;
    }
}
