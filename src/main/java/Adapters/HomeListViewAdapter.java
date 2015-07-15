package Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import DatabaseHandler.DatabaseHandler;
import ParseClasses.Channel;
import ParseClasses.Video;
import UtilityClasses.MyCalculator;


public class HomeListViewAdapter extends BaseAdapter {

    Context context;
    List<Video> videos = new ArrayList<Video>();
    List<Channel> channels = new ArrayList<>();
    private int[] empty_channel_views = {R.drawable.empty_channel_place_holder_blue, R.drawable.empty_channel_place_holder_green, R.drawable.empty_channel_place_holder_grey};
    private int counter = 0;
    private int view_counter = 0;
    private int color = 0;
    static final int VIDEO_TYPE = 0;
    static final int SUGGEST_CHANNELS_TYPE = 1;
    int width, height;
    FragmentManager fm;


    public HomeListViewAdapter(Context context, List<Video> videos, List<Channel> channels, int width, int height) {

        this.width = width;
        this.height = height;

        this.context = context;
        this.videos = videos;
        this.channels = channels;
        for (int z = 0; z < videos.size(); z++) {
            Log.d("Home List Adapter", "Added video from " + videos.get(z).getProvider_name() + " of type " + videos.get(z).getType());

        }
    }

    @Override
    public int getItemViewType(int position) {
            if ((position%20 == 0 && position!=0) || (position == 5)) {
                return SUGGEST_CHANNELS_TYPE;
            }else{
               return VIDEO_TYPE;
            }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public class ViewHolder {
        TextView title_textview, date_textview, channel_textview, provider_textview, type_textview, watched_indicator;
        ProgressBar progressBar;
        ImageView cover, provider_thumb, play;
        RelativeLayout cover_holder;
        RecyclerView recyclerView;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    ViewHolder viewHolder;
    boolean hasWatched;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        switch (getItemViewType(position)) {
            case VIDEO_TYPE:

                final int pos = position;



                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.explore_list_item1, null);
                    //Set video information
                    viewHolder.watched_indicator = (TextView) convertView.findViewById(R.id.watched_indicator);
                    viewHolder.title_textview = (TextView) convertView.findViewById(R.id.title);
                    viewHolder.date_textview = (TextView) convertView.findViewById(R.id.date);
                    viewHolder.channel_textview = (TextView) convertView.findViewById(R.id.channel);
                    viewHolder.provider_textview = (TextView) convertView.findViewById(R.id.provider);
                    viewHolder.type_textview = (TextView) convertView.findViewById(R.id.type);
                    viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.pb);
                    viewHolder.cover = (ImageView) convertView.findViewById(R.id.cover);
                    viewHolder.play = (ImageView) convertView.findViewById(R.id.play);
                    viewHolder.cover_holder = (RelativeLayout) convertView.findViewById(R.id.cover_holder);
                    viewHolder.provider_thumb = (ImageView) convertView.findViewById(R.id.imageView);
                    convertView.setTag(viewHolder);
                } else { viewHolder = (ViewHolder) convertView.getTag();}

                    final String dateinfo = videos.get(position).getDatePublished();
                    final String channel = videos.get(position).getChannel_name();
                    final String provider = videos.get(position).getProvider_name();
                    final String titleinfo = videos.get(position).getVideoTitle();
                    final String type = videos.get(position).getType();
                    String url = videos.get(position).getVideoThumbnail();
                    final String prov_thumb = videos.get(position).getProvider_thumbnail();
                    final String id = videos.get(position).getVideoId();

                    if (type.equals("yt")) {
                        url = "http://img.youtube.com/vi/" + videos.get(position).getVideoId() + "/maxresdefault.jpg";
                    }


                    if (dateinfo.length() > 9) {
                        String day = dateinfo.substring(8, 10);
                        String month = dateinfo.substring(5, 7);
                    }

                    String timePassedString = MyCalculator.getTimeAgo(dateinfo);

                    viewHolder.date_textview.setText(timePassedString);
                    viewHolder.channel_textview.setText(channel);
                    viewHolder.provider_textview.setText(provider);
                    viewHolder.title_textview.setText(titleinfo);

                    viewHolder.watched_indicator.setVisibility(View.INVISIBLE);
                    unSetGreyScale(viewHolder.cover);

                    hasWatched = new DatabaseHandler(context).hasVideoBeenWatched(id);

                    if (hasWatched) {
                        viewHolder.watched_indicator.setVisibility(View.VISIBLE);
                        setGreyScale(viewHolder.cover);
                    }

                    switch (type) {
                        case "yt":
                            viewHolder.type_textview.setText("YouTube");
                            break;
                        case "v":
                            viewHolder.type_textview.setText("Vimeo");
                            break;
                        case "dm":
                            viewHolder.type_textview.setText("DailyMotion");
                            break;
                        default:
                            break;
                    }

                    switch (counter) {
                        case 0:
                            viewHolder.cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                            viewHolder.provider_thumb.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                            color = R.drawable.empty_channel_place_holder_blue;
                            counter++;
                            break;
                        case 1:
                            viewHolder.cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_green);
                            viewHolder.provider_thumb.setBackgroundResource(R.drawable.empty_channel_place_holder_green);
                            color = R.drawable.empty_channel_place_holder_green;
                            counter++;
                            break;
                        case 2:
                            viewHolder.cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_grey);
                            viewHolder.provider_thumb.setBackgroundResource(R.drawable.empty_channel_place_holder_grey);
                            color = R.drawable.empty_channel_place_holder_grey;
                            counter = 0;
                            break;
                        default:
                            viewHolder.cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                            viewHolder.provider_thumb.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                            color = R.drawable.empty_channel_place_holder_blue;
                            counter = 1;
                            break;
                    }


                    Picasso.with(context).load(url).centerCrop().fit().into(viewHolder.cover, new Callback() {
                        @Override
                        public void onSuccess() {
                            viewHolder.cover.setVisibility(View.VISIBLE); //WHY IS THIS RETURNING NULL???!!!!****************
                            viewHolder.progressBar.setVisibility(View.GONE);
                            viewHolder.play.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });

                    Picasso.with(context).load(prov_thumb).centerCrop().fit().into(viewHolder.provider_thumb, new Callback() {
                        @Override
                        public void onSuccess() {
                            //viewHolder.cover.setVisibility(View.VISIBLE);
                            //viewHolder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });


                    viewHolder.cover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(context, "Clicked on "+ type  +" video", Toast.LENGTH_SHORT).show();
                            Intent intent;
                            if (!hasWatched) {
                                Toast.makeText(context, "Video watched!", Toast.LENGTH_SHORT).show();
                                new DatabaseHandler(context).addWatchedVideo(id);
                                viewHolder.watched_indicator.setVisibility(View.VISIBLE);
                                setGreyScale(viewHolder.cover);
                            }

                            switch (type) {
                                case "v":
                                    intent = new Intent(context, VimeoPlayerActivity.class);
                                    intent.putExtra("video_id", videos.get(pos).getVideoId());
                                    context.startActivity(intent);
                                    break;
                                case "yt":
                                    intent = new Intent(context, YouTubePlayerActivity.class);
                                    intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
                                    intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
                                    intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                                    intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.modal_close_enter);
                                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.modal_close_exit);
                                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, videos.get(pos).getVideoId());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    break;
                                case "dm":
                                    intent = new Intent(context, DailyMotionPlayerActivity.class);
                                    intent.putExtra("video_id", videos.get(pos).getVideoId());
                                    context.startActivity(intent);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                break;
            case SUGGEST_CHANNELS_TYPE:

                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.explore_list_item2, null);
                    viewHolder = new ViewHolder();
                    viewHolder.recyclerView = (RecyclerView) convertView.findViewById(R.id.recycler_view);
                    convertView.setTag(viewHolder);
                }
                else{viewHolder = (ViewHolder) convertView.getTag();}

                SuggestedChannelsRecyclerViewAdapter suggestedChannelsRecyclerViewAdapter = new SuggestedChannelsRecyclerViewAdapter(context, channels, width, height);
                //viewHolder.recyclerView.setHasFixedSize(true);
                viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                viewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
                viewHolder.recyclerView.setAdapter(suggestedChannelsRecyclerViewAdapter);
                break;
            default:
                break;
        }
            return convertView;
        }

    public static void  setGreyScale(ImageView v)
    {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        v.setImageAlpha(128);   // 128 = 0.5
    }

    public static void  unSetGreyScale(ImageView v)
    {
        v.setColorFilter(null);
        v.setImageAlpha(255);
    }

    }


