package com.app.qlchitieu.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.qlchitieu.fragments.ChiFragment;
import com.app.qlchitieu.fragments.QuanLyFragment;
import com.app.qlchitieu.fragments.ThuFragment;

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
