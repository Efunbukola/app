package Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import Fragments.ExploreListViewPagerFragement;
import Fragments.ExploreViewPagerFragmentDefault;
import ParseClasses.Channel;
import ParseClasses.Video;

/**
 * Created by Saboor Salaam on 5/31/2015.
 */
public class ExploreListViewViewPagerAdapter extends FragmentStatePagerAdapter {

    Context context;
    List<Channel> channels = new ArrayList<>();
    private int counter = 0;

    public ExploreListViewViewPagerAdapter(FragmentManager fm, Context context, List<Channel> channels) {
        super(fm);
        this.context = context;
        this.channels = channels;
    }


    @Override
    public Fragment getItem(int position) {

        switch (counter) {
            case 0:
                counter++;
                break;
            case 1:
                counter++;
                break;
            case 2:
                counter=0;
                break;
            default:
                counter=1;
                break;
        }

           return new ExploreListViewPagerFragement().newInstance(channels.get(position), counter);
        }

    @Override
    public int getCount() {
        return channels.size();
    }
}
