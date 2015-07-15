package com.example.saboorsalaam.veed10;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import Adapters.ChannelViewPagerAdapter;
import DatabaseHandler.DatabaseHandler;
import ParseClasses.ParseDBCommunicator;
import ParseClasses.Video;
import ParseClasses.connectionFailedListener;


public class InsideChannelActivity extends ActionBarActivity {
    ParseDBCommunicator parseDBCommunicator;
    ViewPager viewPager;
    DatabaseHandler databaseHandler;
    List<Video> videos;
    ChannelViewPagerAdapter channelViewPagerAdapter;
    String id, name;
    MenuItem fav_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_inside_channel_activity);

        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            id = extras.getString("channel_id");
            name = extras.getString("channel_name");
            setTitle(name);
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(name);

        this.overridePendingTransition(R.anim.in_from_right ,
                R.anim.out_to_left);

        parseDBCommunicator = new ParseDBCommunicator(getApplicationContext());
        databaseHandler = new DatabaseHandler(getApplicationContext());




        videos = parseDBCommunicator.getVideosofAChannel(id, name, new connectionFailedListener() {
            @Override
            public void onConnectionFailed() {

            }

            @Override
            public void onConnectionSuccessful() {

            }

            @Override
            public void onCannotConnectToParse() {

            }
        });
        Log.d("Size", "Retrieved from id: " + id + ": " + videos.size() + " videos");

        viewPager = (ViewPager) findViewById(R.id.pager);

        channelViewPagerAdapter = new ChannelViewPagerAdapter(getSupportFragmentManager());
        channelViewPagerAdapter.setList(videos);
        Log.d("Size", "Retrieved: " + videos.size() + " videos");
        viewPager.setAdapter(channelViewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inside_channel_activity, menu);

        return  true;
    }


        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

            case R.id.add_to_favorites:

                if(!databaseHandler.isVideoAFavorite(videos.get(viewPager.getCurrentItem()).getVideoId()))//If its not already a favorite set icon red and add to favs
                {
                    databaseHandler.addFavorite(videos.get(viewPager.getCurrentItem()));
                    //fav_item.setIcon(R.drawable.fav_selected_state);
                }
                else //if is already a ready a fav set icon back to grey and remove from favs
                {
                    //fav_item.setIcon(R.drawable.fav_unselected_state);
                    databaseHandler.deleteFavorite(videos.get(viewPager.getCurrentItem()));
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Intent getDefaultShareIntent(Video video){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        if(video.getType().equals("yt")) {
            intent.putExtra(Intent.EXTRA_SUBJECT, "Watch \"" +  video.getVideoTitle() + "\" on YouTube");
            intent.putExtra(Intent.EXTRA_TEXT,"https://www.youtube.com/watch?v=" + video.getVideoId());
        }

        if(video.getType().equals("v"))
        {
            intent.putExtra(Intent.EXTRA_SUBJECT, "Watch \"" +  video.getVideoTitle() + "\" on Vimeo");
            intent.putExtra(Intent.EXTRA_TEXT, "https://vimeo.com/" + video.getVideoId());
        }

        return intent;
    }
}
