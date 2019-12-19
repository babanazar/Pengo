package com.example.pengout.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pengout.view.fragment.GecmisEtkinlikFragment;
import com.example.pengout.view.fragment.GelecekEtkinlikFragment;

public class EtkinlikTabsAccessorAdapter extends FragmentPagerAdapter {
    public EtkinlikTabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                GelecekEtkinlikFragment gelecekEtkinlikFragment = new GelecekEtkinlikFragment();
                return gelecekEtkinlikFragment;
            case 1:
                GecmisEtkinlikFragment gecmisEtkinlikFragment = new GecmisEtkinlikFragment();
                return gecmisEtkinlikFragment;

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
                return "Upcomings";

            case 1:
                return " ";

            default:
                return null;
        }
    }
}
