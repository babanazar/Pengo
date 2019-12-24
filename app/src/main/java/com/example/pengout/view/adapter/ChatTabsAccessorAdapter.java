package com.example.pengout.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pengout.view.fragment.ChatsFragment;
import com.example.pengout.view.fragment.ContactsFragment;
import com.example.pengout.view.fragment.GroupsFragment;
import com.example.pengout.view.fragment.RequestsFragment;

public class ChatTabsAccessorAdapter extends FragmentPagerAdapter {
    public ChatTabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i)
        {

            case 0:
                GroupsFragment groupsFragment= new GroupsFragment();
                return groupsFragment;

            case 1:
                ContactsFragment contactsFragment= new ContactsFragment();
               return contactsFragment;

            case 2:
                RequestsFragment requestsFragment= new RequestsFragment();
                return requestsFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Matches";

            case 1:
                return "Contacts";

            case 2:
                return "Requests";


            default:
                return null;
        }
    }
}
