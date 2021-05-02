package com.hirmiproject.hirmi.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hirmiproject.hirmi.R;
import com.hirmiproject.hirmi.graphs;

public class SectionsPagerAdapter_forReport extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{ R.string.tab_daily_report,R.string.tab_monthly_report, R.string.graphs};
    private final Context mContext;

    public SectionsPagerAdapter_forReport(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new daily_report_fragment();
                break;
            case 1:
                fragment = new monthly_report_fragment();
                break;
            case 2:
                fragment = new graphs();
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
        // Show 3 total pages.
        return 3;
    }
}
