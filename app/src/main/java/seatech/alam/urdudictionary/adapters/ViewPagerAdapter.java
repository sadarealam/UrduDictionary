package seatech.alam.urdudictionary.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import seatech.alam.urdudictionary.fragments.Definition;
import seatech.alam.urdudictionary.fragments.Favourite;
import seatech.alam.urdudictionary.fragments.History;
import seatech.alam.urdudictionary.fragments.Home;

/**
 * Created by root on 27/9/15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final int TAB_COUNT = 4 ;
    private final String[] TAB_TITLE = {"Home","Definition","Favourite","History"};
    Context context ;

    public ViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context ;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null ;
        switch (position){
            case 0 :
                fragment = new Home() ;
                return fragment ;
            case 1 :
                fragment = new Definition();
                return fragment ;
            case 2 :
                fragment = new Favourite();
                return fragment ;
            case 3 :
                fragment = new History();
                return fragment;
        }
        return  null ;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLE[position] ;
    }
}
