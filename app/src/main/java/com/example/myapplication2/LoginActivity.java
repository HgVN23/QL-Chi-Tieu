/* Không dùng file này
package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    Button btDangNhap;
    EditText edTenDangNhap, edMatKhau;
    public boolean check = false;

    String url = "http://192.168.0.201/androidwebservice/getdatauser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bind();
        btDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KiemTraUser();
            }
        });

        edMatKhau.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    KiemTraUser();
                }
                return false;
            }
        });
    }

    public void KiemTraUser() {
        final String tdn = edTenDangNhap.getText().toString();
        final String mk = edMatKhau.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        if (tdn.isEmpty() || mk.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Các mục đang trống", Toast.LENGTH_SHORT).show();
        } else {

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    if (tdn.equals(object.getString("TenDangNhap")) && mk.equals(object.getString("MatKhau"))) {
                                        check = true;
                                        Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                        String tdni = object.getString("TenDangNhap");
                                        it.putExtra("TenDangNhap", tdni);
                                        startActivity(it);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            if (!check) {
                                edMatKhau.setError("Sai Tên đăng nhâp hoặc mật khẩu");
                                Toast.makeText(LoginActivity.this, "Sai Tên đăng nhâp hoặc mật khẩu", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }

            );
            requestQueue.add(jsonArrayRequest);
        }
    }


    public void Bind() {
        edTenDangNhap = (EditText) findViewById(R.id.username);
        edMatKhau = (EditText) findViewById(R.id.password);
        btDangNhap = (Button) findViewById(R.id.btnSignIn);

    }
}
*/
