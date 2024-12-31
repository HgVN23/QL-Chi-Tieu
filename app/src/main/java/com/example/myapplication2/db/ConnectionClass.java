/* Không dùng file này
package com.example.myapplication2.db;

import android.annotation.SuppressLint;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    String url = "jdbc:mysql://localhost:3306/test";

    @SuppressLint("NewApi")
    public Connection CONN() {
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, "root", "");
            if(conn != null) {
                Log.i("Thong Bao", "Success");
            } else {
                Log.i("Thong Bao", "Failed");
            }
        }  catch (ClassNotFoundException e) {
            Log.e("ERROR", "Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            Log.e("ERROR", "SQL Exception: " + e.getMessage());
        }

        return conn;
    }
}
*/
