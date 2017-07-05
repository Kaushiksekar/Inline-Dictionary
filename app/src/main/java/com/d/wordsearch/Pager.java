package com.d.wordsearch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by kaushiksekar on 26/01/17.
 */

public class Pager extends FragmentStatePagerAdapter {

    int tabCount;

    public Pager(FragmentManager fm,int tabCount){
        super(fm);
        this.tabCount=tabCount;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                WordHistoryFragment wordHistoryFragment=new WordHistoryFragment();
                return wordHistoryFragment;
            case 1:
                AllElseHistoryFragment allElseHistoryFragment=new AllElseHistoryFragment();
                return allElseHistoryFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}