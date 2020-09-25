package com.example.vaporwaveVideo.adapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.vaporwaveVideo.R;

import java.util.ArrayList;

public class EditSelectActionViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments=new ArrayList<>();
    Context context;
    String tabtitle[];

    public EditSelectActionViewPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<Fragment> fragments, Context context) {
        super(fm, behavior);
        this.fragments = fragments;
        this.context = context;
        this.tabtitle = context.getResources().getStringArray(R.array.edit_action_menu_bar);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitle[position];
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
