package com.app.qlchitieu.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "QLChiTieu.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_THUCHI = "thuChi";
    private static final String COLUMN_ID = "MaThuChi";
    private static final String COLUMN_TITLE = "TieuDe";
    private static final String COLUMN_DATE = "NgayThuChi";
    private static final String COLUMN_PAYMENT = "KhoanTien";
    private static final String COLUMN_TYPE = "LoaiThuChi";
    private static final String COLUMN_CATEGORY = "DanhMuc";

    private static final String TABLE_SETTING = "setting";
    private static final String COLUMN_ID_SETTING = "MaSetting";
    private static final String COLUMN_PIN = "PIN";
    private static final String COLUMN_HAN_MUC = "HanMuc";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryThuChi = "CREATE TABLE " + TABLE_THUCHI +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_PAYMENT + " INTEGER, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT);";
        db.execSQL(queryThuChi);

        String querySetting = "CREATE TABLE " + TABLE_SETTING +
                " (" + COLUMN_ID_SETTING + " INTEGER PRIMARY KEY, " +
                COLUMN_PIN + " INTEGER, " +
                COLUMN_HAN_MUC + " INTEGER);";
        db.execSQL(querySetting);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THUCHI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING);
        onCreate(db);
    }

    public Cursor getAll(String type) {
        String query = "SELECT * FROM " + TABLE_THUCHI +
                " WHERE " + COLUMN_TYPE + " = ?" +
                " ORDER BY " + COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{type});
        }
        return cursor;
    }

    public void addChiTieu(String TieuDe, String NgayThuChi, int KhoanTien, String Loai, String DanhMuc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, TieuDe);
        cv.put(COLUMN_DATE, NgayThuChi);
        cv.put(COLUMN_PAYMENT, KhoanTien);
        cv.put(COLUMN_TYPE, Loai);
        cv.put(COLUMN_CATEGORY, DanhMuc);

        long result = db.insert(TABLE_THUCHI, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateChiTieu(int id, String tieuDe, String ngayThuChi, int khoanTien, String loai, String danhMuc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, tieuDe);
        cv.put(COLUMN_DATE, ngayThuChi);
        cv.put(COLUMN_PAYMENT, khoanTien);
        cv.put(COLUMN_TYPE, loai);
        cv.put(COLUMN_CATEGORY, danhMuc);

        long result = db.update(TABLE_THUCHI, cv, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteChiTieu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_THUCHI, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor getByFilter(String type, String category, String date) {
        String query = "SELECT * FROM " + TABLE_THUCHI +
                " WHERE " + COLUMN_TYPE + " = ?";
        if(category != null && !category.isEmpty()) {
            query = query + " AND " + COLUMN_CATEGORY + " = '" + category + "'";
        }
        if(date != null && !date.isEmpty()) {
            query = query + " AND substr(" + COLUMN_DATE + ", 4, 7) = '" + date + "'";
        }
        query = query + " ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{type});
        }
        return cursor;
    }

    @SuppressLint("Range")
    public int getSumByType(String type, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        int sum = 0;
        String query = "SELECT SUM(" + COLUMN_PAYMENT + ") AS Total FROM " + TABLE_THUCHI +
                " WHERE " + COLUMN_TYPE + " = ?" +
                " AND substr(" + COLUMN_DATE + ", 4, 7) = ?";
        Cursor cursor = db.rawQuery(query, new String[]{type, date});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                sum = cursor.getInt(cursor.getColumnIndex("Total"));
            }
            cursor.close();
        }
        return sum;
    }


    public Cursor getSetting() {
        String query = "SELECT * FROM " + TABLE_SETTING;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getPIN(String PIN) {
        String query = "SELECT " + COLUMN_PIN + " FROM " + TABLE_SETTING +
                " WHERE " + COLUMN_PIN + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{PIN});
        }
        return cursor;
    }

    public Cursor getHanMuc() {
        String query = "SELECT " + COLUMN_HAN_MUC + " FROM " + TABLE_SETTING +
                " WHERE " + COLUMN_ID_SETTING + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateHanMuc(int newHanMuc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HAN_MUC, newHanMuc);

        db.update(TABLE_SETTING, values, COLUMN_ID_SETTING + " = 1", null);
        db.close();
    }


    public void addSetting(String PIN) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID_SETTING, 1);
        cv.put(COLUMN_PIN, PIN);
        cv.put(COLUMN_HAN_MUC, 5000000);

        long result = db.insert(TABLE_SETTING, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Setting thất bại", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Setting thành công", Toast.LENGTH_SHORT).show();
        }
    }
}
