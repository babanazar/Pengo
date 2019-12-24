package com.example.pengout.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pengout.view.fragment.JoinedFragment;
import com.example.pengout.view.fragment.PostedFragment;

public class PostedAndJoinedTabsAccessorAdapter extends FragmentPagerAdapter {
    public PostedAndJoinedTabsAccessorAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                JoinedFragment joinedFragment = new JoinedFragment();
                return joinedFragment;

            case 1:
                PostedFragment postedFragment = new PostedFragment();
                return postedFragment;

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
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Joined";

            case 1:
                return "Posted";

            default:
                return null;
        }

    }

}
