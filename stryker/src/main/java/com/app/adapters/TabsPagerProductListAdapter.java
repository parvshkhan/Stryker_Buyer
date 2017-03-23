package com.app.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.fragments.ChatListFragment;
import com.app.fragments.ProductListFragment;
import com.app.fragments.StoreFrontFragment;


public class TabsPagerProductListAdapter extends FragmentPagerAdapter {

    public TabsPagerProductListAdapter(FragmentManager fm) {
        super(fm);
    }


    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // store fragment fragment activity
                return new StoreFrontFragment();
            case 1:
                // product list fragment activity
                return new ProductListFragment();

            case 2:
                //
                return new ChatListFragment();
        }
        return null;
    }


    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
