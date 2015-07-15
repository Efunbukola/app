package Fragments;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.saboorsalaam.veed10.DailyMotionPlayerActivity;
import com.example.saboorsalaam.veed10.R;
import com.example.saboorsalaam.veed10.VimeoPlayerActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import CustomYouTubePlayer.Orientation;
import ParseClasses.Video;
import UtilityClasses.MyCalculator;
import com.example.saboorsalaam.veed10.YouTubePlayerActivity;


/**
 * Created by Saboor Salaam on 6/10/2014.
 */
public class InsideChannelVideoFragment extends Fragment {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    FragmentManager fm;
    ProgressBar pb;
    ImageView cover;


    static private final String DEVELOPER_KEY = "AIzaSyCLGCJOPU8VVj7daoh5HwXZASnmGoc4ylo";

    public InsideChannelVideoFragment newInstance(Video v) {

        InsideChannelVideoFragment vf = new InsideChannelVideoFragment();
        String uri = v.getVideoThumbnail();


        String id = v.getVideoId();
        String info = v.getVideoDescription();
        String title = v.getVideoTitle();
        String date = v.getDatePublished();
        String channel = v.getProvider_name();
        String type = v.getType();
        String prov_thumb = v.getProvider_thumbnail();
        //currentVideoID = id;

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("info", info);
        bundle.putString("thumb", uri);
        bundle.putString("date", date);
        bundle.putString("channel", channel);
        bundle.putString("title", title);
        bundle.putString("type", type);
        bundle.putString("provider_thumb", prov_thumb);

        vf.setArguments(bundle);


        return vf;
    }




    View rootView;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (rootView == null) {
            // Inflate the layout for this fragment
            rootView = inflater.inflate(R.layout.video_player_fragment_layout, container, false);
        }

            //Set video information
            TextView title_textview = (TextView) rootView.findViewById(R.id.title);
            TextView date_textview = (TextView) rootView.findViewById(R.id.date);
            TextView channel_textview = (TextView) rootView.findViewById(R.id.provider);
            TextView video_description_textview = (TextView) rootView.findViewById(R.id.desc);
            ImageView provider_thumb = (ImageView) rootView.findViewById(R.id.imageView);

            String titleinfo = "";
            String description = " ";
            String dateinfo = " ";
            String channel = " ";
            String month, day;
            final String type;
            String url = "";
            String prov_thumb = "";

            description = getArguments().getString("info");
            dateinfo = getArguments().getString("date");
            channel = getArguments().getString("channel");
            titleinfo = getArguments().getString("title");
            type = getArguments().getString("type");
            url = getArguments().getString("thumb");
            prov_thumb = getArguments().getString("provider_thumb");

            if (dateinfo.length() > 9) {
                day = dateinfo.substring(8, 10);
                month = dateinfo.substring(5, 7);
            }

            title_textview.setText(titleinfo);

            String timePassedString = MyCalculator.getTimeAgo(dateinfo);

            date_textview.setText(timePassedString);
            channel_textview.setText(channel);
            video_description_textview.setText(Html.fromHtml(description));

            pb = (ProgressBar) rootView.findViewById(R.id.pb);
/*
        Picasso.with(getActivity()).load(prov_thumb).centerCrop().fit().into(provider_thumb, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {

            }
        });
        */


            cover = (ImageView) rootView.findViewById(R.id.cover);
            Picasso.with(getActivity()).load(url).centerCrop().fit().into(cover, new Callback() {
                @Override
                public void onSuccess() {
                    cover.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);

                }

                @Override
                public void onError() {

                }
            });
            cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent;
                    switch (type) {
                        case "v":
                            intent = new Intent(getActivity(), VimeoPlayerActivity.class);
                            intent.putExtra("video_id", (String) getArguments().getString("id"));
                            getActivity().startActivity(intent);
                            break;
                        case "yt":
                            intent = new Intent(getActivity(), YouTubePlayerActivity.class);
                            intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
                            intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
                            intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                            intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                            intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.modal_close_enter);
                            intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.modal_close_exit);
                            intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, (String) getArguments().getString("id"));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                            break;
                        case "dm":
                            intent = new Intent(getActivity(), DailyMotionPlayerActivity.class);
                            intent.putExtra("video_id", (String) getArguments().getString("id"));
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
