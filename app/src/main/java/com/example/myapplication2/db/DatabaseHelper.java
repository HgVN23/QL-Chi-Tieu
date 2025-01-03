package com.example.myapplication2.db;

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

    private static final String TABLE_NAME = "thuChi";
    private static final String COLUMN_ID = "MaThuChi";
    private static final String COLUMN_TITLE = "TieuDe";
    private static final String COLUMN_DATE = "NgayThuChi";
    private static final String COLUMN_PAYMENT = "KhoanTien";
    private static final String COLUMN_TYPE = "LoaiThuChi";
    private static final String COLUMN_CATEGORY = "DanhMuc";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_PAYMENT + " INTEGER, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getAll(String type) {
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_TYPE + " = '" + type + "'" +
                " ORDER BY " + COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
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

        long result = db.insert(TABLE_NAME, null, cv);
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

        long result = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteChiTieu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
