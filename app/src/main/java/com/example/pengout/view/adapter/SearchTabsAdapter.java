package com.example.pengout.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pengout.view.fragment.EventResultsFragment;
import com.example.pengout.view.fragment.UserResultsFragment;

public class SearchTabsAdapter extends FragmentPagerAdapter {

    public SearchTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                EventResultsFragment eventResultsFragment = new EventResultsFragment();
                return eventResultsFragment;
            case 1:
                UserResultsFragment userResultsFragment = new UserResultsFragment();
                return userResultsFragment;
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
        switch (position){
            case 0:
                return "Events";
            case 1:
                return "Users";
            default:
                return null;
        }
    }


}
