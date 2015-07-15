package Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


import Fragments.ExploreViewPagerFragmentDefault;
import ParseClasses.Video;

/**
 * Created by Saboor Salaam on 5/31/2015.
 */
public class ExploreViewPagerAdapter extends FragmentStatePagerAdapter {

    Context context;
    List<Video> videos = new ArrayList<Video>();
    private int counter = 0;
    private int color = 0;

    public ExploreViewPagerAdapter(FragmentManager fm, Context context, List<Video> videos) {
        super(fm);
        this.context = context;
        this.videos = videos;
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

        switch (position) {
            case 0:
                //return new ExploreViewPagerFragmentCover().newInstance(videos.get(position), counter);
            default:
                return new ExploreViewPagerFragmentDefault().newInstance(videos.get(position), counter);
        }


    }

    @Override
    public int getCount() {
        return videos.size();
    }
}
