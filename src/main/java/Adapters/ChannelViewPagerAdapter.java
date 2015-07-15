package Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;


import java.util.Collections;
import java.util.List;

import Fragments.InsideChannelVideoFragment;
import ParseClasses.Video;
import UtilityClasses.VideoComparator;


/**
 * Created by Saboor Salaam on 6/10/2014.
 */
public class ChannelViewPagerAdapter extends FragmentStatePagerAdapter {

    List<Video> videos;
    SparseArray<InsideChannelVideoFragment> registeredFragments = new SparseArray<InsideChannelVideoFragment>();


    public ChannelViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setList(List<Video> v) {
        //notifyDataSetChanged();
        videos = v;
        Collections.sort(videos, new VideoComparator());

    }

    @Override
    public Fragment getItem(int position) {
        //notifyDataSetChanged();
        return new InsideChannelVideoFragment().newInstance(videos.get(position));
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        InsideChannelVideoFragment fragment = (InsideChannelVideoFragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public InsideChannelVideoFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}

