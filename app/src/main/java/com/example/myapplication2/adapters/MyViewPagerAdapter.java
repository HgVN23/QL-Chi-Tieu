package com.example.myapplication2.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication2.fragments.ChiFragment;
import com.example.myapplication2.fragments.QuanLyFragment;
import com.example.myapplication2.fragments.ThuFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull QuanLyFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new ChiFragment();
            case 0:
            default:
                return new ThuFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
