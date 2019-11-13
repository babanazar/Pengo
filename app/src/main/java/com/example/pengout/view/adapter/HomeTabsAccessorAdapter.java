package com.example.pengout.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pengout.view.fragment.FutureEventFragment;
import com.example.pengout.view.fragment.PastEventFragment;

public class HomeTabsAccessorAdapter extends FragmentPagerAdapter {
    public HomeTabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i)
        {
            case 0:
                FutureEventFragment futureEventFragment= new FutureEventFragment();
                return futureEventFragment;

            case 1:
                PastEventFragment pastEventFragment= new PastEventFragment();
                return pastEventFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Future Events";

            case 1:
                return "Past Events";

            default:
                return null;
        }
    }
}
