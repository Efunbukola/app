package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saboorsalaam.veed10.DailyMotionPlayerActivity;
import com.example.saboorsalaam.veed10.R;
import com.example.saboorsalaam.veed10.VimeoPlayerActivity;
import com.example.saboorsalaam.veed10.YouTubePlayerActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import CustomYouTubePlayer.Orientation;
import ParseClasses.Video;
import UtilityClasses.MyCalculator;

/**
 * Created by Saboor Salaam on 5/31/2015.
 */
public class ExploreViewPagerFragmentDefault extends Fragment {

    List<Video> videos = new ArrayList<Video>();
    private int[] empty_channel_views = {R.drawable.empty_channel_place_holder_blue, R.drawable.empty_channel_place_holder_green, R.drawable.empty_channel_place_holder_grey};
    private int color = 0;
    Context context;
    String thumbnail;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

   public ExploreViewPagerFragmentDefault newInstance(Video v, int counter) {

        ExploreViewPagerFragmentDefault vf = new ExploreViewPagerFragmentDefault();

       final String dateinfo = v.getDatePublished();
       final String channel = v.getChannel_name();
       final String provider = v.getProvider_name();
       final String title = v.getVideoTitle();
       final String type = v.getType();
       final String desc = v.getVideoDescription();
       String url = v.getVideoThumbnail();
       String id = v.getVideoId();

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("thumb", url);
        bundle.putString("date", dateinfo);
        bundle.putString("channel", channel);
        bundle.putString("title", title);
        bundle.putString("type", type);
        bundle.putString("provider", provider);
        bundle.putInt("counter", counter);
        bundle.putString("desc", desc);

        vf.setArguments(bundle);

        return vf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.explore_list_item1, container, false);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_web_view);


        String title = "";
        String date = " ";
        String channel = " ";
        String desc = "";
        String month, day;
        final String type;
        String highurl = "";
        thumbnail = "";
        String provider;
        int counter = 0;

        counter = (int) getArguments().get("counter");
        date = (String) getArguments().getString("date");
        channel = (String) getArguments().getString("channel");
        title = (String) getArguments().getString("title");
        type = (String) getArguments().getString("type");
        final String id = (String) getArguments().getString("id");
        thumbnail = (String) getArguments().getString("thumb");
        provider = (String) getArguments().getString("provider");
        desc = (String) getArguments().getString("desc");

        if (date.length() > 9) {
            day = date.substring(8, 10);
            month = date.substring(5, 7);
        }

        String timePassedString = MyCalculator.getTimeAgo(date);

        //Set video information
        final TextView title_textview = (TextView) rootView.findViewById(R.id.title);
        final TextView date_textview = (TextView) rootView.findViewById(R.id.date);
        final TextView channel_textview = (TextView) rootView.findViewById(R.id.channel);
        final TextView provider_textview = (TextView) rootView.findViewById(R.id.provider);
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.pb);
        final ImageView cover = (ImageView) rootView.findViewById(R.id.cover);
        final RelativeLayout cover_holder = (RelativeLayout) rootView.findViewById(R.id.cover_holder);
        final TextView desc_textview = (TextView) rootView.findViewById(R.id.desc);

        date_textview.setText(timePassedString);
        channel_textview.setText(channel); //*****************************888 Look Here
        provider_textview.setText(provider);
        title_textview.setText(title);
        desc_textview.setText(desc);

        switch (counter) {
            case 0:
               cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                color = R.drawable.empty_channel_place_holder_blue;
                break;
            case 1:
                cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_green);
                color = R.drawable.empty_channel_place_holder_green;
                break;
            case 2:
               cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_grey);
                color = R.drawable.empty_channel_place_holder_grey;
                break;
            default:
                cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                color = R.drawable.empty_channel_place_holder_blue;
                break;
        }

        if(type.equals("yt")) {
            Picasso.with(getActivity()).load("http://img.youtube.com/vi/" + id + "/maxresdefault.jpg").centerCrop().fit().into(cover, new Callback() {
                @Override
                public void onSuccess() {
                    cover.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                        Picasso.with(getActivity()).load(thumbnail).centerCrop().fit().into(cover, new Callback() {
                            @Override
                            public void onSuccess() {
                                cover.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                            }

                        });
                }
            });
        }
        else{
            Picasso.with(getActivity()).load(thumbnail).centerCrop().fit().into(cover, new Callback() {
                @Override
                public void onSuccess() {
                    cover.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                }
            });
        }

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch(type){
                    case "v":
                        intent = new Intent(getActivity(), VimeoPlayerActivity.class);
                        intent.putExtra("video_id", id);
                        Toast.makeText(getActivity(), "Clicked on " + type + " video", Toast.LENGTH_SHORT).show();
                        context.startActivity(intent);
                        break;
                    case "yt":
                        intent = new Intent(getActivity(), YouTubePlayerActivity.class);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.modal_close_enter);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.modal_close_exit);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                        break;
                    case "dm":
                        intent = new Intent(getActivity(), DailyMotionPlayerActivity.class);
                        intent.putExtra("video_id", id);
                        getActivity().startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        return rootView;
    }
}
