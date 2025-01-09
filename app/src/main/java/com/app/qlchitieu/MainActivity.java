package com.app.qlchitieu;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.qlchitieu.fragments.QuanLyFragment;
import com.app.qlchitieu.fragments.SettingFragment;
import com.app.qlchitieu.fragments.ThongKeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private final QuanLyFragment quanLyFragment = new QuanLyFragment();
    private final ThongKeFragment thongKeFragment = new ThongKeFragment();
    private final SettingFragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        loadFragment(quanLyFragment);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.nav_quan_ly) {
                    selectedFragment = quanLyFragment;
                } else if (item.getItemId() == R.id.nav_thong_ke) {
                    selectedFragment = thongKeFragment;
                } else if (item.getItemId() == R.id.nav_cai_dat) {
                    selectedFragment = settingFragment;
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment addedFragment : fragmentManager.getFragments()) {
            transaction.hide(addedFragment);
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.container, fragment);
        }

        transaction.commit();
    }

}
