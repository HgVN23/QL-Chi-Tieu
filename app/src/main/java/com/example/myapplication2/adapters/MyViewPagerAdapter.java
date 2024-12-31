package com.example.myapplication2.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication2.fragments.ChiFragment;
import com.example.myapplication2.fragments.ThuFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ThuFragment();
            case 1:
                return new ChiFragment();
            default:
                return new ThuFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
