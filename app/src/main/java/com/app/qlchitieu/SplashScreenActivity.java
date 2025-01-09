package com.app.qlchitieu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    TextView appname;
    ImageView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        appname= findViewById(R.id.appname);
        lottie=findViewById(R.id.IntroAni);

        appname.animate().translationY(-400).setDuration(2500).setStartDelay(0);
        lottie.animate().translationY(400).setDuration(2500).setStartDelay(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}