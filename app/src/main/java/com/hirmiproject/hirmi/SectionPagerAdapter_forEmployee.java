package com.hirmiproject.hirmi;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hirmiproject.hirmi.ui.main.daily_report_fragment;
import com.hirmiproject.hirmi.ui.main.monthly_report_fragment;

public class SectionPagerAdapter_forEmployee extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{ R.string.tab_employee_initiator,R.string.tab_employee_inspector};
    private final Context mContext;

    public SectionPagerAdapter_forEmployee (Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new employee_initiator();
                break;
            case 1:
                fragment = new employee_inspector();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}

