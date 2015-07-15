package Adapters;

/**
 * Created by Saboor Salaam on 6/11/2015.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.saboorsalaam.veed10.DailyMotionPlayerActivity;
import com.example.saboorsalaam.veed10.InsideChannelActivity;
import com.example.saboorsalaam.veed10.R;
import com.example.saboorsalaam.veed10.VimeoPlayerActivity;
import com.example.saboorsalaam.veed10.YouTubePlayerActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import CustomYouTubePlayer.Orientation;
import ParseClasses.Channel;
import ParseClasses.Video;
import UtilityClasses.MyCalculator;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private List<Video> videos = new ArrayList<Video>();
    private List<Channel> channels = new ArrayList<>();
    private int[] empty_channel_views = {R.drawable.empty_channel_place_holder_blue, R.drawable.empty_channel_place_holder_green, R.drawable.empty_channel_place_holder_grey};
    private int counter = 0;
    private int color_counter;
    private int color = 0;
    private int inflated_suggested_channel_views = 0;
    int width, height;

    private View mHeaderView;

    private static final int VIEW_TYPE_HEADER = 0;
    static final int VIDEO_TYPE = 1;
    static final int SUGGEST_CHANNELS_TYPE = 2;
    Animation fadeIn, fadeOut2, fadeOut;


    public HomeRecyclerViewAdapter(Context context, List<Video> videos, List<Channel> channels, View mHeaderView, int width, int height) {
        this.width = width;
        this.height = height;
        this.context = context;
        this.videos = videos;
        this.channels = channels;
        this.mHeaderView = mHeaderView;

        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(300);

        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(300);
    }

    @Override
    public int getItemViewType(int position) {
/*
        if ((position%15 == 0 && position!=0) || (position == 5)) {
            inflated_suggested_channel_views++;
            return SUGGEST_CHANNELS_TYPE;
        }
        */
        /*
        else{
            return VIDEO_TYPE;
        }*/
        return (position == 0) ? VIEW_TYPE_HEADER : VIDEO_TYPE;
}


    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return videos.size();
        } else {
            return videos.size() + 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        switch(viewType){
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(mHeaderView);
            case SUGGEST_CHANNELS_TYPE:
                return new HomeRecyclerViewAdapter.ViewHolderSuggestChannels(mInflater.inflate(R.layout.explore_list_item2, parent, false));
            case VIDEO_TYPE:
                return new HomeRecyclerViewAdapter.ViewHolderVideo(mInflater.inflate(R.layout.explore_list_item1atl2, parent, false));
            default:
                return new HomeRecyclerViewAdapter.ViewHolderVideo(mInflater.inflate(R.layout.explore_list_item1atl2, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {

        switch (getItemViewType(i))
        {
            case SUGGEST_CHANNELS_TYPE:

                ViewHolderSuggestChannels viewHolder = (ViewHolderSuggestChannels) holder;
                SuggestedChannelsRecyclerViewAdapter suggestedChannelsRecyclerViewAdapter = new SuggestedChannelsRecyclerViewAdapter(context, this.channels, this.width, this.height);
                viewHolder.recyclerView.setAdapter(suggestedChannelsRecyclerViewAdapter);
                break;

            case VIDEO_TYPE:
                final int position = (i-1); //- inflated_suggested_channel_views;
                final ViewHolderVideo _viewHolder = (ViewHolderVideo) holder;

                _viewHolder.header.setVisibility((i==1) ? View.VISIBLE :View.GONE);



                /*MaterialRippleLayout.on(_viewHolder.channel_textview)
                        .rippleColor(Color.parseColor("#000000"))
                        .rippleAlpha(0.7f)
                        .rippleHover(true)
                        .create();
                        */

                final String dateinfo = videos.get(position).getDatePublished();
                final String channel = videos.get(position).getChannel_name();
                final String provider = videos.get(position).getProvider_name();
                final String titleinfo = videos.get(position).getVideoTitle().replaceAll("[^\\x00-\\x7F]", ""); //Pattern Matching!!
                final String type = videos.get(position).getType();
                String url = videos.get(position).getVideoThumbnail();
                final String prov_thumb = videos.get(position).getProvider_thumbnail();
                final String desc = videos.get(position).getVideoDescription().replaceAll("[^\\x00-\\x7F]", "");

                if (type.equals("yt")) {
                    url = "http://img.youtube.com/vi/" + videos.get(position).getVideoId() + "/maxresdefault.jpg";
                }

                String timePassedString = MyCalculator.getTimeAgo(dateinfo);

                _viewHolder.date_textview.setText(timePassedString);
                _viewHolder.channel_textview.setText(channel);
                _viewHolder.provider_textview.setText(provider);
                _viewHolder.title_textview.setText(titleinfo);

                if(isWhitespace(desc) || desc.contains("https") || desc.contains("http"))
                {
                    _viewHolder.desc_textview.setVisibility(View.GONE);
                }else{
                    desc.replace("\n", " ");
                    desc.replace("<br>", " ");
                    desc.replace("<p>", " ");
                    _viewHolder.desc_textview.setText(Html.fromHtml(desc));
                }

                _viewHolder.watched_indicator.setVisibility(View.INVISIBLE);


                //unSetGreyScale(_viewHolder.cover);
                /*
                final boolean hasWatched = new DatabaseHandler(context).hasVideoBeenWatched(id);

                if (hasWatched) {
                    _viewHolder.watched_indicator.setVisibility(View.VISIBLE);
                    setGreyScale(_viewHolder.cover);
                }*/

                switch (type) {
                    case "yt":
                        _viewHolder.type_textview.setText("YouTube");
                        break;
                    case "v":
                        _viewHolder.type_textview.setText("Vimeo");
                        break;
                    case "dm":
                        _viewHolder.type_textview.setText("DailyMotion");
                        break;
                    default:
                        break;
                }

/*
                switch (2) {
                    case 0:
                        _viewHolder.cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                        _viewHolder.provider_thumb.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                        color = R.drawable.empty_channel_place_holder_grey;
                        counter++;
                        break;
                    case 1:
                        _viewHolder.cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_green);
                        _viewHolder.provider_thumb.setBackgroundResource(R.drawable.empty_channel_place_holder_green);
                        color = R.drawable.empty_channel_place_holder_grey;
                        counter++;
                        break;
                    case 2:
                        _viewHolder.cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_grey);
                        _viewHolder.provider_thumb.setBackgroundResource(R.drawable.empty_channel_place_holder_grey);
                        color = R.drawable.empty_channel_place_holder_grey;
                        counter = 0;
                        break;
                    default:
                        _viewHolder.cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                        _viewHolder.provider_thumb.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                        color = R.drawable.empty_channel_place_holder_grey;
                        counter = 1;
                        break;
                }
*/


                Picasso.with(context).load(url).centerCrop().fit().into(_viewHolder.cover, new Callback() {
                    @Override
                    public void onSuccess() {
                        _viewHolder.cover.setVisibility(View.VISIBLE); //WHY IS THIS RETURNING NULL???!!!!****************
                        _viewHolder.progressBar.setVisibility(View.GONE);
                        _viewHolder.play.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });

                Picasso.with(context).load(prov_thumb).centerCrop().fit().into(_viewHolder.provider_thumb, new Callback() {
                    @Override
                    public void onSuccess() {
                        //viewHolder.cover.setVisibility(View.VISIBLE);
                        //viewHolder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

                _viewHolder.menu.setVisibility(View.GONE);

                _viewHolder.channel_textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, InsideChannelActivity.class);
                        intent.putExtra("channel_id", videos.get(position).getChannel_id());
                        intent.putExtra("channel_name", videos.get(position).getChannel_name());
                        context.startActivity(intent);
                    }
                });
                _viewHolder.cover.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                            showPopup(_viewHolder.menu_anchor);
                        //_viewHolder.menu.setVisibility(View.VISIBLE);
                        //_viewHolder.menu.startAnimation(fadeIn);
                        //_viewHolder.cover.setEnabled(false);
                        return false;
                    }
                });

                _viewHolder.menu.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        _viewHolder.menu.startAnimation(fadeOut2);
                       _viewHolder.menu.setVisibility(View.GONE);
                        _viewHolder.cover.setEnabled(true);
                    }
                });

                _viewHolder.cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(context, "Clicked on "+ type  +" video", Toast.LENGTH_SHORT).show();
                        Intent intent;
                        /*
                        if (!hasWatched) {
                            Toast.makeText(context, "Video watched!", Toast.LENGTH_SHORT).show();
                            new DatabaseHandler(context).addWatchedVideo(id);
                            _viewHolder.watched_indicator.setVisibility(View.VISIBLE);
                            setGreyScale(_viewHolder.cover);
                        }*/

                        switch (type) {
                            case "v":
                                intent = new Intent(context, VimeoPlayerActivity.class);
                                intent.putExtra("video_id", videos.get(position).getVideoId());
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
                                intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, videos.get(position).getVideoId());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                break;
                            case "dm":
                                intent = new Intent(context, DailyMotionPlayerActivity.class);
                                intent.putExtra("video_id", videos.get(position).getVideoId());
                                context.startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    }
                });


                break;
            default:
                break;
        }
    }

    public class ViewHolderVideo extends RecyclerView.ViewHolder {

        TextView title_textview, date_textview, channel_textview, provider_textview, type_textview, desc_textview, watched_indicator;
        ProgressBar progressBar;
        ImageView cover, play;
        ImageView provider_thumb;
        View header, menu_anchor;
        LinearLayout menu;
        RelativeLayout cover_holder;
        LinearLayout root;




        public ViewHolderVideo(View view) {
            super(view);

            menu_anchor = view.findViewById(R.id.menu_anchor);
            root = (LinearLayout) view.findViewById(R.id.root);
            desc_textview = (TextView) view.findViewById(R.id.desc);
            header = view.findViewById(R.id.header);
            watched_indicator = (TextView) view.findViewById(R.id.watched_indicator);
            title_textview = (TextView) view.findViewById(R.id.title);
            date_textview = (TextView) view.findViewById(R.id.date);
            channel_textview = (TextView) view.findViewById(R.id.channel);
            provider_textview = (TextView) view.findViewById(R.id.provider);
            type_textview = (TextView) view.findViewById(R.id.type);
            progressBar = (ProgressBar) view.findViewById(R.id.pb);
            cover = (ImageView) view.findViewById(R.id.cover);
            play = (ImageView) view.findViewById(R.id.play);
            cover_holder = (RelativeLayout) view.findViewById(R.id.cover_holder);
            provider_thumb = (ImageView) view.findViewById(R.id.imageView);
            menu = (LinearLayout) view.findViewById(R.id.menu_holder);
        }

    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public class ViewHolderSuggestChannels extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;


        public ViewHolderSuggestChannels(View view) {
            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new MyLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
        }

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


    public class MyLinearLayoutManager extends LinearLayoutManager {

        public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout)    {
            super(context, orientation, reverseLayout);
        }

        private int[] mMeasuredDimension = new int[2];

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                              int widthSpec, int heightSpec) {
            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);
            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);
            int width = 0;
            int height = 0;
            for (int i = 0; i < getItemCount(); i++) {

                measureScrapChild(recycler, i,
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension);

                if (getOrientation() == HORIZONTAL) {
                    width = width + mMeasuredDimension[0];
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    height = height + mMeasuredDimension[1];
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }
            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                    width = widthSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            setMeasuredDimension(width, height);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                       int heightSpec, int[] measuredDimension) {
            View view = recycler.getViewForPosition(position);
            if (view != null) {
                RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
                int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                        getPaddingLeft() + getPaddingRight(), p.width);
                int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                        getPaddingTop() + getPaddingBottom(), p.height);
                view.measure(childWidthSpec, childHeightSpec);
                measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                recycler.recycleView(view);
            }
        }
    }

    public boolean isWhitespace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.video_menu, popup.getMenu());
        popup.show();
    }
}

