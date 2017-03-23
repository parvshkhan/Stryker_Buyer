package com.app.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.fragments.ChatUserListFragment;
import com.app.fragments.OrderHomeFragment;
import com.app.fragments.StoresHomeFragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Account Setting fragment activity
                return OrderHomeFragment.getInstance();
            case 1:
                // General Setting fragment activity
                return new StoresHomeFragment();
            case 2:
                // chat user list fragment
                return new ChatUserListFragment();
        }
        return null;
    }


    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
