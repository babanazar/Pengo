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
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;

            case 1:
                GroupsFragment groupsFragment= new GroupsFragment();
                return groupsFragment;

            case 2:
                ContactsFragment contactsFragment= new ContactsFragment();
               return contactsFragment;

            case 3:
                RequestsFragment requestsFragment= new RequestsFragment();
                return requestsFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Chats";

            case 1:
                return "Matchs";

            case 2:
                return "Contacts";

            case 3:
                return "Requests";

            default:
                return null;
        }
    }
}
