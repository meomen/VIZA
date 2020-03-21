package com.vuducminh.viza.adapters;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class TabsPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> listFragment;

    public TabsPagerAdapter(FragmentManager fm, ArrayList<Fragment> listFragment) {
        super(fm);
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}
