package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

/**
 * Created by jchavando on 7/3/17.
 */

public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {

    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context context;
    public ArrayList<TweetsListFragment> mFragmentReferences = new ArrayList<>();

    public TweetsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }


    //return the total number of fragments
    //similar to recycler view

    @Override
    public int getCount() {
        return 2;
    }


    //return the fragment to use depending on the position

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            HomeTimelineFragment firstFrag = new HomeTimelineFragment();
            mFragmentReferences.add(0, firstFrag);
            return new HomeTimelineFragment();
            //return HomeTimelineFragment.newInstance(0, "Page #1");
        } else if (position == 1){
            MentionsTimelineFragments secondFrag = new MentionsTimelineFragments();
            mFragmentReferences.add(1, secondFrag);
            return new MentionsTimelineFragments();

        } else {
            return null;
        }
    }




}
