package com.example.ebook;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> datas;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(ArrayList<Fragment> datas) {
        this.datas = datas;
    }
    @Override
    public Fragment getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }
}

